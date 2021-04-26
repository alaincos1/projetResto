package fr.ul.miage.projetResto.controller.role;

import fr.ul.miage.projetResto.constants.Features;
import fr.ul.miage.projetResto.constants.Role;
import fr.ul.miage.projetResto.utils.InputUtil;
import fr.ul.miage.projetResto.view.role.RoleView;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
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
        log.debug("Nothing to do with " + action);
    }

    public void askMainMenu() {
        roleView.displayAskReturnMainMenu();
        if (InputUtil.getIntegerInput(0, 0) == 0) {
            launch(roleTemp);
        }
    }
}
