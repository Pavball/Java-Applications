package hr.servis.kontroleri;

import hr.servis.baza.BazaPodataka;
import hr.servis.entiteti.Admin;
import hr.servis.iznimke.BazaPodatakaException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.*;

public class AdminAdminEditController {

    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField passwordTextField;

    @FXML
    private TableView adminTableView;

    @FXML
    private TableColumn<Admin, String> idTableColumn;
    @FXML
    private TableColumn<Admin, String> usernameTableColumn;
    @FXML
    private TableColumn<Admin, String> passwordTableColumn;

    public List<Admin> adminList;
    public Integer id;

    public void initialize(){

        adminList = BazaPodataka.dohvatiSveAdmine();

        idTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getId().toString()));


        usernameTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getUsername()));

        passwordTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getPassword()));



        adminTableView.setItems(FXCollections.observableList(adminList));

        adminTableView.setRowFactory( tv -> {
            TableRow<Admin> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Admin rowData = row.getItem();
                    id = rowData.getId();
                    usernameTextField.setText(rowData.getUsername());
                    passwordTextField.setText(rowData.getPassword());
                    System.out.println(rowData);
                }
            });
            return row ;
        });

    }

    public void dohvatiAdmina(){

        String username = usernameTextField.getText();
        String password = passwordTextField.getText();

        List<Admin> filtriraniStudenti = adminList.stream()
                .filter(s -> s.getUsername().contains(username))
                .filter(s -> s.getPassword().contains(password))
                .toList();

        adminTableView.setItems(FXCollections.observableList(filtriraniStudenti));
    }

    public void dodajAdmina() throws Exception {


        if (usernameTextField.getText().isEmpty() || passwordTextField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Pogreska");
            alert.setHeaderText("Ispravite sljedece greske:");
            GridPane expContent = new GridPane();
            expContent.setMaxWidth(Double.MAX_VALUE);
            if (usernameTextField.getText().isEmpty()) {
                Label label = new Label("Molimo, upisite USERNAME!");

                expContent.add(label, 0, 0);

            }
            if (passwordTextField.getText().isEmpty()) {
                Label label1 = new Label("Molimo, upisite PASSWORD!");

                expContent.add(label1, 0, 1);

            }

            alert.getDialogPane().setContent(expContent);
            alert.showAndWait();
        } else {

            BazaPodataka.unesiAdmina(usernameTextField.getText(), passwordTextField.getText());
            adminList = BazaPodataka.dohvatiSveAdmine();
            idTableColumn
                    .setCellValueFactory(cellData ->
                            new SimpleStringProperty(cellData.getValue().getId().toString()));

            usernameTableColumn
                    .setCellValueFactory(cellData ->
                            new SimpleStringProperty(cellData.getValue().getUsername()));

            passwordTableColumn
                    .setCellValueFactory(cellData ->
                            new SimpleStringProperty(cellData.getValue().getPassword()));


            adminTableView.setItems(FXCollections.observableList(adminList));

        }
    }

    public void azurirajAdmina() throws BazaPodatakaException {

        if (usernameTextField.getText().isEmpty() || passwordTextField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Pogreska");
            alert.setHeaderText("Ispravite sljedece greske:");
            GridPane expContent = new GridPane();
            expContent.setMaxWidth(Double.MAX_VALUE);
            if (usernameTextField.getText().isEmpty()) {
                Label label = new Label("Molimo, upisite USERNAME!");

                expContent.add(label, 0, 0);

            }
            if (passwordTextField.getText().isEmpty()) {
                Label label1 = new Label("Molimo, upisite PASSWORD!");

                expContent.add(label1, 0, 1);

            }

            alert.getDialogPane().setContent(expContent);
            alert.showAndWait();
        } else {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Update " + usernameTextField.getText() + " ?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES) {

                BazaPodataka.azurirajAdmina(id, usernameTextField.getText(), passwordTextField.getText());
                adminList = BazaPodataka.dohvatiSveAdmine();

                idTableColumn
                        .setCellValueFactory(cellData ->
                                new SimpleStringProperty(cellData.getValue().getId().toString()));

                usernameTableColumn
                        .setCellValueFactory(cellData ->
                                new SimpleStringProperty(cellData.getValue().getUsername()));

                passwordTableColumn
                        .setCellValueFactory(cellData ->
                                new SimpleStringProperty(cellData.getValue().getPassword()));


                adminTableView.setItems(FXCollections.observableList(adminList));
            }
        }
    }
    public void izbrisiAdmina() throws BazaPodatakaException {

        if (usernameTextField.getText().isEmpty() || passwordTextField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Pogreska");
            alert.setHeaderText("Ispravite sljedece greske:");
            GridPane expContent = new GridPane();
            expContent.setMaxWidth(Double.MAX_VALUE);
            if (usernameTextField.getText().isEmpty()) {
                Label label = new Label("Molimo, upisite USERNAME!");

                expContent.add(label, 0, 0);

            }
            if (passwordTextField.getText().isEmpty()) {
                Label label1 = new Label("Molimo, upisite PASSWORD!");

                expContent.add(label1, 0, 1);

            }

            alert.getDialogPane().setContent(expContent);
            alert.showAndWait();
        } else {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete " + usernameTextField.getText() + " ?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES) {
                BazaPodataka.izbrisiAdmina(id, usernameTextField.getText(), passwordTextField.getText());
                adminList = BazaPodataka.dohvatiSveAdmine();

                idTableColumn
                        .setCellValueFactory(cellData ->
                                new SimpleStringProperty(cellData.getValue().getId().toString()));

                usernameTableColumn
                        .setCellValueFactory(cellData ->
                                new SimpleStringProperty(cellData.getValue().getUsername()));

                passwordTableColumn
                        .setCellValueFactory(cellData ->
                                new SimpleStringProperty(cellData.getValue().getPassword()));

                adminTableView.setItems(FXCollections.observableList(adminList));
            }
        }
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