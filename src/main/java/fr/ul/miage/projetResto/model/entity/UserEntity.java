package fr.ul.miage.projetResto.model.entity;

import fr.ul.miage.projetResto.constants.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserEntity {
    private String _id;
    private Role role;
}
