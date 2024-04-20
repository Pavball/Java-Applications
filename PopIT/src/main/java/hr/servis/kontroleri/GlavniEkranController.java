package hr.servis.kontroleri;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

public class GlavniEkranController {

    public static boolean isAdmin = false;
    public static boolean isUser = false;
    @FXML
    protected void userLoginPage() throws IOException {

        isUser = true;
        FXMLLoader fxmlLoader = new FXMLLoader(ServisRacunala.class.getResource("userLogIn.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        ServisRacunala.getMainStage().setTitle("User Login");
        ServisRacunala.getMainStage().setScene(scene);
        ServisRacunala.getMainStage().show();
    }

    @FXML
    protected void adminLoginPage() throws IOException {
        isAdmin = true;
        FXMLLoader fxmlLoader = new FXMLLoader(ServisRacunala.class.getResource("adminLogIn.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        ServisRacunala.getMainStage().setTitle("Admin Login");
        ServisRacunala.getMainStage().setScene(scene);
        ServisRacunala.getMainStage().show();
    }


}