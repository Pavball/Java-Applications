package hr.servis.entiteti;

import java.io.Serializable;

public abstract class Entitet implements Serializable {

    private Integer id;

    public Entitet(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


}
