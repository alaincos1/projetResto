package fr.ul.miage.resto.model.entity;

import fr.ul.miage.resto.constants.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    private String _id;
    private Role role;
}
