package hr.servis.kontroleri;

import hr.servis.entiteti.LoginInterface;
import javafx.fxml.FXML;

import java.io.IOException;


import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class AdminLogInController implements LoginInterface {

    public static boolean isAdminLoggedIn = false;
    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label kriviLabel;

    public static Label kriviLabelStatic;

    public void initialize(){
        kriviLabelStatic = kriviLabel;
    }

    @FXML
    public void goBackAdmin() throws IOException {

        goBack();

    }

    @FXML
    public void loginAdmin() throws IOException {

        dohvatiFile(usernameTextField, passwordField);

    }


}
