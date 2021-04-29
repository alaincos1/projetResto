package fr.ul.miage.projetResto.model.entity;

import fr.ul.miage.projetResto.constants.DishType;
import lombok.Data;

import java.util.List;

@Data
public class DishEntity {
    private String _id;
    private Integer price;
    private DishType dishType;
    private List<String> idsProduct;
    private String idCategory;
    private boolean onTheMenu;

    @Override
    public String toString() {
        StringBuilder toString = new StringBuilder();
        toString.append(_id + ", ");
        toString.append(price + "€, ");
        toString.append(dishType.getDish() + ", (");
        toString.append(idCategory + ")\n");
        for (String product : idsProduct) {
            toString.append(" - " + product + "\n");
        }
        return toString.toString();
    }
}
