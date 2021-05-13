package fr.ul.miage.resto.utils;

import fr.ul.miage.resto.constants.DishType;
import fr.ul.miage.resto.model.entity.DishEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class MenuUtil {
    private DishType dishType;
    private boolean dishesAvailable;
    private List<DishEntity> dishes;
}
