package fr.ul.miage.projetResto.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {
    Director("Directeur"),
    Butler("Maître d'hôtel"),
    Server("Serveur"),
    Helper("Assistant de service"),
    Cook("Cusinier");

    private String value;
}
