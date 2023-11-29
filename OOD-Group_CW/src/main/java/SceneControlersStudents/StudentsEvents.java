package SceneControlersStudents;


import CommonSceneControlers.BaseSceneController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import main.Main;
import stake_holders.Clubs;
import stake_holders.Events;
import utils.ClubDataHandling;
import utils.EventDataHandling;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Manith Ratnayake RGU ID-2237949
 */
public class StudentsEvents extends BaseSceneController implements Initializable {

    @FXML private ComboBox<String> navigateEventsPageComboBox;
    @FXML private ComboBox<String> navigateEventsPageComboBox2;
    ObservableList<String> navigateEventsPageComboBoxData= FXCollections.observableArrayList();
    ObservableList<String> navigateEventsPageComboBox2Data = FXCollections.observableArrayList(
            "All Clubs",
            "My Clubs"
    );
    private String organizingClubComboBoxSelectedOption;
    private String navigateEventsPageComboBoxSelectedOption;
    //=================================================================

    @FXML private TableView<Events> eventNavigateTable;
    @FXML private TableColumn<Events,String> eventIdColumn;
    @FXML private TableColumn<Events,String> eventNameColumn;
    @FXML private TableColumn<LocalDate,String> eventDateColumn;
    @FXML private TableColumn<Events,String> eventVenueColumn;

    //====================================================================
    @FXML private Label eventNameLabel;
    @FXML private Label eventVenueLabel;
    @FXML private Label eventDateLabel;
    @FXML private Label eventOrganizingClubNmaeLabel;
    @FXML private TextArea eventDescriptionTextArea;
    //============================================
    @FXML private AnchorPane caNaviEvents;
    @FXML private AnchorPane caViewEvents;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        caNaviEvents.toFront();
        eventDescriptionTextArea.setWrapText(true);
        navigateEventsPageComboBox2.setItems(navigateEventsPageComboBox2Data);


        ClubDataHandling object=new ClubDataHandling();
        object.loadClubDataRelevantToStudent(Main.currentStudentUser);

        ArrayList<String> namesOfAllClubs=new ArrayList<>();
        ArrayList<String> namesOfMyClubs = new ArrayList<>();

        try {
            for (Clubs ca: object.loadAllClubs()){
                namesOfAllClubs.add(ca.getClubId()+"-"+ca.getClubName());
            }
        }catch (SQLException e){
            showErrorAlert(e.getMessage());
        }
        for (Clubs ca : Main.currentStudentUser.getJoinedClubs()) {
            namesOfMyClubs.add(ca.getClubId()+"-"+ca.getClubName());
        }
        //================================================================

        navigateEventsPageComboBox2.setOnAction(event->{
            object.loadClubDataRelevantToStudent(Main.currentStudentUser);

            String selectedOption = navigateEventsPageComboBox2.getSelectionModel().getSelectedItem();

            if (selectedOption.equals("All Clubs")){
                navigateEventsPageComboBoxData.clear();
                navigateEventsPageComboBoxData.addAll(namesOfAllClubs);
            } else if (selectedOption.equals("My Clubs")) {
                navigateEventsPageComboBoxData.clear();
                navigateEventsPageComboBoxData.addAll(namesOfMyClubs);
            }
            navigateEventsPageComboBox.setItems(navigateEventsPageComboBoxData);
        });

        navigateEventsPageComboBox.setOnAction(event->{
            navigateEventsPageComboBoxSelectedOption = navigateEventsPageComboBox.getSelectionModel().getSelectedItem();

            if (navigateEventsPageComboBoxSelectedOption != null) {
                setUpEventNavigateTable(selectedClubOptionToClubObject(navigateEventsPageComboBoxSelectedOption)) ;
            }
        });
        //==================================================================

    }

    private void setUpEventNavigateTable(Clubs clubs){
        eventIdColumn.setCellValueFactory(new PropertyValueFactory<>("eventId"));
        eventNameColumn.setCellValueFactory(new PropertyValueFactory<>("eventName"));
        eventDateColumn.setCellValueFactory(new PropertyValueFactory<>("eventDate"));
        eventVenueColumn.setCellValueFactory(new PropertyValueFactory<>("eventLocation"));

        EventDataHandling obj=new EventDataHandling();
        obj.loadEventDataOfAClub(clubs);
        ObservableList<Events> eventNavigateTableData=FXCollections.observableArrayList();
        try{eventNavigateTableData.addAll(clubs.getClubEvents());}catch (Exception e){
            showInfoAlert("There aren't any events under this club");
        }
        eventNavigateTable.setItems(eventNavigateTableData);
    }
    private Clubs selectedClubOptionToClubObject(String selectedClubOption){
        String[] split=selectedClubOption.split("-");
        ClubDataHandling obj=new ClubDataHandling();
        return obj.loadClubData(split[0]);
    }
    private Events selectedEventOptionToObject(String selectedEventOption){
        String[] split=selectedEventOption.split("-");
        EventDataHandling obj=new EventDataHandling();
        return obj.loadEventData(split[0]);
    }

    @FXML
    public void viewEventDetails(){
        Events theEventUserIsViewing=eventNavigateTable.getSelectionModel().getSelectedItem();

        if(theEventUserIsViewing==null){
            showErrorAlert("please select an event from the table");
        }else{
            //set up the text labels
            eventNameLabel.setText(theEventUserIsViewing.getEventName());
            eventDescriptionTextArea.setText(theEventUserIsViewing.getEventDescription());
            eventDateLabel.setText(theEventUserIsViewing.getEventDate().toString());
            eventVenueLabel.setText(theEventUserIsViewing.getEventLocation());
            eventOrganizingClubNmaeLabel.setText(theEventUserIsViewing.getParentClub().getClubName());
            //loading the pane
            caViewEvents.toFront();

        }
    }
    @FXML
    public void goBackToEventNavigation(){
        caNaviEvents.toFront();
    }
}
