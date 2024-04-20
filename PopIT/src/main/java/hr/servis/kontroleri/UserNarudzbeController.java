package hr.servis.kontroleri;

import hr.servis.baza.BazaPodataka;
import hr.servis.entiteti.Model;
import hr.servis.entiteti.Narudzbe;
import hr.servis.entiteti.Product;
import hr.servis.entiteti.User;
import hr.servis.iznimke.BazaPodatakaException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

import static hr.servis.kontroleri.UserMainPageController.userId;
public class UserNarudzbeController {

    private static final Logger logger = LoggerFactory.getLogger(UserNarudzbeController.class);
    private static final Integer BROJ_ZAPISA_LOGIN = 5;
    private static Product[] dataProduct = {new Product("AMD"), new Product("NVIDIA"),
            new Product("Igraca konzola")};

    private static Model[] dataModelAmd = {new Model("Ryzen 5", 5000), new Model("Ryzen 7", 5500),
            new Model("Ryzen 9", 4000)};

    private static Model[] dataModelNvidia = {new Model("GTX 1050", 1500), new Model("RTX 4080", 15000),
            new Model("GTX 1060 3GB", 1000)};

    private static Model[] dataModelKonzola = {new Model("PS4", 500), new Model("Xbox One", 550),
            new Model("Nintendo Switch", 400)};

    @FXML
    private ComboBox<Product> comboBoxProduct;
    @FXML
    private ComboBox<Model> comboBoxModel;
    @FXML
    private Label cijena;

    public static Product selectedProduct = null;
    public static Model selectedModel = null;

    public void initialize(){
        comboBoxProduct.getItems().addAll(dataProduct);
    }
    @FXML
    public void kupi() throws BazaPodatakaException {

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
            logger.error("Pogreška u čitanju User datoteke u UserNarudzbeController-u u kupi() metodi!", e);
            System.err.println(e);
        }


        TextInputDialog dialog = new TextInputDialog("");

        dialog.setTitle("Confirmation");
        dialog.setHeaderText("Upisite vas CSV");
        dialog.setContentText("CSV");
        dialog.showAndWait();

        TextField inputField = dialog.getEditor();

        if(inputField.getText().isEmpty()){
            Alert alertError = new Alert(Alert.AlertType.ERROR, "" ,ButtonType.OK);
            alertError.setHeaderText("Upisite CSV u polje!");
            alertError.showAndWait();
            if(alertError.getResult() == ButtonType.OK){
                kupi();
            }
        }
        else{
            Integer csvInput = Integer.valueOf(inputField.getText());

            boolean nasaoCSV = false;
            for (int i = 0; i < listaUser.size(); i++) {

                if(csvInput.equals(listaUser.get(i).getCSV())){
                    Narudzbe narudzba = new Narudzbe<>(userId, selectedProduct, selectedModel, "Naruceno");
                    BazaPodataka.posaljiNarudzbu(narudzba);
                    nasaoCSV = true;
                }
            }

            if(nasaoCSV == false){
                Alert alertError = new Alert(Alert.AlertType.ERROR, "" ,ButtonType.OK, ButtonType.CANCEL);
                alertError.setHeaderText("Krivi CSV!");
                alertError.showAndWait();
                if(alertError.getResult() == ButtonType.OK){
                    kupi();
                }
                if(alertError.getResult() == ButtonType.CANCEL){
                    alertError.close();
                }
            }

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

    public void dohvatiModele(){
        selectedProduct = comboBoxProduct.getSelectionModel().getSelectedItem();
        comboBoxModel.getItems().removeAll();

        if(selectedProduct.getProductName().equals("AMD")){
            comboBoxModel.getItems().removeAll(dataModelAmd);
            comboBoxModel.getItems().removeAll(dataModelNvidia);
            comboBoxModel.getItems().removeAll(dataModelKonzola);
            comboBoxModel.getItems().addAll(dataModelAmd);
        }
        else if(selectedProduct.getProductName().equals("NVIDIA")){
            comboBoxModel.getItems().removeAll(dataModelAmd);
            comboBoxModel.getItems().removeAll(dataModelNvidia);
            comboBoxModel.getItems().removeAll(dataModelKonzola);
            comboBoxModel.getItems().addAll(dataModelNvidia);
        }
        else if(selectedProduct.getProductName().equals("Igraca konzola")){
            comboBoxModel.getItems().removeAll(dataModelAmd);
            comboBoxModel.getItems().removeAll(dataModelNvidia);
            comboBoxModel.getItems().removeAll(dataModelKonzola);
            comboBoxModel.getItems().addAll(dataModelKonzola);
        }
    }

    public void dohvatiCijene(){
        selectedModel = comboBoxModel.getSelectionModel().getSelectedItem();
        cijena.setText("Cijena: " + selectedModel.getCijena() + " eura");
    }
}
