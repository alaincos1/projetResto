package fr.ul.miage.projetResto.model.entity;

import fr.ul.miage.projetResto.constants.DishType;
import fr.ul.miage.projetResto.constants.OrderState;
import fr.ul.miage.projetResto.constants.TableState;
import fr.ul.miage.projetResto.dao.service.BaseService;
import lombok.Data;

import java.util.List;

@Data
public class OrderEntity {
    private String _id;
    private OrderState orderState;
    private Boolean childOrder;
    private Integer rank;
    private List<String> idsDish;
    private String idTable;

    public TableState getDishType(BaseService baseService) {
        for(String dish : idsDish){
            DishEntity dishEntity = baseService.getDishById(dish);
            if(!dishEntity.getDishType().equals(DishType.Drink)){
                return TableState.valueOf(dishEntity.getDishType().toString());
            }
        }
        return null;
    }

    public void giveStockBack(BaseService baseService) {
        for(String id : idsDish){
            DishEntity dishEntity = baseService.getDishById(id);
            dishEntity.changeStock(baseService, true);
        }
    }
}
