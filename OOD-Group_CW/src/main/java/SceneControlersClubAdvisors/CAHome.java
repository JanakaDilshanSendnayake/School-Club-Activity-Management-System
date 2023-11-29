package SceneControlersClubAdvisors;

import CommonSceneControlers.BaseSceneController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import main.Main;
import utils.CADataHandling;
import utils.StudentDataHandling;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class CAHome extends BaseSceneController implements Initializable {

    @FXML
    private Label greetings;
    @FXML
    private Label greetings2;
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

        greetings.setText("Date: " + formattedDateTime);
        greetings2.setText("Welcome - "+ Main.currentUser.getName());

        //Loading the
        CADataHandling object=new CADataHandling();
        int caCount=object.getTotalClubAdvisorCount();
        StudentDataHandling object2=new StudentDataHandling();
        int studentCount=object2.getTotalStudentCount();
        XYChart.Series set1= new XYChart.Series<>();
        set1.getData().add(new XYChart.Data("Club-Advisors",caCount));
        set1.getData().add(new XYChart.Data("Students",studentCount));
        userCount.getData().addAll(set1);
    }

}
