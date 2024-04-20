package hr.servis.kontroleri;

import hr.servis.baza.BazaPodataka;
import hr.servis.entiteti.PromjeneAdmin;
import hr.servis.entiteti.PromjeneUser;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.util.List;

public class AdminProvjeraPromjenaController {
    @FXML
    private TableView userTableView;
    @FXML
    private TableColumn<PromjeneUser, String> idUserTableColumn;
    @FXML
    private TableColumn<PromjeneUser, String> staraVrijednostUserTableColumn;
    @FXML
    private TableColumn<PromjeneUser, String> novaVrijednostUserTableColumn;
    @FXML
    private TableColumn<PromjeneUser, String> adminImeUserTableColumn;
    @FXML
    private TableColumn<PromjeneUser, String> datumIVrijemeUserTableColumn;

    @FXML
    private TableView adminTableView;
    @FXML
    private TableColumn<PromjeneAdmin, String> idAdminTableColumn;
    @FXML
    private TableColumn<PromjeneAdmin, String> staraVrijednostAdminTableColumn;
    @FXML
    private TableColumn<PromjeneAdmin, String> novaVrijednostAdminTableColumn;
    @FXML
    private TableColumn<PromjeneAdmin, String> adminImeAdminTableColumn;
    @FXML
    private TableColumn<PromjeneAdmin, String> datumIVrijemeAdminTableColumn;

    public List<PromjeneAdmin> adminList;
    public List<PromjeneUser> userList;
    public Integer id;

    public void initialize(){

        userList = BazaPodataka.dohvatiSvePromjeneUser();
        adminList = BazaPodataka.dohvatiSvePromjeneAdmin();

        idUserTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getId().toString()));

        staraVrijednostUserTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getStaraVrijednost()));

        novaVrijednostUserTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getNovaVrijednost()));

        adminImeUserTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getAdminIme()));

        datumIVrijemeUserTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getDatum_i_vrijeme().toString()));

        userTableView.setItems(FXCollections.observableList(userList));


        idAdminTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getId().toString()));

        staraVrijednostAdminTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getStaraVrijednost()));

        novaVrijednostAdminTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getNovaVrijednost()));

        adminImeAdminTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getAdminIme()));

        datumIVrijemeAdminTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getDatum_i_vrijeme().toString()));

        adminTableView.setItems(FXCollections.observableList(adminList));



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
