package fr.ul.miage.projetResto.controller.role;

import fr.ul.miage.projetResto.constants.Features;
import fr.ul.miage.projetResto.constants.Role;
import fr.ul.miage.projetResto.utils.InputUtil;
import fr.ul.miage.projetResto.view.role.RoleView;

import java.util.Arrays;
import java.util.stream.Collectors;

public class RoleMenuController {
    protected RoleView roleView = new RoleView();
    protected Integer nbActions;
    protected Role roleTemp;

    public void launch(Role role) {
        this.roleTemp = role;
        setnbActions();
        Integer action = askAction();
        callAction(action);
    }

    public void setnbActions() {
        nbActions = Arrays.stream(Features.values()).filter(
                features -> features.getRole().equals(roleTemp))
                .collect(Collectors.toList()).size();
    }

    public Integer askAction() {
        roleView.displayMenu(roleTemp);
        return InputUtil.getIntegerInput(0, nbActions);
    }

    public void callAction(Integer action) {
    }

}
