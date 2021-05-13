package fr.ul.miage.resto.constants;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import fr.ul.miage.resto.model.jackson.deserializer.RoleDeserializer;
import fr.ul.miage.resto.model.jackson.serializer.EnumSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@JsonSerialize(converter = EnumSerializer.class)
@JsonDeserialize(converter = RoleDeserializer.class)
public enum Role implements EnumInBase {
    DIRECTOR("Directeur"),
    BUTLER("Maître d'hôtel"),
    SERVER("Serveur"),
    HELPER("Assistant de service"),
    COOK("Cuisinier");

    private final String value;
}
