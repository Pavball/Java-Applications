module hr.servis.kontroleri {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;
    requires java.sql;
    requires java.desktop;
    requires itextpdf;


    opens hr.servis.kontroleri to javafx.fxml;
    exports hr.servis.kontroleri;
}