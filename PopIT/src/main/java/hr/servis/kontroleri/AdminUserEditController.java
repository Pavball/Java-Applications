package hr.servis.kontroleri;

import hr.servis.baza.BazaPodataka;
import hr.servis.entiteti.User;
import hr.servis.iznimke.BazaPodatakaException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class AdminUserEditController {

    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private DatePicker datumRodjenjaUseraDatePicker;

    @FXML
    private TableView userTableView;

    @FXML
    private TableColumn<User, String> idTableColumn;
    @FXML
    private TableColumn<User, String> usernameTableColumn;
    @FXML
    private TableColumn<User, String> passwordTableColumn;

    @FXML
    private TableColumn<User, String> csvTableColumn;
    @FXML
    private TableColumn<User, String> datumUseraTableColumn;
    public List<User> userList;
    public Integer id;

    public void initialize(){

        userList = BazaPodataka.dohvatiSveUsere();

        idTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getId().toString()));


        usernameTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getUsername()));

        passwordTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getPassword()));

        csvTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getCSV().toString()));

        datumUseraTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getDatumRodjenja().toString()));

        userTableView.setItems(FXCollections.observableList(userList));

        userTableView.setRowFactory( tv -> {
            TableRow<User> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    User rowData = row.getItem();
                    id = rowData.getId();

                    usernameTextField.setText(rowData.getUsername());
                    passwordTextField.setText(rowData.getPassword());
                    datumRodjenjaUseraDatePicker.setValue(rowData.getDatumRodjenja());

                }
            });
            return row ;
        });

    }

    public void dohvatiUsera(){

        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        LocalDate datumRodjenja = datumRodjenjaUseraDatePicker.getValue();

        List<User> filtriraniStudenti = userList.stream()
                .filter(s -> s.getUsername().contains(username))
                .filter(s -> s.getPassword().contains(password))
                .filter(s -> s.getDatumRodjenja().equals(datumRodjenja))
                .toList();

        userTableView.setItems(FXCollections.observableList(filtriraniStudenti));
    }

    public void dodajUsera() throws Exception {

        Random random = new Random();
        Integer csv = random.nextInt(1000 - 0) + 0;

        if (usernameTextField.getText().isEmpty() || passwordTextField.getText().isEmpty() || datumRodjenjaUseraDatePicker.getValue().equals(null)) {
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
            if (datumRodjenjaUseraDatePicker.getValue().equals(null)) {
                Label label2 = new Label("Molimo, upisite DATUM RODJENJA!");

                expContent.add(label2, 0, 2);

            }

            alert.getDialogPane().setContent(expContent);
            alert.showAndWait();
        } else {

            BazaPodataka.unesiUsera(usernameTextField.getText(), passwordTextField.getText(), csv, datumRodjenjaUseraDatePicker.getValue());
            userList = BazaPodataka.dohvatiSveUsere();
            idTableColumn
                    .setCellValueFactory(cellData ->
                            new SimpleStringProperty(cellData.getValue().getId().toString()));

            usernameTableColumn
                    .setCellValueFactory(cellData ->
                            new SimpleStringProperty(cellData.getValue().getUsername()));

            passwordTableColumn
                    .setCellValueFactory(cellData ->
                            new SimpleStringProperty(cellData.getValue().getPassword()));

            csvTableColumn
                    .setCellValueFactory(cellData ->
                            new SimpleStringProperty(cellData.getValue().getCSV().toString()));

            datumUseraTableColumn
                    .setCellValueFactory(cellData ->
                            new SimpleStringProperty(cellData.getValue().getDatumRodjenja().toString()));

            userTableView.setItems(FXCollections.observableList(userList));

        }
    }

    public void azurirajUser() throws BazaPodatakaException {

        if (usernameTextField.getText().isEmpty() || passwordTextField.getText().isEmpty() || datumRodjenjaUseraDatePicker.getValue().equals(null)) {
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
            if (datumRodjenjaUseraDatePicker.getValue().equals(null)) {
                Label label2 = new Label("Molimo, upisite DATUM RODJENJA!");

                expContent.add(label2, 0, 2);

            }

            alert.getDialogPane().setContent(expContent);
            alert.showAndWait();
        } else {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Ažuriraj " + usernameTextField.getText() + " ?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES) {

                BazaPodataka.azurirajUsera(id, usernameTextField.getText(), passwordTextField.getText(), datumRodjenjaUseraDatePicker.getValue());
                userList = BazaPodataka.dohvatiSveUsere();

                idTableColumn
                        .setCellValueFactory(cellData ->
                                new SimpleStringProperty(cellData.getValue().getId().toString()));

                usernameTableColumn
                        .setCellValueFactory(cellData ->
                                new SimpleStringProperty(cellData.getValue().getUsername()));

                passwordTableColumn
                        .setCellValueFactory(cellData ->
                                new SimpleStringProperty(cellData.getValue().getPassword()));

                csvTableColumn
                        .setCellValueFactory(cellData ->
                                new SimpleStringProperty(cellData.getValue().getCSV().toString()));

                datumUseraTableColumn
                        .setCellValueFactory(cellData ->
                                new SimpleStringProperty(cellData.getValue().getDatumRodjenja().toString()));

                userTableView.setItems(FXCollections.observableList(userList));
            }
        }
    }
    public void izbrisiUser() throws BazaPodatakaException {

        if (usernameTextField.getText().isEmpty() || passwordTextField.getText().isEmpty() || datumRodjenjaUseraDatePicker.getValue().equals(null)) {
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
            if (datumRodjenjaUseraDatePicker.getValue().equals(null)) {
                Label label2 = new Label("Molimo, upisite DATUM RODJENJA!");

                expContent.add(label2, 0, 2);

            }

            alert.getDialogPane().setContent(expContent);
            alert.showAndWait();
        } else {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Izbriši " + usernameTextField.getText() + " ?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES) {
                BazaPodataka.izbrisiUsera(id, usernameTextField.getText(), passwordTextField.getText());
                userList = BazaPodataka.dohvatiSveUsere();

                idTableColumn
                        .setCellValueFactory(cellData ->
                                new SimpleStringProperty(cellData.getValue().getId().toString()));

                usernameTableColumn
                        .setCellValueFactory(cellData ->
                                new SimpleStringProperty(cellData.getValue().getUsername()));

                passwordTableColumn
                        .setCellValueFactory(cellData ->
                                new SimpleStringProperty(cellData.getValue().getPassword()));

                csvTableColumn
                        .setCellValueFactory(cellData ->
                                new SimpleStringProperty(cellData.getValue().getCSV().toString()));

                datumUseraTableColumn
                        .setCellValueFactory(cellData ->
                                new SimpleStringProperty(cellData.getValue().getDatumRodjenja().toString()));

                userTableView.setItems(FXCollections.observableList(userList));
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