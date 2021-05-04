package fr.ul.miage.projetResto.dao;

import fr.ul.miage.projetResto.appinfo.Service;
import fr.ul.miage.projetResto.constants.OrderState;
import fr.ul.miage.projetResto.constants.Role;
import fr.ul.miage.projetResto.constants.TableState;
import fr.ul.miage.projetResto.dao.service.BaseService;
import fr.ul.miage.projetResto.model.entity.BookingEntity;
import fr.ul.miage.projetResto.model.entity.OrderEntity;
import fr.ul.miage.projetResto.model.entity.TableEntity;
import fr.ul.miage.projetResto.model.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class InitRestaurant {
    private final Service service;
    private final BaseService baseService;

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
                order.setOrderState(OrderState.Unchecked);
                baseService.update(order);
            });
        }
    }

    public void initTableState() {
        List<TableEntity> tableEntities = baseService.getAllTable();
        List<BookingEntity> bookingEntities = baseService.getAllBooking();

        if (CollectionUtils.isEmpty(bookingEntities) || CollectionUtils.isEmpty(tableEntities)) {
            return;
        }

        List<String> idsReservedTable = bookingEntities.stream()
                .filter(bookingEntity -> StringUtils.equals(bookingEntity.getDate(), service.getDate()) &&
                        bookingEntity.getMealType() == service.getMealType())
                .map(bookingEntity -> bookingEntity.getIdTable())
                .collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(idsReservedTable)) {
            tableEntities.forEach(table -> {
                if (idsReservedTable.contains(table.get_id())) {
                    table.setTableState(TableState.Booked);
                } else {
                    table.setTableState(TableState.Free);
                }

                baseService.update(table);
            });
        }
    }

    //Si aucun utilisateur est en base, en insère un avec le role Director et l'identifiant admin
    protected void initUsers() {
        if(baseService.getAllUsers().isEmpty()){
            UserEntity admin = new UserEntity();
            admin.set_id("admin");
            admin.setRole(Role.Director);
            baseService.save(admin);
            System.out.println("Un seul utilisateur disponible : admin");
        }
    }
}
