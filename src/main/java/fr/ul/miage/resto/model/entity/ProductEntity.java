package fr.ul.miage.resto.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProductEntity {
    @JsonProperty("_id")
    private String id;
    private Integer stock;
}
