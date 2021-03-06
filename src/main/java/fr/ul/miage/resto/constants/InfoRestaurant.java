package fr.ul.miage.resto.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InfoRestaurant {
    MAX_PRICE(25),
    MAX_CHOICES(10),
    MAX_LENGTH_NAME(30),
    MAX_STOCK(100),
    MAX_TABLES(100),
    MAX_SEATS(4);

    private final int value;
}
