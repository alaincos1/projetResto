package fr.ul.miage.projetResto.controller.role;

import fr.ul.miage.projetResto.Launcher;
import fr.ul.miage.projetResto.constants.OrderState;
import fr.ul.miage.projetResto.constants.Role;
import fr.ul.miage.projetResto.controller.feature.LogInController;
import fr.ul.miage.projetResto.model.entity.OrderEntity;
import fr.ul.miage.projetResto.utils.InputUtil;
import fr.ul.miage.projetResto.view.role.CookView;

import java.util.List;

public class CookController extends RoleMenuController {
    CookView cookView = new CookView();

    @Override
    public void callAction(Integer action) {
        Role role = Launcher.getLoggedUser().getRole();
        switch (action) {
            case 0:
                if (role.equals(Role.Director)) {
                    DirectorController directorController = new DirectorController();
                    directorController.launch(Role.Director);
                } else {
                    LogInController logInController = new LogInController();
                    logInController.disconnect();
                }
                break;
            case 1:
                viewOrdersList();
                break;
            case 2:
                setOrderReady();
                break;
            case 3:
                createDishes();
                break;
            case 4:
                endCooking();
                break;
        }
    }

    protected void viewOrdersList() {
        List<OrderEntity> orders = Launcher.getBaseService().getNotPreparedOrders();
        if (orders.isEmpty()) {
            cookView.displayNoOrderToPrepare();
            launch(Role.Cook);
        } else {
            cookView.displayOrdersList(orders);
            askMainMenu();
        }
    }

    protected void setOrderReady() {
        List<OrderEntity> orders = Launcher.getBaseService().getNotPreparedOrders();
        if (!orders.isEmpty()) {
            cookView.displayOrdersList(orders);
            cookView.displayWhichOrderPrepared();
            Integer input = InputUtil.getIntegerInput(0, orders.size()) - 1;
            if (input != -1) {
                orders.get(input).setOrderState(OrderState.Prepared);
                Launcher.getBaseService().update(orders.get(input));
                cookView.displayOrderPrepared(orders.get(input).get_id(), orders.get(input).getIdTable());
                if (doAgain()) {
                    setOrderReady();
                }
            }
        } else {
            cookView.displayNoOrderToPrepare();
        }
        launch(Role.Cook);
    }

    protected void createDishes() {
    }

    protected void endCooking() {
        if (!Launcher.getService().isEndNewClients()) {
            cookView.displayAskEndCooking();
            Integer input = InputUtil.getIntegerInput(0, 1);
            if (input == 1) {
                Launcher.getService().setEndNewClients(true);
                cookView.displayEnded();
            }
        } else {
            cookView.displayAlreadyEnded();
        }
        launch(Role.Cook);
    }
}
