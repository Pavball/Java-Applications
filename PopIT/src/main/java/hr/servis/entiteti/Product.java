package hr.servis.entiteti;

import java.io.Serializable;

public class Product implements Serializable {

    private String productName;

    public Product(String productName) {
        this.productName = productName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Override
    public String toString() {
        return "" + productName;
    }
}
