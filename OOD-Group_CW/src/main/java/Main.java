import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {

//    public void init() throws Exception{
//        //Load the database
//    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("fxml_files/Common/Login.fxml")));
        Scene scene = new Scene(root);

        //Setting up the stage
        stage.setScene(scene);
        stage.setTitle("SACMS");
        stage.setResizable(false);
        stage.show();


    }
    public static void main(String[] args) {
        launch(args);
    }

//    public void stop(){
//        //Close the database
//    }
}
