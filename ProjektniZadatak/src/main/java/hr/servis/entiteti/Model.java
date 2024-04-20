package hr.servis.entiteti;

import java.io.Serializable;

public class Model implements Serializable {

    private String modelName;
    private Integer cijena;

    public Model(String modelName, Integer cijena) {
        this.modelName = modelName;
        this.cijena = cijena;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public Integer getCijena() {
        return cijena;
    }

    public void setCijena(Integer cijena) {
        this.cijena = cijena;
    }

    @Override
    public String toString() {
        return "" + modelName + " | " + "Cijena: " + cijena + " EUR";
    }
}
