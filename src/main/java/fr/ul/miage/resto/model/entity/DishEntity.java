package fr.ul.miage.resto.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.ul.miage.resto.constants.DishType;
import fr.ul.miage.resto.dao.service.BaseService;
import lombok.Data;

import java.util.List;

@Data
public class DishEntity {
    @JsonProperty("_id")
    private String id;
    private Integer price;
    private DishType dishType;
    private List<String> idsProduct;
    private String idCategory;
    private boolean onTheMenu;

    public static int orderDishByType(DishEntity dishEntity1, DishEntity dishEntity2) {
        return dishEntity1.getDishType().getPriority().compareTo(dishEntity2.getDishType().getPriority());
    }

    @Override
    public String toString() {
        StringBuilder toString = new StringBuilder();
        toString.append(id).append(", ").append(price).append("€, ").append(dishType.getDish()).append(", (").append(idCategory).append(")\n");
        for (String product : idsProduct) {
            toString.append(" - ").append(product).append("\n");
        }
        return toString.toString();
    }

    //Retourne true si tous les produits du plat ont le stock suffisant
    public boolean checkStock(BaseService baseService) {
        for (String product : idsProduct) {
            if (baseService.getProductById(product).getStock() == 0) {
                return false;
            }
        }
        return true;
    }

    //Ajoute 1 au stock de chaque produit si add = true sinon retire 1
    public void changeStock(BaseService baseService, boolean add) {
        for (String product : idsProduct) {
            ProductEntity productEntity = baseService.getProductById(product);
            if (add) {
                productEntity.setStock(productEntity.getStock() + 1);
            } else {
                productEntity.setStock(productEntity.getStock() - 1);
            }
            baseService.update(productEntity);
        }
    }
}
