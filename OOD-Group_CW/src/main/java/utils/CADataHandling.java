package utils;

import main.Main;
import stake_holders.ClubAdvisor;
import stake_holders.Clubs;
import stake_holders.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
//BY JANAKA SENDANAYAKE RGU ID:2237952

public class CADataHandling {

    //This method will be used for validate the user id and the name provide by club advisor members when login
    //First it checks if there is a field in club_advisor table with the user id and password user entered
    //If it's there then a club advisor object is made using the information from the relevant columns and then that object will be assigned
    //to Main.currentUser which will be used to track the user who's currently using the system
    public boolean clubAdvisorLogin(String clubAdvisorId, String password){
        boolean isAuthenticated = false;
        String sql = "SELECT * FROM club_advisor WHERE BINARY club_advisor_id = ? AND BINARY club_advisor_password = ?";
        MySqlConnect databaseLink= new MySqlConnect();
        try (Connection connection = databaseLink.getDatabaseLink();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, clubAdvisorId);
            preparedStatement.setString(2, password);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Student ID and password match
                    isAuthenticated = true;
                    String clubadvisorId=resultSet.getString("club_advisor_id");
                    String clubAdvisorName=resultSet.getString("club_advisor_name");
                    String clubAdvisorEmail=resultSet.getString("club_advisor_email");
                    String clubAdvisorTele=resultSet.getString("club_advisor_telephone");
                    String clubAdvisorPassword=resultSet.getString("club_advisor_password");
                    //creating new object for logged in club advisor
                    ClubAdvisor loggedInClubAdvisor=new ClubAdvisor(clubAdvisorName,clubAdvisorEmail,clubAdvisorTele,clubAdvisorPassword);
                    loggedInClubAdvisor.setClubAdvisorId(clubadvisorId);
                    //assigning the above created object to current logged-in user tracker in main
                    Main.currentUser=loggedInClubAdvisor;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isAuthenticated;
    }

    //To check if the username is already being used by another club advisor member.
    public boolean clubAdvisorUserNameValidation(String userIdToBeValidated){
        boolean userIdAlreadyExists=false;
        String sql ="SELECT * FROM club_advisor WHERE BINARY club_advisor_id= ?";
        MySqlConnect databaseLink= new MySqlConnect();

        try (Connection connection = databaseLink.getDatabaseLink();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, userIdToBeValidated);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    //User Id already exists
                    userIdAlreadyExists = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userIdAlreadyExists;

    }

    //This will be used to save a newly registered club advisor in the table
    public void saveNewCAToDatabase(ClubAdvisor clubAdvisor) {
        String sql = "INSERT INTO club_advisor (club_advisor_id, club_advisor_name, club_advisor_email, club_advisor_telephone, club_advisor_password) VALUES (?, ?, ?, ?, ?)";

        MySqlConnect databaseLink= new MySqlConnect();

        try (Connection connection = databaseLink.getDatabaseLink();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            // Set parameters for the SQL query
            preparedStatement.setString(1, clubAdvisor.getClubAdvisorId());
            preparedStatement.setString(2, clubAdvisor.getName());
            preparedStatement.setString(3, clubAdvisor.getEmail());
            preparedStatement.setString(4, clubAdvisor.getMobileNumber());
            preparedStatement.setString(5, clubAdvisor.getPassword());
            // Execute the SQL query
            preparedStatement.executeUpdate();

            System.out.println("Student data saved successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //This method is used to update the club_advisor table when user makes changes to account details in account mamangement page
    public void updateClubAdvisorInDatabase(ClubAdvisor updatedClubAdvisor) {
        String sql = "UPDATE club_advisor SET club_advisor_name = ?, club_advisor_email = ?, club_advisor_telephone = ?, club_advisor_password = ? WHERE BINARY club_advisor_id = ?";
        MySqlConnect databaseLink= new MySqlConnect();
        try (Connection connection = databaseLink.getDatabaseLink();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            // Set parameters for the SQL query
            preparedStatement.setString(1, updatedClubAdvisor.getName());
            preparedStatement.setString(2, updatedClubAdvisor.getEmail());
            preparedStatement.setString(3, updatedClubAdvisor.getMobileNumber());
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

    //to load data of a certain club advisor using just the id
    public ClubAdvisor loadClubAdvisorData(String clubAdvisorID){
        ClubAdvisor clubAdvisor=null;
        String sql="SELECT * FROM club_advisor WHERE BINARY club_advisor_Id = ?";
        MySqlConnect databaseLink= new MySqlConnect();

        try (Connection connection = databaseLink.getDatabaseLink();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, clubAdvisorID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String clubAdvisorIdiD=resultSet.getString("club_advisor_id");
                    String clubAdvisorName=resultSet.getString("club_advisor_name");
                    String clubAdvisorEmail=resultSet.getString("club_advisor_email");
                    String clubAdvisorTele=resultSet.getString("club_advisor_telephone");
                    String clubAdvisorPassword=resultSet.getString("club_advisor_password");
                    clubAdvisor=new ClubAdvisor(clubAdvisorName,clubAdvisorEmail,clubAdvisorTele,clubAdvisorPassword);
                    clubAdvisor.setClubAdvisorId(clubAdvisorIdiD);
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return clubAdvisor;
    }
    //To get total clubadvisor count
    public int getTotalClubAdvisorCount() {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM club_advisor";
        MySqlConnect databaseLink= new MySqlConnect();
        try (Connection connection = databaseLink.getDatabaseLink();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            // Retrieve the result
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

}
