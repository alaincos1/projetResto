package fr.ul.miage.projetResto.view.role;

import fr.ul.miage.projetResto.model.entity.OrderEntity;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class CookView extends RoleView {

    public void displayAskEndCooking() {
        System.out.println("Souhaitez vous annoncer la fin de prise en charge des nouveaux clients ? Choix définitif." +
                "\n O) Non" +
                "\n 1) Oui");
    }

    public void displayAlreadyEnded() {
        System.out.println("La fin de prise en charge des nouveaux clients a déjà été annoncée.");
    }

    public void displayEnded() {
        System.out.println("La fin de prise en charge des nouveaux clients est annoncée.");
    }

    public void displayOrdersList(List<OrderEntity> orders) {
        int i = 1;
        for (OrderEntity order : orders) {
            System.out.println(" " + i + ") Id: " + order.get_id() + " Table: " + order.getIdTable() + (Boolean.TRUE.equals(order.getChildOrder()) ? " PRIORITAIRE ENFANT" : StringUtils.EMPTY));
            for (String dish : order.getIdsDish()) {
                System.out.println("  -> " + dish);
            }
            i++;
        }
    }
}
