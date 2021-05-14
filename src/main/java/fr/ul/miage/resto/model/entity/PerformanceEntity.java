package fr.ul.miage.resto.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.ul.miage.resto.constants.MealType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PerformanceEntity {
    @JsonProperty("_id")
    private String id;
    private MealType mealType;
    private Integer serviceTime;
    private Integer nbTableServed;
    private Integer preparationTime;
    private Integer nbOrder;

    public void update(String label, Integer time) {
        if (label.equals("serviceTime")) {
            this.serviceTime += time;
            this.nbTableServed += 1;
        }
        if (label.equals("preparationTime")) {
            this.preparationTime += time;
            this.nbOrder += 1;
        }
    }

    public void initPerf(String id, String label, Integer time) {
        this.id = id;
        this.mealType = MealType.valueOf(id.substring(10));
        if (label.equals("serviceTime")) {
            this.serviceTime = time;
            this.nbTableServed = 1;
            this.preparationTime = 0;
            this.nbOrder = 0;
        }
        if (label.equals("preparationTime")) {
            this.preparationTime = time;
            this.nbOrder = 1;
            this.serviceTime = 0;
            this.nbTableServed = 0;
        }
    }
}
