package hr.servis.entiteti;

import java.sql.Timestamp;

public class PromjeneUser extends Entitet{

    private Integer id;
    private String staraVrijednost;
    private String novaVrijednost;
    private String adminIme;
    private Timestamp datum_i_vrijeme;

    public PromjeneUser(Integer id, String staraVrijednost, String novaVrijednost, String adminIme, Timestamp datum_i_vrijeme) {
        super(id);
        this.id = id;
        this.staraVrijednost = staraVrijednost;
        this.novaVrijednost = novaVrijednost;
        this.adminIme = adminIme;
        this.datum_i_vrijeme = datum_i_vrijeme;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getStaraVrijednost() {
        return staraVrijednost;
    }

    public void setStaraVrijednost(String staraVrijednost) {
        this.staraVrijednost = staraVrijednost;
    }

    public String getNovaVrijednost() {
        return novaVrijednost;
    }

    public void setNovaVrijednost(String novaVrijednost) {
        this.novaVrijednost = novaVrijednost;
    }

    public String getAdminIme() {
        return adminIme;
    }

    public void setAdminIme(String adminIme) {
        this.adminIme = adminIme;
    }

    public Timestamp getDatum_i_vrijeme() {
        return datum_i_vrijeme;
    }

    public void setDatum_i_vrijeme(Timestamp datum_i_vrijeme) {
        this.datum_i_vrijeme = datum_i_vrijeme;
    }
}
