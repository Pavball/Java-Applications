package hr.servis.niti;

import hr.servis.entiteti.User;

import hr.servis.kontroleri.UserLogInController;
import javafx.scene.control.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import static hr.servis.kontroleri.UserMainPageController.userId;
public class CsvNit implements Runnable{

    Logger logger = LoggerFactory.getLogger(CsvNit.class);
    Integer BROJ_ZAPISA_LOGIN = 5;
    private static AtomicInteger atomicInteger = new AtomicInteger(0);



    @Override
    public void run() {

            System.out.println("Csv nit radi");

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
                logger.error("Pogreška u čitanju User datoteke u metodi run() u klasi CsvNit()!", e);
                System.err.println(e);
            }

            if(atomicInteger.get() == 0 && UserLogInController.isUserLoggedIn == true){
                Alert info = new Alert(Alert.AlertType.INFORMATION);
                info.setTitle("Obavijest za CSV");
                for (int i = 0; i < listaUser.size(); i++) {
                    if(listaUser.get(i).getId().equals(userId)){
                        info.setHeaderText("Vas CSV je: "+ listaUser.get(i).getCSV());
                    }
                }
                info.showAndWait();

            }

            atomicInteger.getAndIncrement();

    }



}
