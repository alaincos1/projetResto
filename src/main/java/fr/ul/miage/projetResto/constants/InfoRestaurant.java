package fr.ul.miage.projetResto.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InfoRestaurant {
    MAX_PRICE(25),
    MAX_CHOICES(10),
    MAX_LENGTH_NAME(30);

    private final int value;
}
