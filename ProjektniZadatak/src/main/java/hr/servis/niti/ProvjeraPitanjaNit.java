package hr.servis.niti;

import hr.servis.baza.BazaPodataka;
import hr.servis.entiteti.Pitanja;
import hr.servis.kontroleri.AdminLogInController;
import hr.servis.kontroleri.AdminMainPageController;
import javafx.scene.control.Alert;

import java.util.List;

public class ProvjeraPitanjaNit implements Runnable{

    @Override
    public void run() {
        List<Pitanja> brojPitanja = BazaPodataka.dohvatiSvaPitanja();

        if (brojPitanja.size()!=0 && AdminLogInController.isAdminLoggedIn == true){
            AdminMainPageController.brojStaticLabel.setText(String.valueOf(brojPitanja.size()));
            Alert info = new Alert(Alert.AlertType.INFORMATION);
            info.setTitle("Obavijest");
            if(brojPitanja.size() == 1){
                info.setHeaderText("Imate " + brojPitanja.size() + " pitanje za odgovoriti.");
            }
            else{
                info.setHeaderText("Imate " + brojPitanja.size() + " pitanja za odgovoriti.");
            }
            info.showAndWait();

        }
    }
}
