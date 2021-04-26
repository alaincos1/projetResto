package fr.ul.miage.projetResto.constants;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import fr.ul.miage.projetResto.model.jackson.deserializer.RoleDeserializer;
import fr.ul.miage.projetResto.model.jackson.serializer.EnumSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@JsonSerialize(converter = EnumSerializer.class)
@JsonDeserialize(converter = RoleDeserializer.class)
public enum Role implements EnumInBase {
    Director("Directeur"),
    Butler("Maître d'hôtel"),
    Server("Serveur"),
    Helper("Assistant de service"),
    Cook("Cuisinier");

    private final String value;
}
