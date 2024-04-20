package hr.servis.kontroleri;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import hr.servis.baza.BazaPodataka;
import hr.servis.entiteti.Admin;
import hr.servis.entiteti.Narudzbe;
import hr.servis.entiteti.Popravci;
import hr.servis.entiteti.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import static hr.servis.kontroleri.AdminMainPageController.adminId;
public class AdminRadniNalogController {
    public static final Integer BROJ_ZAPISA_ADMIN = 3;
    public static final Integer BROJ_ZAPISA_LOGIN = 5;
    private static final Logger logger = LoggerFactory.getLogger(AdminRadniNalogController.class);

    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField usernameKvarTextField;
    @FXML
    private TextField productTextField;
    @FXML
    private TextField modelTextField;
    @FXML
    private Label cijenaLabel;
    @FXML
    private TextField tipPopravkaTextField;
    @FXML
    private TextArea opisTextArea;
    @FXML
    private TextArea odgovorTextArea;
    @FXML
    private TableView narudzbaTableView;
    @FXML
    private TableView popravakTableView;

    @FXML
    private TableColumn<Narudzbe, String> userIdNarudzbeTableColumn;
    @FXML
    private TableColumn<Narudzbe, String> productNarudzbeTableColumn;
    @FXML
    private TableColumn<Narudzbe, String> modelNarudzbeTableColumn;
    @FXML
    private TableColumn<Narudzbe, String> stanjeNarudzbeTableColumn;

    @FXML
    private TableColumn<Popravci, String> userIdPopravciTableColumn;
    @FXML
    private TableColumn<Popravci, String> tipPopravciTableColumn;
    @FXML
    private TableColumn<Popravci, String> opisKvaraTableColumn;
    public List<Narudzbe> narudzbeList;
    public List<Popravci> popravciList;
    public Integer idRow;
    public int rowNum;
    public boolean isNarudzbaSelected = false;
    public boolean isPopravakSelected = false;


