package hr.servis.kontroleri;

import hr.servis.baza.BazaPodataka;
import hr.servis.entiteti.Odgovor;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserOdgovoriController {

    @FXML
    private TableView odgovoriTableView;

    @FXML
    private TableColumn<Odgovor, String> subjectTableColumn;
    @FXML
    private TableColumn<Odgovor, String> pitanjeTableColumn;
    @FXML
    private TableColumn<Odgovor, String> odgovorTableColumn;

    @FXML
    private TextArea pitanjeTextArea;
    @FXML
    private TextArea odgovorTextArea;

    public List<Odgovor> odgovoriList = new ArrayList<>();
    public Integer idRow;

    public void initialize() {


        odgovoriList = BazaPodataka.dohvatiOdredeneOdgovore();

                subjectTableColumn
                        .setCellValueFactory(cellData ->
                                new SimpleStringProperty(cellData.getValue().subject()));

                pitanjeTableColumn
                        .setCellValueFactory(cellData ->
                                new SimpleStringProperty(cellData.getValue().pitanje()));

                odgovorTableColumn
                        .setCellValueFactory(cellData ->
                                new SimpleStringProperty(cellData.getValue().odgovor()));


                odgovoriTableView.setItems(FXCollections.observableList(odgovoriList));

        odgovoriTableView.setRowFactory(tv -> {
            TableRow<Odgovor> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Odgovor rowData = row.getItem();
                    idRow = rowData.id();

                    odgovorTextArea.setText(rowData.odgovor());
                    pitanjeTextArea.setText(rowData.pitanje());

                }
            });
            return row;
        });

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
