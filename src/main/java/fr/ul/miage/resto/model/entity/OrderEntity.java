package fr.ul.miage.resto.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.ul.miage.resto.constants.DishType;
import fr.ul.miage.resto.constants.OrderState;
import fr.ul.miage.resto.constants.TableState;
import fr.ul.miage.resto.dao.service.BaseService;
import lombok.Data;

import java.util.List;

@Data
public class OrderEntity {
    @JsonProperty("_id")
    private String id;
    private OrderState orderState;
    private Boolean childOrder;
    private Integer rank;
    private List<String> idsDish;
    private String idTable;

    public TableState getDishType(BaseService baseService) {
        for (String dish : idsDish) {
            DishEntity dishEntity = baseService.getDishById(dish);
            if (!dishEntity.getDishType().equals(DishType.DRINK)) {
                return TableState.valueOf(dishEntity.getDishType().toString());
            }
        }
        return null;
    }

    public void giveStockBack(BaseService baseService) {
        for (String idDish : idsDish) {
            DishEntity dishEntity = baseService.getDishById(idDish);
            dishEntity.changeStock(baseService, true);
        }
    }
}
