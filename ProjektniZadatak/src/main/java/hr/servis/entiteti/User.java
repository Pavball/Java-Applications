package hr.servis.entiteti;

import java.io.Serializable;
import java.time.LocalDate;

public class User extends Entitet implements Serializable {

    private Integer id;
    private String username;
    private String password;

    private Integer CSV;
    private LocalDate datumRodjenja;

    public User(int id, String username, String password, Integer CSV, LocalDate datumRodjenja) {
        super(id);
        this.id = id;
        this.username = username;
        this.password = password;
        this.CSV = CSV;
        this.datumRodjenja = datumRodjenja;
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

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCSV() {
        return CSV;
    }

    public void setCSV(Integer CSV) {
        this.CSV = CSV;
    }

    public LocalDate getDatumRodjenja() {
        return datumRodjenja;
    }

    public void setDatumRodjenja(LocalDate datumRodjenja) {
        this.datumRodjenja = datumRodjenja;
    }
}