    public void initialize(){
        System.out.println(adminId);
        narudzbeList = BazaPodataka.dohvatiSveNarudzbe();

        userIdNarudzbeTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getUserId().toString()));


        productNarudzbeTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getProduct().toString()));

        modelNarudzbeTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getModel().toString()));

        stanjeNarudzbeTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getTrenutnoStanje().toString()));


        narudzbaTableView.setItems(FXCollections.observableList(narudzbeList));

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
            logger.error("Pogreška u čitanju User datoteke u AdminRadniNalogController-u u initialize() metodi!", e);
            System.err.println(e);
        }

        narudzbaTableView.setRowFactory(tv -> {
            TableRow<Narudzbe> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    rowNum = narudzbaTableView.getSelectionModel().getSelectedIndex();
                    Narudzbe rowData = row.getItem();
                    idRow = (Integer) rowData.getUserId();
                    productTextField.setText(rowData.getProduct().toString());

                    for (int i = 0; i < listaUser.size(); i++) {
                        if(idRow.equals(listaUser.get(i).getId())){
                            usernameTextField.setText(listaUser.get(i).getUsername());
                        }
                    }

                    modelTextField.setText(rowData.getModel().toString());
                    cijenaLabel.setText(narudzbeList.get(rowNum).getModel().toString());

                    isNarudzbaSelected = true;
                    isPopravakSelected = false;

                    usernameKvarTextField.setText("");
                    tipPopravkaTextField.setText("");
                    opisTextArea.setText("");
                    odgovorTextArea.setText("");

                }
            });
            return row;
        });


        popravciList = BazaPodataka.dohvatiSvePopravke();


        userIdPopravciTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getUserId().toString()));

        tipPopravciTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getTipPopravka().toString()));

        opisKvaraTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getOpisPopravka().toString()));



        popravakTableView.setItems(FXCollections.observableList(popravciList));


        popravakTableView.setRowFactory(tv -> {
            TableRow<Popravci> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Popravci rowData = row.getItem();
                    idRow = (Integer) rowData.getUserId();

                    tipPopravkaTextField.setText(rowData.getTipPopravka().toString());
                    for (int i = 0; i < listaUser.size(); i++) {
                        if(idRow.equals(listaUser.get(i).getId())){
                            usernameKvarTextField.setText(listaUser.get(i).getUsername());
                        }
                    }
                    opisTextArea.setText(rowData.getOpisPopravka().toString());

                    isNarudzbaSelected = false;
                    isPopravakSelected = true;

                    usernameTextField.setText("");
                    productTextField.setText("");
                    modelTextField.setText("");
                    cijenaLabel.setText("Cijena");


                }
            });
            return row;
        });

    }

    public void napraviNalog(){

        if(isNarudzbaSelected == false && isPopravakSelected == false){
            Alert info = new Alert(Alert.AlertType.ERROR);
            info.setTitle("Upozorenje");
            info.setHeaderText("Molimo odaberite redak u jednoj od tablica kako bi napravili nalog.");
            info.showAndWait();
        }

        if(isNarudzbaSelected == true){
            napraviNarudzbaPdf();
        }

        if(isPopravakSelected == true && odgovorTextArea.getText().isEmpty() == false){
            napraviPopravakPdf();
        }
        else{
            Alert info = new Alert(Alert.AlertType.ERROR);
            info.setTitle("Upozorenje");
            info.setHeaderText("Molimo ispunite odgovor za nalog.");
            info.showAndWait();
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

    public void napraviNarudzbaPdf(){

        //Dohvacanje Admin datoteke u listu
        List<Admin> listaAdmin = new ArrayList<>();
        String FILE_NAME = "dat/loginAdmin.txt";
        try (BufferedReader admin = new BufferedReader(new FileReader(FILE_NAME))) {

            List<String> datotekaAdmina = admin.lines().collect(Collectors.toList());

            for (int i = 0; i < datotekaAdmina.size() / BROJ_ZAPISA_ADMIN; i++) {
                String id = datotekaAdmina.get(i * BROJ_ZAPISA_ADMIN);
                String username = datotekaAdmina.get(i * BROJ_ZAPISA_ADMIN +1);
                String password = datotekaAdmina.get(i * BROJ_ZAPISA_ADMIN +2);


                listaAdmin.add(new Admin(Integer.valueOf(id), username, password));
            }
        } catch (IOException e) {
            logger.error("Pogreška u čitanju Admin datoteke u AdminRadniNalogController-u u napraviNarudzbaPdf() metodi!", e);
            System.err.println(e);
        }

        Document document = new Document();
        try
        {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("dat/RadniNalog.pdf"));
            document.open();

            Font titleFont =new Font(Font.FontFamily.TIMES_ROMAN,50.0f,Font.BOLD,BaseColor.CYAN);
            Paragraph par = new Paragraph("PopIT", titleFont);
            par.setAlignment(Element.ALIGN_CENTER);
            document.add(par);

            PdfPTable table = new PdfPTable(2);
            table.addCell("Kupac");
            table.addCell(usernameTextField.getText());
            table.addCell("Blagajnik");
            for (int i = 0; i < listaAdmin.size(); i++) {
                if(listaAdmin.get(i).getId().equals(adminId)){
                    table.addCell(listaAdmin.get(i).getUsername());
                }
            }
            table.addCell("Product");
            table.addCell(productTextField.getText());
            Font priceFont =new Font(Font.FontFamily.TIMES_ROMAN,25.0f,Font.BOLD,BaseColor.RED);
            PdfPCell cell;
            cell = new PdfPCell(new Phrase(modelTextField.getText(), priceFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(2);
            table.addCell(cell);
            table.setSpacingBefore(50);
            document.add(table);

            Font endingFont =new Font(Font.FontFamily.TIMES_ROMAN,17.5f,Font.BOLD,BaseColor.BLACK);
            Paragraph endingPar = new Paragraph("U Zagrebu, " + LocalDate.now(), endingFont);
            endingPar.setAlignment(Element.ALIGN_LEFT);
            document.add(endingPar);


            document.close();
            writer.close();
            System.out.println("Radni nalog napravljen!");
        } catch (DocumentException e) {
            e.printStackTrace();
            logger.error("Pogreška u vezi radnog naloga za popravak u AdminRadniNalogController-u u napraviNarudzbaPdf() metodi.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.error("PDF nije naden u AdminRadniNalogController-u u metodi napraviNarudzbaPdf() metodi.");
        }
    }

    public void napraviPopravakPdf(){

        //Dohvacanje Admin datoteke u listu
        List<Admin> listaAdmin = new ArrayList<>();
        String FILE_NAME = "dat/loginAdmin.txt";
        try (BufferedReader admin = new BufferedReader(new FileReader(FILE_NAME))) {

            List<String> datotekaAdmina = admin.lines().collect(Collectors.toList());

            for (int i = 0; i < datotekaAdmina.size() / BROJ_ZAPISA_ADMIN; i++) {
                String id = datotekaAdmina.get(i * BROJ_ZAPISA_ADMIN);
                String username = datotekaAdmina.get(i * BROJ_ZAPISA_ADMIN +1);
                String password = datotekaAdmina.get(i * BROJ_ZAPISA_ADMIN +2);

                listaAdmin.add(new Admin(Integer.valueOf(id), username, password));
            }
        } catch (IOException e) {
            logger.error("Pogreška u čitanju Admin datoteke u AdminRadniNalogController-u u napraviPopravakPdf() metodi!", e);
            System.err.println(e);
        }

        Document document = new Document();
        try
        {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("dat/RadniNalogPopravak.pdf"));
            document.open();

            Font titleFont =new Font(Font.FontFamily.TIMES_ROMAN,50.0f,Font.BOLD,BaseColor.CYAN);
            Paragraph par = new Paragraph("PopIT", titleFont);
            par.setAlignment(Element.ALIGN_CENTER);
            document.add(par);

            PdfPTable table = new PdfPTable(2);
            table.addCell("Kupac");
            table.addCell(usernameKvarTextField.getText());
            table.addCell("Serviser");

            for (int i = 0; i < listaAdmin.size(); i++) {
                if(listaAdmin.get(i).getId().equals(adminId)){
                    table.addCell(listaAdmin.get(i).getUsername());
                }
            }

            table.addCell("Tip kvara");
            table.addCell(tipPopravkaTextField.getText());

            Font priceFont =new Font(Font.FontFamily.TIMES_ROMAN,25.0f,Font.BOLD,BaseColor.RED);
            PdfPCell cell;
            cell = new PdfPCell(new Phrase("Opis kvara", priceFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(2);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(opisTextArea.getText()));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(2);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Odgovor", priceFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(2);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(odgovorTextArea.getText()));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(2);
            table.addCell(cell);

            table.setSpacingBefore(50);
            document.add(table);

            Font endingFont =new Font(Font.FontFamily.TIMES_ROMAN,17.5f,Font.BOLD,BaseColor.BLACK);
            Paragraph endingPar = new Paragraph("U Zagrebu, " + LocalDate.now(), endingFont);
            endingPar.setAlignment(Element.ALIGN_LEFT);
            document.add(endingPar);

            document.close();
            writer.close();
            System.out.println("Radni nalog za popravak napravljen!");
        } catch (DocumentException e) {
            e.printStackTrace();
            logger.error("Pogreška u vezi radnog naloga za popravak u AdminRadniNalogController-u u napraviPopravakPdf() metodi.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.error("PDF nije naden u AdminRadniNalogController-u u napraviPopravakPdf() metodi.");
        }
    }





}
