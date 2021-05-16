package fr.ul.miage.resto.controller.role;

import fr.ul.miage.resto.Launcher;
import fr.ul.miage.resto.appinfo.Service;
import fr.ul.miage.resto.constants.MealType;
import fr.ul.miage.resto.constants.OrderState;
import fr.ul.miage.resto.constants.Role;
import fr.ul.miage.resto.constants.TableState;
import fr.ul.miage.resto.dao.service.BaseService;
import fr.ul.miage.resto.model.entity.*;
import fr.ul.miage.resto.view.role.ButlerView;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class ButlerController extends RoleController {
    private final BaseService baseService;
    private final Service service;
    private final ButlerView butlerView;

    @Override
    public void callAction(Integer action) {
        Role role = Launcher.getLoggedUser().getRole();
        switch (action) {
            case 0:
                goBackOrDisconnect(role, baseService, service, false);
                break;
            case 1:
                affectTablesToServer();
                break;
            case 2:
                editBills();
                break;
            case 3:
                if (service.isEndNewClients()) {
                    butlerView.displayMessage("Malheureusement nous n'acceptons plus de nouveaux clients mais vous pouvez réserver ou alors revenir demain :)");
                    launch(Role.BUTLER);
                } else {
                    affectTablesToClients();
                }
                break;
            case 4:
                takeBookings();
                break;
            default:
                break;
        }
    }

    /**
     * Affecter un serveur/assistant à une table
     */
    protected void affectTablesToServer() {
        List<UserEntity> users = baseService.getAllUsers();
        List<TableEntity> tables = baseService.getAllTables();
        butlerView.displayAllTables(tables);
        butlerView.displayMessage("Choisissez le serveur que vous voulez affecter à une table.");
        butlerView.displayServersList(users);

        String choiceUser = getUserIdInput();
        UserEntity user = baseService.getUserById(choiceUser);
        String choiceTable = "";
        if (!StringUtils.isBlank(choiceUser) && ObjectUtils.isEmpty(user)) {
            butlerView.displayInputIncorrect();
            affectTablesToServer();
        } else {
            butlerView.displayMessage("Choisissez la table à affecter à " + choiceUser + ". Entrez l'Id de la table.");
            butlerView.displayTablesList(tables, choiceUser, null);
            choiceTable = choiceTableServer(user);
        }

        TableEntity tableToChange = baseService.getTableById(choiceTable);

        if (ObjectUtils.isNotEmpty(user)) {
            if (Role.SERVER.equals(user.getRole())) {
                tableToChange.setIdServer(choiceUser);
            } else {
                tableToChange.setIdHelper(choiceUser);
            }
        }

        updateObject(tableToChange);
        launch(Role.BUTLER);
    }

    /**
     * Editer une facture
     */
    protected void editBills() {
        List<TableEntity> tables = baseService.getAllTables();
        if (butlerView.displayAllTablesForBill(tables, this) != 0) {
            butlerView.displayMessage("Choisissez la table pour éditer sa facture. Entrez son id.");

            String choiceTable = getStringInput();
            TableEntity tableChoice = baseService.getTableById(choiceTable);

            if (Boolean.TRUE.equals(ObjectUtils.isEmpty(tableChoice) || !ordersServed(tableChoice)
                    || !stateForBill(tableChoice))) {
                butlerView.displayInputIncorrect();
                editBills();
            } else {
                List<String> listIdDishes = listDishes(tableChoice);
                Integer priceTotal = priceBill(listIdDishes);
                butlerView.displayMessage("Le prix total de cette facture est " + priceTotal + " euros.");
                BillEntity bill = new BillEntity();
                bill.setId(new ObjectId().toString());
                bill.setDate(service.getDate());
                bill.setMealType(service.getMealType());
                bill.setTotalPrice(priceTotal);
                bill.setIdsOrder(listIdDishes);
                tableChoice.setTableState(TableState.DIRTY);
                saveObject(tableChoice);
                saveObject(bill);
                changeOrderState();
                savePerformance(service, baseService, "serviceTime", 30, 120);
            }

        } else {
            butlerView.displayMessage("Aucune facture ne peut-être éditée.");
        }

        launch(Role.BUTLER);
    }

    /**
     * Savoir si des commandes ont été servies à cette table
     *
     * @param table la table
     * @return boolean
     */
    public Boolean ordersServed(TableEntity table) {
        List<OrderEntity> orders = baseService.getServedOrders();
        for (OrderEntity order : orders) {
            if (order.getIdTable().equals(table.getId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Savoir si l'état de la table est correct pour éditer la facture
     *
     * @param table la table
     * @return boolean
     */
    public Boolean stateForBill(TableEntity table) {
        return table.getTableState() != TableState.BOOKED && table.getTableState() != TableState.FREE
                && table.getTableState() != TableState.OCCUPIED;
    }

    /**
     * Affecter les clients qui arrivent dans le restaurant à une table
     */
    protected void affectTablesToClients() {
        List<TableEntity> tables = baseService.getAllTables();

        butlerView.displayMessage("Est ce une réservation ? \n 0) Non \n 1) Oui ");

        Integer reservation = getIntegerInput(0, 1);
        String choiceTable = choiceReservation(tables, reservation);

        // si il ne veut pas de réservation ou qu'il n'y en a pas pour ce jour
        if ((reservation == 1 && StringUtils.isEmpty(choiceTable)) || reservation == 0) {
            if (butlerView.displayTablesList(tables, null, TableState.FREE.getState()) != 0) {
                butlerView.displayChoiceTableClient();
                choiceTable = choiceTable(TableState.FREE);
            } else {
                butlerView.displayMessage("Malheureusement, notre restaurant est complet !");
                launch(Role.BUTLER);
            }
        }

        TableEntity tableToChange = baseService.getTableById(choiceTable);
        tableToChange.setTableState(TableState.OCCUPIED);

        updateObject(tableToChange);
        launch(Role.BUTLER);
    }

    /**
     * Prendre une réservation
     */
    protected void takeBookings() {
        butlerView.displayMessage("Entrez la date de réservation au format AAAA/MM/JJ.");
        String dateBooking = getDateInput();

        MealType mealTypeBooking = choiceMealTypeWithDate(dateBooking);

        butlerView.displayMessage("Entrez le nom de réservation.");
        String nameBooking = getStringInput();

        List<TableEntity> tables = baseService.getAllTables();
        butlerView.displayAllTablesWithNoBooking(tables, dateBooking, mealTypeBooking, baseService);
        butlerView.displayChoiceTableClient();
        String choiceTable = choiceTable(null);

        BookingEntity booking = new BookingEntity();
        booking.setId(new ObjectId().toString());
        booking.setDate(dateBooking);
        booking.setIdTable(choiceTable);
        booking.setMealType(mealTypeBooking);
        booking.setReservationName(nameBooking);

        saveObject(booking);
        launch(Role.BUTLER);
    }

    /**
     * Affectation du mealtype possible pour une réservation en fonction de la date
     *
     * @param dateBooking date souhaité de la réservation
     * @return MealType
     */
    protected MealType choiceMealTypeWithDate(String dateBooking) {
        MealType mealTypeBooking = null;

        // Si la reservation est pour la date du jour et qu'on est au service du diner
        // alors la reservation est impossible
        if (dateBooking.equals(service.getDate()) && service.getMealType() == MealType.DINNER) {
            butlerView.displayMessage("Impossible de créer une réservation pour aujourd'hui.");
            launch(Role.BUTLER);
        }
        // Si la reservation est pour le jour et qu'on est au service du dejeuner alors
        // la reservation est automatique pour le soir
        else if (dateBooking.equals(service.getDate())) {
            butlerView.displayBookingDiner();
            Integer correct = getIntegerInput(0, 1);
            if (correct == 0) {
                launch(Role.BUTLER);
            } else {
                mealTypeBooking = MealType.DINNER;
            }
        }
        // Si c'est pour un date ultérieur on deande le service
        else {
            butlerView.displayMessage("Pour quel service est la réservation? \n0) Dejeuner \n1) Diner");
            Integer choiceMealType = getIntegerInput(0, 1);
            mealTypeBooking = choiceMealType(choiceMealType);
        }
        return mealTypeBooking;
    }

    /**
     * Fonction qui permet de définir le service en fonction d l'entrée de
     * l'utilisateur
     *
     * @param choiceMealType le mealType choisi
     * @return Entrée de l'utilsiateur (0/1)
     */
    protected MealType choiceMealType(Integer choiceMealType) {
        MealType mealTypeBooking;
        if (choiceMealType == 0) {
            mealTypeBooking = MealType.LUNCH;
        } else {
            mealTypeBooking = MealType.DINNER;

        }
        return mealTypeBooking;
    }

    /**
     * Retourne l'id de la table entrée par l'utilisateur si elle est correcte et
     * correspond à l'état voulu
     *
     * @param state l'état voulu
     * @return Id de la table
     */
    protected String choiceTable(TableState state) {
        String choiceTable = getStringInput();

        if (!StringUtils.isBlank(choiceTable) && !isTableIdCorrect(choiceTable, state)) {
            butlerView.displayInputIncorrect();
            return choiceTable(state);
        }
        return choiceTable;
    }

    /**
     * Savoir si une table existe en fonction de son id et de son état Par exemple
     * savoir si le table avec l'id 1 existe et est libre
     *
     * @param tableId id de la table
     * @param state   état souhaité
     * @return boolean
     */
    protected boolean isTableIdCorrect(String tableId, TableState state) {
        TableEntity table = baseService.getTableById(tableId);
        return ObjectUtils.isNotEmpty(table) && (state == null || table.getTableState() == state);
    }

    /**
     * Retourne l'id de la table entrée par l'utilisateur si elle existe et que
     * l'user en parametre n'est ni son serveur ni son assistant
     *
     * @param user l'utilisateur
     * @return id de la table
     */
    protected String choiceTableServer(UserEntity user) {
        String choiceTable = getStringInput();

        if (!StringUtils.isBlank(choiceTable) && !isTableIdCorrectServer(choiceTable, user)) {
            butlerView.displayInputIncorrect();
            return choiceTableServer(user);
        }
        return choiceTable;
    }

    /**
     * Savoir si une table existe en fonction de son id et d'un user Par exemple
     * savoir si le table avec l'id 1 existe et que "ser" n'est ni son serveur ni
     * son assistant
     *
     * @param tableId l'id de la table
     * @param user    l'utilisateur
     * @return boolean
     */
    protected boolean isTableIdCorrectServer(String tableId, UserEntity user) {
        TableEntity table = baseService.getTableById(tableId);
        return ObjectUtils.isNotEmpty(table) && !table.getIdServer().equals(user.getId())
                && !table.getIdHelper().equals(user.getId());
    }

    /**
     * Retourne l'id de la table qui correspond à l'entrée de l'utilisateur si le
     * client avait réservé
     *
     * @param tables      liste de tables
     * @param reservation entrée pour savoir si c'est une réservation ou pas (0/1)
     * @return id de la table
     */
    protected String choiceReservation(List<TableEntity> tables, Integer reservation) {
        String choiceTable = null;
        if (reservation == 1) {
            if (butlerView.displayAllTablesWithBooking(tables, service.getDate(), service.getMealType(),
                    baseService) != 0) {
                butlerView.displayChoiceTableClient();
                choiceTable = choiceTable(TableState.BOOKED);
            } else {
                butlerView.displayMessage("Il n'y a pas de réservation ce jour.");
            }
        }
        return choiceTable;
    }

    /**
     * Mettre à jour un objet (table...)
     *
     * @param o l'objet à sauvegarder en base
     */
    protected void updateObject(Object o) {
        if (baseService.update(o)) {
            butlerView.displaySuccess();
        } else {
            butlerView.displayError();
            launch(Role.BUTLER);
        }
    }

    /**
     * Insere un objet (facture, réservation..)
     *
     * @param o l'objet à sauvegarder en base
     */
    protected void saveObject(Object o) {
        if (baseService.save(o)) {
            butlerView.displaySuccess();
        } else {
            butlerView.displayError();
            launch(Role.BUTLER);
        }
    }

    /**
     * retourne la liste des plats d'une table (toutes les commandes servies)
     *
     * @param table la table
     * @return la liste des id des plats
     */
    public List<String> listDishes(TableEntity table) {
        List<OrderEntity> orders = baseService.getServedOrders();
        List<String> dishes = new ArrayList<>();
        for (OrderEntity order : orders) {
            if (order.getIdTable().equals(table.getId())) {
                dishes.addAll(order.getIdsDish());
            }
        }
        return dishes;
    }

    /**
     * Calculer le prix total des plats de la liste en parametre
     *
     * @param listIdDishes liste deplat
     * @return le prix total
     */
    public Integer priceBill(List<String> listIdDishes) {
        Integer priceOrderDouble = 0;
        for (String idDish : listIdDishes) {
            DishEntity dish = baseService.getDishById(idDish);
            butlerView.displayMessage("- " + dish.getId() + " : " + dish.getPrice() + " euros");
            priceOrderDouble += dish.getPrice();
        }
        return priceOrderDouble;
    }

    /**
     * Mettre à jour l'état des commandes lorsqu'elles sont payées
     */
    public void changeOrderState() {
        List<OrderEntity> orders = baseService.getServedOrders();
        for (OrderEntity order : orders) {
            order.setOrderState(OrderState.CHECKED);
            updateObject(order);
        }
    }

}
