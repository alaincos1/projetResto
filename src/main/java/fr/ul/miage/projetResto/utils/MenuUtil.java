package fr.ul.miage.projetResto.utils;

import fr.ul.miage.projetResto.constants.DishType;
import fr.ul.miage.projetResto.model.entity.DishEntity;
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
