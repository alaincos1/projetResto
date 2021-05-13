package fr.ul.miage.resto.controller.role;

import fr.ul.miage.resto.appinfo.Service;
import fr.ul.miage.resto.constants.Features;
import fr.ul.miage.resto.constants.Role;
import fr.ul.miage.resto.dao.service.BaseService;
import fr.ul.miage.resto.model.entity.PerformanceEntity;
import fr.ul.miage.resto.utils.DateDto;
import fr.ul.miage.resto.utils.DateUtil;
import fr.ul.miage.resto.utils.InputUtil;
import fr.ul.miage.resto.view.role.RoleView;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Slf4j
public class RoleMenuController {
    protected RoleView roleView = new RoleView();
    protected Integer nbActions;
    protected Role roleTemp;
    private static final Random random = new Random();

    public void launch(Role role) {
        this.roleTemp = role;
        setNbActions();
        Integer action = askAction();
        callAction(action);
    }

    public void setNbActions() {
        nbActions = (int) Arrays.stream(Features.values()).filter(
                features -> features.getRole().equals(roleTemp)).count();
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

    public boolean doAgain() {
        return InputUtil.getIntegerInput(0, 1) == 1;
    }

    public Integer getIntegerInput(Integer min, Integer max) {
        return InputUtil.getIntegerInput(min, max);
    }

    public String getDateInput() {
        return InputUtil.getDateInput();
    }

    public String getUserIdInput() {
        return InputUtil.getUserIdInput();
    }

    public String getStringCommandInput(Integer min, Integer max) {
        return InputUtil.getStringCommandInput(min, max);
    }

    public String getStringInput() {
        return InputUtil.getStringInput();
    }

    public String getStringMultipleChoices(Integer min, Integer max) {
        return InputUtil.getStringMultipleChoices(min, max);
    }

    /**
     * Sauvegarde les performances en fonction du label
     */
    protected void savePerformance(Service service, BaseService baseService, String label, Integer min, Integer max) {
        String idPerf = service.getDate() + service.getMealType().toString();
        PerformanceEntity perf = baseService.getPerformanceById(idPerf);
        Integer time = random.nextInt((max - min) + 1) + min;
        if (perf == null) {
            perf = new PerformanceEntity();
            perf.initPerf(idPerf, label, time);
            baseService.save(perf);
        } else {
            perf.update(label, time);
            baseService.update(perf);
        }
    }

    protected List<DateDto> getListDate(String type, String date, Integer last) {
        return DateUtil.getListDate(type, date, last);
    }
}
