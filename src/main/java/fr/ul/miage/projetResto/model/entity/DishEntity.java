package fr.ul.miage.projetResto.model.entity;

import fr.ul.miage.projetResto.constants.DishType;
import fr.ul.miage.projetResto.dao.service.BaseService;
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

    //Retourne true si tous les produits du plat ont le stock suffisant
    protected boolean checkStock(BaseService baseService) {
        for (String product : idsProduct) {
            if (baseService.getProductById(product).getStock() == 0) {
                return false;
            }
        }
        return true;
    }

    //Ajoute 1 au stock de chaque produit si add = true sinon retire 1
    protected void changeStock(BaseService baseService, boolean add) {
        for (String product : idsProduct) {
            ProductEntity productEntity = baseService.getProductById(product);
            if (add) {
                productEntity.setStock(productEntity.getStock() + 1);
            } else {
                productEntity.setStock(productEntity.getStock() - 1);
            }
            baseService.save(productEntity);
        }
    }

}
