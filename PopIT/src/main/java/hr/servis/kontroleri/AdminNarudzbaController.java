package hr.servis.kontroleri;

import hr.servis.baza.BazaPodataka;
import hr.servis.entiteti.Narudzbe;
import hr.servis.entiteti.User;
import hr.servis.iznimke.BazaPodatakaException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
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
import java.util.*;
import java.util.stream.Collectors;

public class AdminNarudzbaController {
    public static final Integer BROJ_ZAPISA_LOGIN = 5;
    private static final Logger logger = LoggerFactory.getLogger(AdminNarudzbaController.class);
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField productTextField;
    @FXML
    private TextField modelTextField;

    @FXML
    private TableView narudzbeTableView;

    @FXML
    private TableColumn<Narudzbe, String> userIdTableColumn;
    @FXML
    private TableColumn<Narudzbe, String> productTableColumn;
    @FXML
    private TableColumn<Narudzbe, String> modelTableColumn;
    @FXML
    private TableColumn<Narudzbe, String> stanjeTableColumn;

    public static List<Integer> narudzbeIdList = new ArrayList<>();
    public List<Narudzbe> narudzbeList;
    public Integer idRow;
    public int rowNum;

    public void initialize() {

        narudzbeList = BazaPodataka.dohvatiSveNarudzbe();

        userIdTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getUserId().toString()));


        productTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getProduct().toString()));

        modelTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getModel().toString()));

        stanjeTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getTrenutnoStanje().toString()));


        narudzbeTableView.setItems(FXCollections.observableList(narudzbeList));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<User> listaUser = new ArrayList<>();
        String FILE_NAME = "dat/login.txt";
        try (BufferedReader user = new BufferedReader(new FileReader(FILE_NAME))) {

            List<String> datotekaUsera = user.lines().collect(Collectors.toList());


            for (int i = 0; i < datotekaUsera.size() / BROJ_ZAPISA_LOGIN; i++) {
                String id = datotekaUsera.get(i * BROJ_ZAPISA_LOGIN);
                String usernameDatoteka = datotekaUsera.get(i * BROJ_ZAPISA_LOGIN +1);
                String passwordDatoteka = datotekaUsera.get(i * BROJ_ZAPISA_LOGIN +2);
                Integer csvDatoteka = Integer.valueOf(datotekaUsera.get(i * BROJ_ZAPISA_LOGIN +3));

                String datumRodjenjaString = datotekaUsera.get(i * BROJ_ZAPISA_LOGIN +4);
                LocalDate datumRodjenja = LocalDate.parse(datumRodjenjaString, formatter);

                listaUser.add(new User(Integer.valueOf(id), usernameDatoteka, passwordDatoteka, csvDatoteka, datumRodjenja));
            }
        } catch (IOException e) {
            logger.error("Pogreška u čitanju User datoteke u AdminNarudzbaControlleru!", e);
            System.err.println(e);
        }

        narudzbeTableView.setRowFactory(tv -> {
            TableRow<Narudzbe> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    rowNum = narudzbeTableView.getSelectionModel().getSelectedIndex();
                    Narudzbe rowData = row.getItem();
                    idRow = (Integer) rowData.getUserId();
                    productTextField.setText(rowData.getProduct().toString());
                    for (int i = 0; i < listaUser.size(); i++) {
                        if(idRow.equals(listaUser.get(i).getId())){
                            usernameTextField.setText(listaUser.get(i).getUsername());
                        }
                    }
                    modelTextField.setText(rowData.getModel().toString());
                }
            });
            return row;
        });

    }

    @FXML
    public void kupiProduct() throws BazaPodatakaException {

        narudzbeList = BazaPodataka.dohvatiSveNarudzbe();
        narudzbeList.get(rowNum).setTrenutnoStanje("Kupljeno");
        BazaPodataka.kupiProduct(narudzbeIdList.get(rowNum), narudzbeList.get(rowNum));

        narudzbeList = BazaPodataka.dohvatiSveNarudzbe();
        userIdTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getUserId().toString()));


        productTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getProduct().toString()));

        modelTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getModel().toString()));

        stanjeTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getTrenutnoStanje().toString()));


        narudzbeTableView.setItems(FXCollections.observableList(narudzbeList));

        productTextField.setText("");
        usernameTextField.setText("");
        modelTextField.setText("");
    }

    @FXML
    public void goBackAdmin() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(ServisRacunala.class.getResource("adminMainPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 825);
        ServisRacunala.getMainStage().setTitle("PopIT");
        ServisRacunala.getMainStage().setScene(scene);
        ServisRacunala.getMainStage().show();

    }


}
