package hr.servis.kontroleri;

import hr.servis.entiteti.Admin;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AdminMainPageController {
    public static Integer BROJ_ZAPISA_ADMIN = 3;
    Logger logger = LoggerFactory.getLogger(AdminMainPageController.class);
    public static Integer adminId = 0;
    @FXML
    public Label welcomeLabel;
    @FXML
    public Label brojPitanjaLabel;

    public static Label brojStaticLabel;

    public void initialize() {

        brojStaticLabel = brojPitanjaLabel;

        //Dohvacanje Admin datoteke u listu
        List<Admin> listaAdmin = new ArrayList<>();
        String FILE_NAME = "dat/loginAdmin.txt";
        try (BufferedReader admin = new BufferedReader(new FileReader(FILE_NAME))) {

            List<String> datotekaAdmina = admin.lines().collect(Collectors.toList());

            for (int i = 0; i < datotekaAdmina.size() / BROJ_ZAPISA_ADMIN; i++) {
                String id = datotekaAdmina.get(i * BROJ_ZAPISA_ADMIN);
                String username = datotekaAdmina.get(i * BROJ_ZAPISA_ADMIN +1);
                String password = datotekaAdmina.get(i * BROJ_ZAPISA_ADMIN +2);


                listaAdmin.add(new Admin(Integer.valueOf(id), username, password));
            }
        } catch (IOException e) {
            logger.error("Pogreška u čitanju Admin datoteke u AdminMainPageControlleru!", e);
            System.err.println(e);
        }

        //Namjesti Username Admina u welcomeLabel
        for(int i = 0; i< listaAdmin.size(); i++){
            if(listaAdmin.get(i).getId().equals(adminId)){
                welcomeLabel.setText("Welcome " + listaAdmin.get(i).getUsername());
                break;
            }
        }

    }
    @FXML
    public void goBackAdmin() throws IOException {

        AdminLogInController.isAdminLoggedIn = false;
        FXMLLoader fxmlLoader = new FXMLLoader(ServisRacunala.class.getResource("adminLogIn.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        ServisRacunala.getMainStage().setTitle("PopIT");
        ServisRacunala.getMainStage().setScene(scene);
        ServisRacunala.getMainStage().show();

    }
    @FXML
    protected void adminUserEditPage() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(ServisRacunala.class.getResource("adminUserEdit.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        ServisRacunala.getMainStage().setTitle("User Edit");
        ServisRacunala.getMainStage().setScene(scene);
        ServisRacunala.getMainStage().show();
    }

    @FXML
    protected void adminAdminEditPage() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(ServisRacunala.class.getResource("adminAdminEdit.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        ServisRacunala.getMainStage().setTitle("Admin Edit");
        ServisRacunala.getMainStage().setScene(scene);
        ServisRacunala.getMainStage().show();
    }

    @FXML
    protected void adminProvjeraPromjenaPage() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(ServisRacunala.class.getResource("adminProvjeraPromjena.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 400);
        ServisRacunala.getMainStage().setTitle("Provjera Promjena");
        ServisRacunala.getMainStage().setScene(scene);
        ServisRacunala.getMainStage().show();
    }

    @FXML
    protected void adminPitanjaPage() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(ServisRacunala.class.getResource("adminPitanja.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 750, 900);
        ServisRacunala.getMainStage().setTitle("Pitanja");
        ServisRacunala.getMainStage().setScene(scene);
        ServisRacunala.getMainStage().show();
    }
    @FXML
    protected void adminNarudzbaPage() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(ServisRacunala.class.getResource("adminNarudzba.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        ServisRacunala.getMainStage().setTitle("Narudzbe");
        ServisRacunala.getMainStage().setScene(scene);
        ServisRacunala.getMainStage().show();
    }

    @FXML
    protected void adminPopravciPage() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(ServisRacunala.class.getResource("adminPopravci.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 600);
        ServisRacunala.getMainStage().setTitle("Popravci");
        ServisRacunala.getMainStage().setScene(scene);
        ServisRacunala.getMainStage().show();
    }

    @FXML
    protected void adminRadniNalogPage() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(ServisRacunala.class.getResource("adminRadniNalog.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 1000);
        ServisRacunala.getMainStage().setTitle("Radni Nalog");
        ServisRacunala.getMainStage().setScene(scene);
        ServisRacunala.getMainStage().show();
    }


}
