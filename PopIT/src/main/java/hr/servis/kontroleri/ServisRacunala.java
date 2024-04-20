package hr.servis.kontroleri;

import hr.servis.niti.CsvNit;
import hr.servis.niti.ProvjeraPitanjaNit;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class ServisRacunala extends Application {

    private static Stage mainStage;

    @Override
    public void start(Stage stage) throws IOException {

        mainStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(ServisRacunala.class.getResource("glavniEkran.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setResizable(false);
        stage.setTitle("PopIT");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {

        Timeline threadovi = new Timeline(
                new KeyFrame(Duration.seconds(15), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        Platform.runLater(new ProvjeraPitanjaNit());
                    }
                }));
        /**/

        CsvNit target = new CsvNit();
        Thread csv = new Thread(target);
        threadovi.getKeyFrames().add(new KeyFrame(Duration.seconds(10), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Platform.runLater(csv);
            }
        }));

        threadovi.setCycleCount(Timeline.INDEFINITE);
        threadovi.play();
        launch();

    }

    public static Stage getMainStage(){
        return mainStage;
    }
}