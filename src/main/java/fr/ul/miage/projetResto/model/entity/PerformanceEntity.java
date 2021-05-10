package fr.ul.miage.projetResto.model.entity;

import lombok.Data;

@Data
public class PerformanceEntity {
    private String _id;
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
        this._id = id;
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
