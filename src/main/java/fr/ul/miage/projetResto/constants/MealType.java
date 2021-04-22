package fr.ul.miage.projetResto.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MealType {
    Déjeuner(1),
    Dîner(2);

    private Integer id;

    public static MealType getFromId(Integer id){
        switch(id){
            case 1 :
                return Déjeuner;
            case 2 :
                return Dîner;
            default:
                return null;
        }
    }

}
