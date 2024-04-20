package hr.servis.entiteti;


import hr.servis.baza.BazaPodataka;
import hr.servis.kontroleri.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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

public interface LoginInterface {

    Logger logger = LoggerFactory.getLogger(AdminMainPageController.class);
    Integer BROJ_ZAPISA_LOGIN = 5;
    Integer BROJ_ZAPISA_ADMIN = 3;


    default void dohvatiFile(TextField userTxtField, PasswordField pass) throws IOException {


        if(GlavniEkranController.isAdmin == true){
            String hashedPass = BazaPodataka.hashPass(pass.getText());
            logger.info("Dohvaćanje Admin datoteke...");
            List<Admin> listaAdmin = new ArrayList<>();

            String FILE_NAME = "dat/loginAdmin.txt";
            try (BufferedReader admin = new BufferedReader(new FileReader(FILE_NAME))) {

                List<String> datotekaAdmina = admin.lines().collect(Collectors.toList());
                logger.info("Dohvaćena datoteka: " + FILE_NAME);

                for (int i = 0; i < datotekaAdmina.size() / BROJ_ZAPISA_ADMIN; i++) {
                    String id = datotekaAdmina.get(i * BROJ_ZAPISA_ADMIN);
                    String username = datotekaAdmina.get(i * BROJ_ZAPISA_ADMIN +1);
                    String password = datotekaAdmina.get(i * BROJ_ZAPISA_ADMIN +2);


                    listaAdmin.add(new Admin(Integer.valueOf(id), username, password));
                }
                logger.info("Upisani admini u listu... ");
            } catch (IOException e) {
                logger.error("Pogreška u čitanju datoteke!", e);
                System.err.println(e);
            }
                boolean isFoundAdmin = false;
                for(int i = 0; i< listaAdmin.size(); i++){

                    if(userTxtField.getText().equals(listaAdmin.get(i).getUsername()) && hashedPass.equals(listaAdmin.get(i).getPassword())){
                        AdminMainPageController.adminId = listaAdmin.get(i).getId();
                        AdminLogInController.isAdminLoggedIn = true;
                        FXMLLoader fxmlLoader = new FXMLLoader(ServisRacunala.class.getResource("adminMainPage.fxml"));
                        Scene scene = new Scene(fxmlLoader.load(), 800, 825);
                        ServisRacunala.getMainStage().setTitle("PopIT");
                        ServisRacunala.getMainStage().setScene(scene);
                        ServisRacunala.getMainStage().show();
                        isFoundAdmin = true;
                        break;
                    }

                }
            if(isFoundAdmin == false){
                AdminLogInController.kriviLabelStatic.setText("Krivi username ili password!");
            }

            }
            else if(GlavniEkranController.isUser == true){
            String hashedPass = BazaPodataka.hashPass(pass.getText());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            List<User> listaUser = new ArrayList<>();
            String FILE_NAME = "dat/login.txt";
            try (BufferedReader user = new BufferedReader(new FileReader(FILE_NAME))) {

                List<String> datotekaUsera = user.lines().collect(Collectors.toList());


                for (int i = 0; i < datotekaUsera.size() / BROJ_ZAPISA_LOGIN; i++) {
                    String id = datotekaUsera.get(i * BROJ_ZAPISA_LOGIN);
                    String username = datotekaUsera.get(i * BROJ_ZAPISA_LOGIN +1);
                    String password = datotekaUsera.get(i * BROJ_ZAPISA_LOGIN +2);
                    Integer csv = Integer.valueOf(datotekaUsera.get(i * BROJ_ZAPISA_LOGIN +3));

                    String datumRodjenjaString = datotekaUsera.get(i * BROJ_ZAPISA_LOGIN +4);
                    LocalDate datumRodjenja = LocalDate.parse(datumRodjenjaString, formatter);

                    listaUser.add(new User(Integer.valueOf(id), username, password, csv, datumRodjenja));
                }
            } catch (IOException e) {
                logger.error("Pogreška u čitanju User datoteke u metodi dohvatiFile()!", e);
                System.err.println(e);
            }

            boolean isFoundUser = false;
            for(int i = 0; i< listaUser.size(); i++){
                if(userTxtField.getText().equals(listaUser.get(i).getUsername()) && hashedPass.equals(listaUser.get(i).getPassword())){
                    UserMainPageController.userId = listaUser.get(i).getId();
                    UserLogInController.isUserLoggedIn = true;
                    FXMLLoader fxmlLoader = new FXMLLoader(ServisRacunala.class.getResource("userMainPage.fxml"));
                    Scene scene = new Scene(fxmlLoader.load(), 800, 600);
                    ServisRacunala.getMainStage().setTitle("PopIT");
                    ServisRacunala.getMainStage().setScene(scene);
                    ServisRacunala.getMainStage().show();
                    isFoundUser = true;
                    break;
                }


            }
            if(isFoundUser == false){
                UserLogInController.kriviLabelStatic.setText("Krivi username ili password!");
            }

            }

        }

        default void goBack() throws IOException {
            GlavniEkranController.isUser = false;
            GlavniEkranController.isAdmin = false;
            FXMLLoader fxmlLoader = new FXMLLoader(ServisRacunala.class.getResource("glavniEkran.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            ServisRacunala.getMainStage().setTitle("PopIT");
            ServisRacunala.getMainStage().setScene(scene);
            ServisRacunala.getMainStage().show();
        }

    }


