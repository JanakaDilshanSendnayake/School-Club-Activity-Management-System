package SceneControlersClubAdvisors;

import CommonSceneControlers.BaseSceneController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Label;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class CAHome extends BaseSceneController implements Initializable {

    @FXML
    private Label greetings;
    @FXML
    private BarChart<?, ?> userCount;

    @FXML
    private CategoryAxis x;

    @FXML
    private NumberAxis y;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Get the current date
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy");
        String formattedDateTime = currentDate.format(formatter);
        // Set the formatted date and time to the Text variable

        greetings.setText("Today is: " + formattedDateTime);
    }

}
