package hr.servis.kontroleri;

import hr.servis.entiteti.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserMainPageController {
    public static Integer BROJ_ZAPISA_USER = 5;
    Logger logger = LoggerFactory.getLogger(UserMainPageController.class);
    public static Integer userId = 0;
    @FXML
    public Label welcomeLabel;

    public void initialize() {

        //Dohvacanje Usera datoteke u listu
        List<User> listaUser = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String FILE_NAME = "dat/login.txt";
        try (BufferedReader admin = new BufferedReader(new FileReader(FILE_NAME))) {

            List<String> datotekaUsera = admin.lines().collect(Collectors.toList());

            for (int i = 0; i < datotekaUsera.size() / BROJ_ZAPISA_USER; i++) {
                String id = datotekaUsera.get(i * BROJ_ZAPISA_USER);
                String username = datotekaUsera.get(i * BROJ_ZAPISA_USER +1);
                String password = datotekaUsera.get(i * BROJ_ZAPISA_USER +2);
                Integer csv = Integer.valueOf(datotekaUsera.get(i * BROJ_ZAPISA_USER +3));

                String datumRodjenjaString = datotekaUsera.get(i * BROJ_ZAPISA_USER +4);
                LocalDate datumRodjenja = LocalDate.parse(datumRodjenjaString, formatter);

                listaUser.add(new User(Integer.valueOf(id), username, password, csv, datumRodjenja));
            }
        } catch (IOException e) {
            logger.error("Pogreška u čitanju User datoteke u UserMainPageController-u u initialize() metodi!", e);
            System.err.println(e);
        }

        //Namjesti Username Usera u welcomeLabel
        for(int i = 0; i< listaUser.size(); i++){
            if(listaUser.get(i).getId().equals(userId)){
                welcomeLabel.setText("Welcome " + listaUser.get(i).getUsername());
                break;
            }
        }

    }

    @FXML
    public void userPitanja() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(ServisRacunala.class.getResource("userPitanja.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        ServisRacunala.getMainStage().setTitle("PopIT");
        ServisRacunala.getMainStage().setScene(scene);
        ServisRacunala.getMainStage().show();

    }

    @FXML
    public void userOdgovori() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(ServisRacunala.class.getResource("userOdgovori.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 400);
        ServisRacunala.getMainStage().setTitle("PopIT");
        ServisRacunala.getMainStage().setScene(scene);
        ServisRacunala.getMainStage().show();

    }

    @FXML
    public void userPopravci() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(ServisRacunala.class.getResource("userPopravci.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 800);
        ServisRacunala.getMainStage().setTitle("PopIT");
        ServisRacunala.getMainStage().setScene(scene);
        ServisRacunala.getMainStage().show();

    }

    @FXML
    public void userNarudzbe() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(ServisRacunala.class.getResource("userNarudzbe.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        ServisRacunala.getMainStage().setTitle("PopIT");
        ServisRacunala.getMainStage().setScene(scene);
        ServisRacunala.getMainStage().show();

    }
    @FXML
    public void goBackUser() throws IOException {

        UserLogInController.isUserLoggedIn = false;
        FXMLLoader fxmlLoader = new FXMLLoader(ServisRacunala.class.getResource("userLogIn.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        ServisRacunala.getMainStage().setTitle("PopIT");
        ServisRacunala.getMainStage().setScene(scene);
        ServisRacunala.getMainStage().show();

    }



}
