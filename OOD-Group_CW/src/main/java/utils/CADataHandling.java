package utils;

import stake_holders.ClubAdvisor;
import stake_holders.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CADataHandling {

    public boolean clubAdvisorLogin(String clubAdvisorId, String password){
        boolean isAuthenticated = false;
        String sql = "SELECT * FROM Club_Advisor WHERE Student_id = ? AND Student_password = ?";

        MySqlConnect databaseLink= new MySqlConnect();

        try (Connection connection = databaseLink.getDatabaseLink();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, clubAdvisorId);
            preparedStatement.setString(2, password);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Student ID and password match
                    isAuthenticated = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database connection or query errors
        }

        return isAuthenticated;
    }

    public static void saveNewCAToDatabase(ClubAdvisor clubAdvisor) {
        String sql = "INSERT INTO club_advisor (club_advisor_id, club_advisor_name, club_advisor_email, club_advisor_telephone, club_advisor_password) VALUES (?, ?, ?, ?, ?)";

        MySqlConnect databaseLink= new MySqlConnect();

        try (Connection connection = databaseLink.getDatabaseLink();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Set parameters for the SQL query
            preparedStatement.setString(1, clubAdvisor.getClubAdvisorId());
            preparedStatement.setString(2, clubAdvisor.getName());
            preparedStatement.setString(3, clubAdvisor.getEmail());
            preparedStatement.setString(4, clubAdvisor.getMobileNum());
            preparedStatement.setString(5, clubAdvisor.getPassword());

            // Execute the SQL query
            preparedStatement.executeUpdate();

            System.out.println("Student data saved successfully.");

        } catch (SQLException e) {
            System.out.println("sorry this name is already taken");;
        }
    }

    public void updateClubAdvisorInDatabase(ClubAdvisor updatedClubAdvisor) {
        String sql = "UPDATE student SET club_advisor_name = ?, club_advisor_email = ?, club_advisor_telephone = ?, club_advisor_password = ? WHERE club_advisor_id = ?";
        MySqlConnect databaseLink= new MySqlConnect();

        try (Connection connection = databaseLink.getDatabaseLink();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Set parameters for the SQL query
            preparedStatement.setString(1, updatedClubAdvisor.getName());
            preparedStatement.setString(2, updatedClubAdvisor.getEmail());
            preparedStatement.setString(3, updatedClubAdvisor.getMobileNum());
            preparedStatement.setString(4, updatedClubAdvisor.getPassword());
            preparedStatement.setString(5, updatedClubAdvisor.getClubAdvisorId());

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



}
