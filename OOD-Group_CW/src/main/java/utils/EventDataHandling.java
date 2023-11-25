package utils;
import java.sql.*;
import java.time.LocalDate;

public class EventDataHandling {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/sacms";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "PerfLog55Weak";


    public void newEventCreationToDatabase(String newEventIDField, String newEventNameField ,String newEventVenueField, LocalDate newEventDatePicker, String organizingClubComboBox, String newEventDescriptionTextArea) {
        String sql = "INSERT INTO event (event_id, event_name, event_venue, event_club, event_date, event_description) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);

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

}


