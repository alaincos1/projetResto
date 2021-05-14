package fr.ul.miage.resto.dao;

import fr.ul.miage.resto.appinfo.Service;
import fr.ul.miage.resto.constants.OrderState;
import fr.ul.miage.resto.constants.Role;
import fr.ul.miage.resto.constants.TableState;
import fr.ul.miage.resto.dao.service.BaseService;
import fr.ul.miage.resto.model.entity.BookingEntity;
import fr.ul.miage.resto.model.entity.OrderEntity;
import fr.ul.miage.resto.model.entity.TableEntity;
import fr.ul.miage.resto.model.entity.UserEntity;
import fr.ul.miage.resto.view.GeneralView;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class InitRestaurant {
    private final Service service;
    private final BaseService baseService;
    private static final GeneralView generalView = new GeneralView();

    public void initRestaurant() {
        InsertData.feedData(baseService);
        deletePastBookings();
        initUncheckedOrder();
        initTableState();
        initUsers();
    }

    public void deletePastBookings() {
        baseService.deletePastBooking(service.getDate());
    }

    public void initUncheckedOrder() {
        List<OrderEntity> orders = baseService.getAllNotCheckedOrder();

        if (CollectionUtils.isNotEmpty(orders)) {
            orders.forEach(order -> {
                order.setOrderState(OrderState.UNCHECKED);
                baseService.update(order);
            });
        }
    }

    public void initTableState() {
        List<TableEntity> tableEntities = baseService.getAllTables();
        List<BookingEntity> bookingEntities = baseService.getAllBooking();

        if (CollectionUtils.isEmpty(bookingEntities) || CollectionUtils.isEmpty(tableEntities)) {
            return;
        }

        List<String> idsReservedTable = bookingEntities.stream()
                .filter(bookingEntity -> StringUtils.equals(bookingEntity.getDate(), service.getDate()) &&
                        bookingEntity.getMealType() == service.getMealType())
                .map(BookingEntity::getIdTable)
                .collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(idsReservedTable)) {
            tableEntities.forEach(table -> {
                if (idsReservedTable.contains(table.get_id())) {
                    table.setTableState(TableState.BOOKED);
                } else {
                    table.setTableState(TableState.FREE);
                }

                baseService.update(table);
            });
        }
    }

    //Si aucun utilisateur est en base, en insère un avec le role Director et l'identifiant admin
    protected void initUsers() {
        List<UserEntity> users = baseService.getAllUsers();
        boolean directorHere = users.stream().noneMatch(userEntity -> userEntity.getRole().equals(Role.DIRECTOR));
        if(directorHere){
            UserEntity admin = new UserEntity();
            admin.set_id("admin");
            admin.setRole(Role.DIRECTOR);
            baseService.save(admin);
            generalView.displayMessage("Nouvel utilisateur disponible en tant que Directeur : admin");
        }
    }
}
