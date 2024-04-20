package hr.servis.entiteti;

import java.io.Serializable;

public class Narudzbe<T> implements Serializable {

    T userId;
    T product;
    T model;
    T trenutnoStanje;

    public Narudzbe(T userId, T product, T model, T trenutnoStanje) {
        this.userId = userId;
        this.product = product;
        this.model = model;
        this.trenutnoStanje = trenutnoStanje;
    }


    public T getUserId() {
        return userId;
    }

    public void setUserId(T userId) {
        this.userId = userId;
    }

    public T getProduct() {
        return product;
    }

    public void setProduct(T product) {
        this.product = product;
    }

    public T getModel() {
        return model;
    }

    public void setModel(T model) {
        this.model = model;
    }

    public T getTrenutnoStanje() {
        return trenutnoStanje;
    }

    public void setTrenutnoStanje(T trenutnoStanje) {
        this.trenutnoStanje = trenutnoStanje;
    }
}
