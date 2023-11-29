package utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import stake_holders.Attendance;
import stake_holders.Clubs;
import stake_holders.Events;
import stake_holders.Student;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

import static java.sql.DriverManager.getConnection;

/**
 * Abdul Shahid- RGU ID 2237030 /
 * Manith Ratnayake RGU ID-2237949
 *
 *
 */
public class EventDataHandling {

    //====================================================================
    public boolean validateEventId(String eventIdToBeValidated) {
        boolean eventIdAlreadyExists = false;
        String sql = "SELECT * FROM events WHERE BINARY event_id= ?";
        MySqlConnect databaseLink = new MySqlConnect();

        try (Connection connection = databaseLink.getDatabaseLink();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, eventIdToBeValidated);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    //Events Id already exists
                    eventIdAlreadyExists = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return eventIdAlreadyExists;
    }

    public void loadEventDataOfAClub(Clubs club) {
        String sql = "SELECT * FROM events WHERE BINARY club_id = ?";
        ArrayList<Events> array = new ArrayList<>();
        MySqlConnect databaseLink = new MySqlConnect();
        try (Connection connection = databaseLink.getDatabaseLink();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, club.getClubId());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String eventId = resultSet.getString("event_id");
                    String eventName = resultSet.getString("event_name");
                    String eventDescription = resultSet.getString("event_description");
                    LocalDate eventDate = resultSet.getDate("event_date").toLocalDate();
                    String event_venue = resultSet.getString("event_venue");

                    array.add(new Events(eventId, eventName, eventDate, event_venue, eventDescription, club));

                }
                club.setClubEvents(array);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            // Handle database connection or query errors
        }
    }

    public Events loadEventData(String eventId) {
        Events event = null;
        String sql = "SELECT * FROM events WHERE event_Id = ?";
        MySqlConnect databaseLink = new MySqlConnect();

        try (Connection connection = databaseLink.getDatabaseLink();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, eventId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String clubiD = resultSet.getString("event_id");
                    String eventName = resultSet.getString("event_name");
                    String eventDescription = resultSet.getString("event_description");
                    LocalDate eventDate = resultSet.getDate("event_date").toLocalDate();
                    String eventVenue = resultSet.getString("event_venue");
                    String parentClubId = resultSet.getString("club_id");
                    ClubDataHandling obj = new ClubDataHandling();
                    Clubs parentClub = obj.loadClubData(parentClubId);
                    event = new Events(clubiD, eventName, eventDate, eventVenue, eventDescription, parentClub);

                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database connection or query errors
        }
        return event;
    }

    public void updateEventInDatabase(Events updatedEvent) {
        String sql = "UPDATE events SET event_name = ?, event_description = ?, event_date = ?, event_venue=?,club_id=? WHERE event_id = ?";
        MySqlConnect databaseLink = new MySqlConnect();

        try (Connection connection = databaseLink.getDatabaseLink();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Set parameters for the SQL query
            preparedStatement.setString(1, updatedEvent.getEventName());
            preparedStatement.setString(2, updatedEvent.getEventDescription());
            preparedStatement.setDate(3, java.sql.Date.valueOf(updatedEvent.getEventDate()));
            preparedStatement.setString(4, updatedEvent.getEventLocation());
            preparedStatement.setString(5, updatedEvent.getParentClub().getClubId());
            preparedStatement.setString(6, updatedEvent.getEventId());

            // Execute the SQL query
            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Student data updated successfully.");
            } else {
                System.out.println("No student found with the given username.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public boolean attendanceAlreadyCheckedOrNot(String eventId) {
        boolean attendanceAlreadyChecked = false;
        String sql = sql = "SELECT * FROM attendance WHERE event_id= ?";
        MySqlConnect databaseLink = new MySqlConnect();

        try (Connection connection = databaseLink.getDatabaseLink();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, eventId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    //event id already exists
                    attendanceAlreadyChecked = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return attendanceAlreadyChecked;

    }

    public void markAttendance(Attendance attendance) {
        String sql = "INSERT INTO attendance(student_id,event_id,student_name,event_name,attendance_status) VALUES (?,?,?,?,?)";
        MySqlConnect databaseLink = new MySqlConnect();
        try (Connection connection = databaseLink.getDatabaseLink();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Set parameters for the SQL query
            preparedStatement.setString(1, attendance.getStudentId());
            preparedStatement.setString(2, attendance.getEventId());
            preparedStatement.setString(3, attendance.getStudentName());
            preparedStatement.setString(4, attendance.getEventName());
            preparedStatement.setBoolean(5, attendance.isStatus());
            // Execute the SQL query
            preparedStatement.executeUpdate();

            System.out.println("attendance data saved successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Attendance> loadAttendance(Events event) {
        ArrayList<Attendance> array = new ArrayList<>();

        String sql = sql = "SELECT * FROM attendance WHERE event_id= ?";
        MySqlConnect databaseLink = new MySqlConnect();

        try (Connection connection = databaseLink.getDatabaseLink();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, event.getEventId());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String studentId = resultSet.getString("event_id");
                    String eventId = resultSet.getString("student_id");
                    String studentName = resultSet.getString("student_name");
                    String eventName = resultSet.getString("event_name");
                    Boolean status = resultSet.getBoolean("attendance_status");

                    Attendance attendance = new Attendance(studentId, studentName, eventId, eventName, status);
                    array.add(attendance);

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return array;
    }


    public void saveNewEventToDatabase(Events event) {
        String sql = "INSERT INTO events (event_id, event_name, event_description,event_date,event_venue,club_id ) VALUES (?, ? , ?, ?, ?, ? )";

        MySqlConnect databaseLink = new MySqlConnect();
        try (Connection connection = databaseLink.getDatabaseLink();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Set parameters for the SQL query
            preparedStatement.setString(1, event.getEventId());
            preparedStatement.setString(2, event.getEventName());
            preparedStatement.setString(3, event.getEventDescription());
            preparedStatement.setDate(4, java.sql.Date.valueOf(event.getEventDate()));
            preparedStatement.setString(5, event.getEventLocation());
            preparedStatement.setString(6, event.getParentClub().getClubId());
            // Execute the SQL query
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
