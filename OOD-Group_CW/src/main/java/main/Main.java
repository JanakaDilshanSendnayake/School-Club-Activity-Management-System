package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import stake_holders.Clubs;


import java.util.ArrayList;

public class Main extends Application {

    public static ArrayList<Clubs> clubs;
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml_files/ClubAdvisor/Events-ClubAdvisor.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("SACMS");
        stage.setResizable(false);
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }

}
