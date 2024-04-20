package hr.servis.kontroleri;

import hr.servis.baza.BazaPodataka;
import hr.servis.entiteti.*;
import hr.servis.iznimke.BazaPodatakaException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static hr.servis.kontroleri.UserMainPageController.userId;
public class UserPopravciController {
    private static PopravciTip[] dataPopravci = {new PopravciTip("PC"), new PopravciTip("Mobitel"),
            new PopravciTip("Konzola")};
    @FXML
    private ComboBox<PopravciTip> comboBoxPopravci;

    @FXML
    private TextField tipTextField;
    @FXML
    private TextArea popravciTextArea;

    @FXML
    private TextArea popravciOpisTextArea;

    @FXML
    private TextArea popravciOdgovorTextArea;

    @FXML
    private TableView popravciTableView;

    @FXML
    private TableColumn<PopravciOdgovor, String> tipPopravkaTableColumn;
    @FXML
    private TableColumn<PopravciOdgovor, String> opisKvaraTableColumn;
    @FXML
    private TableColumn<PopravciOdgovor, String> odgovorTableColumn;
    public List<PopravciOdgovor> popravciList = new ArrayList<>();
    public static PopravciTip selectedPopravak = null;

    public void initialize(){
        comboBoxPopravci.getItems().addAll(dataPopravci);


        popravciList = BazaPodataka.dohvatiOdredenePopravke();

        tipPopravkaTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().tipkvara()));

        opisKvaraTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().opiskvara()));

        odgovorTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().odgovor()));


        popravciTableView.setItems(FXCollections.observableList(popravciList));

        popravciTableView.setRowFactory(tv -> {
            TableRow<PopravciOdgovor> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    PopravciOdgovor rowData = row.getItem();
                    Integer idRow = rowData.userId();
                    System.out.println(idRow);
                    tipTextField.setText(rowData.tipkvara());
                    popravciOdgovorTextArea.setText(rowData.odgovor());
                    popravciOpisTextArea.setText(rowData.opiskvara());
                    System.out.println(rowData);
                }
            });
            return row;
        });

    }
    @FXML
    public void posaljiPopravak() throws BazaPodatakaException {

        Popravci popravak = new Popravci(userId, selectedPopravak, popravciTextArea.getText());
        BazaPodataka.posaljiPopravak(popravak);
    }

    @FXML
    public void dohvatiTip(){
        selectedPopravak = comboBoxPopravci.getSelectionModel().getSelectedItem();
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
