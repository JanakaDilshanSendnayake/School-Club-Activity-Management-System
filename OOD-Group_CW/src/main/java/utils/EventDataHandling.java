package utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;

import static java.sql.DriverManager.getConnection;

public class EventDataHandling {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/sacms";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "PerfLog55Weak";

    ObservableList<String> clubList = FXCollections.observableArrayList();

    Connection connection = null;
    String sql;


    /**
     * Club advisor schedule a new event
     * @param newEventIDField
     * @param newEventNameField
     * @param newEventVenueField
     * @param newEventDatePicker
     * @param organizingClubComboBoxValue
     * @param newEventDescriptionTextArea
     */
    public void newEventCreationToDatabase(String newEventIDField, String newEventNameField, String newEventVenueField, LocalDate newEventDatePicker, String organizingClubComboBoxValue, String newEventDescriptionTextArea) {

        String club_id = "";

        // To find the club id of the organizing club , then later query it as foreign key to even table
        sql = " SELECT club_id FROM club WHERE club_name = ? ";

        // Database
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = getConnection(JDBC_URL, USERNAME, PASSWORD);

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                // Set the parameter (club_name) for the query
                preparedStatement.setString(1, organizingClubComboBoxValue);

                // Execute the query and get the result set
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    // Check if there is a result
                    if (resultSet.next()) {
                        // Retrieve the value of the "club_id" column from the result set
                        club_id = resultSet.getString("club_id");

                        // Now, you can use the clubId variable as needed
                        System.out.println("Club ID: " + club_id);
                    } else {
                        System.out.println("No matching club found.");
                    }
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            // Handle any SQL or class loading errors
        }
        sql =

        "INSERT INTO event (event_id, event_name, event_venue,  event_date, event_description, club_id ) VALUES (?, ? , ?, ?, ?, ? )";

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = getConnection(JDBC_URL, USERNAME, PASSWORD);

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, newEventIDField);
            preparedStatement.setString(2, newEventNameField);
            preparedStatement.setString(3, newEventVenueField);
            preparedStatement.setString(4, String.valueOf(newEventDatePicker));
            preparedStatement.setString(5, newEventDescriptionTextArea);
            preparedStatement.setString(6, club_id);

            preparedStatement.executeUpdate();

            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Combobox for clubs; which displays all available clubs using database club table
     * @return clubList
     */
    public ObservableList<String> getClubListFromDatabase() {

        try (Connection connection = getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String query =

            " SELECT club_name FROM club ";

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


    /**
     * Get the events of a club using join sql query method
     * @param userInputClubName
     * @return
     */
    public ObservableList<String> getEventListByClub(String userInputClubName) {

        ObservableList<String> eventNames = FXCollections.observableArrayList();
        try (Connection connection = getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            sql =

        " SELECT event_name FROM event JOIN club ON event.club_id = club.club_id WHERE club_name = ? ";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
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


    /**
     * In the update combo box we have strings as club name not integers club id here
     * Since only 1 foreign key we turn it
     * @param selectedEvent
     * @return
     */
    public ObservableList<Object> getEventsByEventName(String selectedEvent) {
        String club_id = "";
        ObservableList<Object> observableList = FXCollections.observableArrayList();

        try (Connection connection = getConnection(JDBC_URL, USERNAME, PASSWORD)) {

        sql =

        " SELECT event_id, event_name, event_venue, event_date, event_description, club_id " +
        " FROM event WHERE event_name = ? ";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, selectedEvent);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {

                        int eventId = resultSet.getInt("event_id");
                        String eventName = resultSet.getString("event_name");
                        String eventVenue = resultSet.getString("event_venue");
                        LocalDate eventDate = resultSet.getDate("event_date").toLocalDate();
                        String eventDescription = resultSet.getString("event_description");
                        club_id = resultSet.getString("club_id");


                        observableList.add(eventId);
                        observableList.add(eventName);
                        observableList.add(eventVenue);
                        observableList.add(eventDate);
                        observableList.add(eventDescription);

                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any SQL errors
        }

        club_id = clubIdtoClubName(club_id);
        observableList.add(club_id);
        return observableList;
    }


    /**
     *
     * @param club_id
     * @return
     */
    public String clubIdtoClubName(String club_id){
        String club_name = "";

        // To find the club id of the organizing club , then later query it as foreign key to even table
        sql = " SELECT club_name FROM club WHERE club_id = ? ";

        // Database
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = getConnection(JDBC_URL, USERNAME, PASSWORD);

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                // Set the parameter (club_name) for the query
                preparedStatement.setString(1, club_id);

                // Execute the query and get the result set
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    // Check if there is a result
                    if (resultSet.next()) {
                        // Retrieve the value of the "club_id" column from the result set
                         club_name = resultSet.getString("club_name");

                        // Now, you can use the clubId variable as needed
                        System.out.println("Club ID: " + club_id);
                    } else {
                        System.out.println("No matching club found.");
                    }
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            // Handle any SQL or class loading errors
        }
        return club_name;
    }

    public String clubNameToClubID(String selectClubComboBoxUpdatePass){

        String club_id = "";

        // To find the club id of the organizing club , then later query it as foreign key to even table
        sql = " SELECT club_id FROM club WHERE club_name = ? ";

        // Database
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = getConnection(JDBC_URL, USERNAME, PASSWORD);

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                // Set the parameter (club_name) for the query
                preparedStatement.setString(1, selectClubComboBoxUpdatePass);

                // Execute the query and get the result set
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    // Check if there is a result
                    if (resultSet.next()) {
                        // Retrieve the value of the "club_id" column from the result set
                        club_id = resultSet.getString("club_id");

                        // Now, you can use the clubId variable as needed
                        System.out.println("Club ID: " + club_id);
                    } else {
                        System.out.println("No matching club found.");
                    }
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            // Handle any SQL or class loading errors
        }
        return club_id;



    }













    /**
     *
     * @param newEventID
     * @param newEventName
     * @param newEventVenue
     * @param newEventDate
     * @param newEventDescriptionText
     * @param
     * @throws SQLException
     */
    public void UpdateEventTable(String newEventID, String newEventName, String newEventVenue, LocalDate newEventDate, String newEventDescriptionText, String club_idPass) throws SQLException {

        System.out.println("time" + newEventDate);

        try (Connection connection = getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            // DELETE operation
            String deleteQuery = "DELETE FROM event WHERE event_id = ? ";

            try (PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
                deleteStatement.setString(1, newEventID);
                int rowsDeleted = deleteStatement.executeUpdate();
                System.out.println("Rows deleted: " + rowsDeleted);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any SQL errors for delete operation
        }

        try (Connection connection = getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            // INSERT operation
            String insertQuery =

                    "INSERT INTO event (event_id, event_name, event_venue, event_date, event_description, club_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                insertStatement.setString(1, newEventID);
                insertStatement.setString(2, newEventName);
                insertStatement.setString(3, newEventVenue);
                insertStatement.setDate(4, java.sql.Date.valueOf(newEventDate));
                insertStatement.setString(5, newEventDescriptionText);
                insertStatement.setString(6, club_idPass);

                int rowsInserted = insertStatement.executeUpdate();
                System.out.println("Rows inserted: " + rowsInserted);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any SQL errors for insert operation
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
