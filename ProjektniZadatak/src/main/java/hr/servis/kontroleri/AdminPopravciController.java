package hr.servis.kontroleri;

import hr.servis.baza.BazaPodataka;
import hr.servis.entiteti.Popravci;
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

public class AdminPopravciController {
    public static final Integer BROJ_ZAPISA_LOGIN = 5;
    private static final Logger logger = LoggerFactory.getLogger(AdminPitanjaController.class);
    @FXML
    private TextArea pitanjeTextArea;
    @FXML
    private TextArea odgovorTextArea;
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField tipPopravkaTextField;
    @FXML
    private TableView popravciTableView;
    @FXML
    private TableColumn<Popravci, String> tipPopravkaTableColumn;
    @FXML
    private TableColumn<Popravci, String> popravakTableColumn;
    @FXML
    private TableColumn<Popravci, String> userIdTableColumn;
    public List<Popravci> popravciList;
    public Integer idRow;

    public void initialize() {

        popravciList = BazaPodataka.dohvatiSvePopravke();

        userIdTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getUserId().toString()));

        tipPopravkaTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getTipPopravka().toString()));

        popravakTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getOpisPopravka().toString()));



        popravciTableView.setItems(FXCollections.observableList(popravciList));

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
            logger.error("Pogreška u čitanju User datoteke u AdminPopravciController-u!", e);
            System.err.println(e);
        }

        popravciTableView.setRowFactory(tv -> {
            TableRow<Popravci> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Popravci rowData = row.getItem();
                    idRow = (Integer) rowData.getUserId();
                    tipPopravkaTextField.setText(rowData.getTipPopravka().toString());

                    for (int i = 0; i < listaUser.size(); i++) {
                        if(idRow.equals(listaUser.get(i).getId())){
                            usernameTextField.setText(listaUser.get(i).getUsername());
                        }
                    }

                    pitanjeTextArea.setText(rowData.getOpisPopravka().toString());
                }
            });
            return row;
        });

    }

    @FXML
    public void posaljiOdgovor() throws BazaPodatakaException {

        for (int i = 0; i < popravciList.size(); i++) {
            if(idRow.equals(popravciList.get(i).getUserId())){
                BazaPodataka.posaljiPopravakOdgovor(idRow, tipPopravkaTextField.getText(), pitanjeTextArea.getText(), odgovorTextArea.getText(), popravciList.get(i));
            }
        }
        popravciList = BazaPodataka.dohvatiSvePopravke();

        userIdTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getUserId().toString()));

        tipPopravkaTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getTipPopravka().toString()));

        popravakTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getOpisPopravka().toString()));

        popravciTableView.setItems(FXCollections.observableList(popravciList));

        pitanjeTextArea.setText("");
        odgovorTextArea.setText("");
        tipPopravkaTextField.setText("");
        usernameTextField.setText("");
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
