package utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import stake_holders.Events;

import java.sql.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static java.sql.DriverManager.getConnection;

public class EventDataHandling {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/sacms";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "PerfLog55Weak";

    ObservableList<String> clubList = FXCollections.observableArrayList();

    Connection connection = null;


    public void newEventCreationToDatabase(String newEventIDField, String newEventNameField, String newEventVenueField, LocalDate newEventDatePicker, String organizingClubComboBox, String newEventDescriptionTextArea) {
        String sql = "INSERT INTO event (event_id, event_name, event_venue, event_club, event_date, event_description) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = getConnection(JDBC_URL, USERNAME, PASSWORD);

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, newEventIDField);
            preparedStatement.setString(2, newEventNameField);
            preparedStatement.setString(3, newEventVenueField);
            preparedStatement.setString(4, String.valueOf(newEventDatePicker));
            preparedStatement.setString(5, organizingClubComboBox);
            preparedStatement.setString(6, newEventDescriptionTextArea);

            preparedStatement.executeUpdate();

            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<String> getClubListFromDatabase() {

        String sql = "SELECT club_name FROM club";

        try (Connection connection = getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String query = "SELECT club_name FROM club";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    String clubName = resultSet.getString("club_name");
                    clubList.add(clubName);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clubList;
    }


    public ObservableList<String> getEventsByClubName(String userInputClubName) {
        ObservableList<String> eventNames = FXCollections.observableArrayList();
        String sql = "SELECT event_name FROM event WHERE club_name = ?";

        try (Connection connection = getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String query = "SELECT event_name FROM event JOIN club ON event.club_id = club.club_id " +
                    "WHERE club_name = ? ";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, userInputClubName);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        // Retrieve the value of the "event_name" column
                        String eventName = resultSet.getString("event_name");
                        System.out.println(eventName);
                        eventNames.add(eventName);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any SQL errors
        }

        return eventNames;
    }


    public ObservableList<Object> getEventsByEventName(String selectedEvent) {
        ObservableList<Object> observableList = FXCollections.observableArrayList();
        String sql = "SELECT event_name FROM event WHERE club_name = ?";

        int eventId;
        String eventName;
        try (Connection connection = getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String query = "SELECT event_id, event_name, event_venue, event_date, event_club, event_description " +
                    "FROM event WHERE event_name = ? ";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, selectedEvent);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        eventId = resultSet.getInt("event_id");
                        eventName = resultSet.getString("event_name");
                        String eventVenue = resultSet.getString("event_venue");
                        LocalDate eventDate = resultSet.getDate("event_date").toLocalDate();
                        ;
                        String eventClub = resultSet.getString("event_club");
                        String eventDescription = resultSet.getString("event_description");

                        observableList.add(eventId);
                        observableList.add(eventName);
                        observableList.add(eventVenue);
                        observableList.add(eventDate);
                        observableList.add(eventClub);
                        observableList.add(eventDescription);

                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any SQL errors
        }
        return observableList;
    }


    public void deleteExisitingData(String newEventID, String newEventName, String newEventVenue, LocalDate newEventDate, ComboBox<String> organizingClubComboBox, String newEventDescriptionText) throws SQLException {

        System.out.println("time" + newEventDate);

        System.out.println("here");
        try (Connection connection = getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            connection.setAutoCommit(false);  // Set autocommit to false

            // DELETE operation
            String deleteQuery = "DELETE FROM event WHERE event_id = ?";
            try (PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
                deleteStatement.setString(1, newEventID);
                int rowsDeleted = deleteStatement.executeUpdate();
                System.out.println("Rows deleted: " + rowsDeleted);
            }

            // INSERT operation
            String insertQuery = "INSERT INTO event (event_id, event_name, event_venue, event_date, event_club, event_description) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                insertStatement.setString(1, newEventID);
                insertStatement.setString(2, newEventName);
                insertStatement.setString(3, newEventVenue);
                insertStatement.setDate(4, java.sql.Date.valueOf(newEventDate));
                insertStatement.setString(5, organizingClubComboBox.getValue());
                insertStatement.setString(6, newEventDescriptionText);

                int rowsInserted = insertStatement.executeUpdate();
                System.out.println("Rows inserted: " + rowsInserted);
            }

            // Commit the transaction
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any SQL errors
        } finally {
            // Close the database connection in the finally block
            if (connection != null) {
                try {
                    connection.close();
                    System.out.println("Database connection closed.");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("Finished");
        }

    }
}




