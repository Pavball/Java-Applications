package hr.servis.iznimke;

public class BazaPodatakaException extends Exception{

    public BazaPodatakaException() {
        super("Dogodila se pogreska u radu baze!");
    }

    public BazaPodatakaException(String poruka) {
        super(poruka);
    }
    public BazaPodatakaException(Throwable uzrok) {
        super(uzrok);
    }

    public BazaPodatakaException(String poruka, Throwable uzrok) {
        super(poruka, uzrok);
    }

}
