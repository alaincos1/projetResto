package fr.ul.miage.projetResto.model.entity;

import lombok.Data;

@Data
public class BookingEntity {
    private String _id;
    private String mealType;
    private String reservationName;
    private String idTable;
}
