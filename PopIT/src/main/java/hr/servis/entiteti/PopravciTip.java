package hr.servis.entiteti;

import java.io.Serializable;

public class PopravciTip implements Serializable {

    private String naziv;

    public PopravciTip(String naziv) {
        this.naziv = naziv;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    @Override
    public String toString() {
        return "" + naziv;
    }
}
