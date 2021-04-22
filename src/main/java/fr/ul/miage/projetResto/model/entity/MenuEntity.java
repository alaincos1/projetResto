package fr.ul.miage.projetResto.model.entity;

import lombok.Data;

import java.util.List;

@Data
public class MenuEntity {
    private String _id;
    private List<String> idsDish;
}
