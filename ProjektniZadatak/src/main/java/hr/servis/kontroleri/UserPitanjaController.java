package hr.servis.kontroleri;

import hr.servis.baza.BazaPodataka;
import hr.servis.iznimke.BazaPodatakaException;
import hr.servis.iznimke.PrazanTextException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import static hr.servis.kontroleri.UserMainPageController.userId;
public class UserPitanjaController {
    private static final Logger logger = LoggerFactory.getLogger(BazaPodataka.class);
    private static String[] data = {"PC", "Mobitel",
            "Igraca konzola", "Popravak", "Narudzba"};
    @FXML
    private ComboBox comboBoxSubject;
    @FXML
    private TextArea textAreaPitanje;

    public void initialize(){
        comboBoxSubject.getItems().addAll(data);
    }

    @FXML
    public void posaljiPitanje() throws BazaPodatakaException, PrazanTextException {
        try{
            BazaPodataka.posaljiPitanje((String) comboBoxSubject.getValue(), textAreaPitanje.getText(), userId);
        }
        catch (Exception e){
            Alert info = new Alert(Alert.AlertType.ERROR);
            info.setTitle("Obavijest");
            info.setHeaderText("Molim popunite sve fieldove.");
            info.showAndWait();
            String poruka = "Molimo, popunite text fieldove.";
            logger.error("Greska kod slanja pitanja jer su text fieldovi prazni!");
            throw new PrazanTextException(poruka, e);

        }
    }

    @FXML
    public void goBackUser() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(ServisRacunala.class.getResource("userMainPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        ServisRacunala.getMainStage().setTitle("PopIT");
        ServisRacunala.getMainStage().setScene(scene);
        ServisRacunala.getMainStage().show();

    }
}
