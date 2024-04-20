package hr.servis.entiteti;

import java.io.Serializable;

public class Admin extends Entitet implements Serializable {

    private Integer id;
    private String username;
    private String password;


    public Admin(Integer id, String username, String password) {
        super(id);
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
