package hr.servis.entiteti;

import java.io.Serializable;

public class Popravci<T, S> implements Serializable {

    T userId;
    S tipPopravka;
    S opisPopravka;

    public Popravci(T userId, S tipPopravka, S opisPopravka) {
        this.userId = userId;
        this.tipPopravka = tipPopravka;
        this.opisPopravka = opisPopravka;
    }

    public T getUserId() {
        return userId;
    }

    public void setUserId(T userId) {
        this.userId = userId;
    }

    public S getTipPopravka() {
        return tipPopravka;
    }

    public void setTipPopravka(S tipPopravka) {
        this.tipPopravka = tipPopravka;
    }

    public S getOpisPopravka() {
        return opisPopravka;
    }

    public void setOpisPopravka(S opisPopravka) {
        this.opisPopravka = opisPopravka;
    }
}
