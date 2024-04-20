package hr.servis.kontroleri;
import hr.servis.entiteti.LoginInterface;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;



import java.io.IOException;

public class UserLogInController implements LoginInterface {

    public static boolean isUserLoggedIn = false;
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
    public void goBackUser() throws IOException {

        goBack();
    }

    @FXML
    public void loginUser() throws IOException {

     dohvatiFile(usernameTextField, passwordField);

    }






}
