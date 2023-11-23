package utils;

import stake_holders.ClubAdvisor;
import stake_holders.Clubs;
import stake_holders.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 * CLass which is handling the data related to club to the database
 */
public class ClubDataHandling {

    public void saveNewClubToDatabase(ClubAdvisor creatorCA,Clubs clubs) {
        String sql = "INSERT INTO Clubs (club_name, club_type, club_description) VALUES (?, ?, ?)";

        MySqlConnect databaseLink= new MySqlConnect();

        try (Connection connection = databaseLink.getDatabaseLink();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Set parameters for the SQL query
            //preparedStatement.setString(1, clubs.getClubId());
            preparedStatement.setString(1, clubs.getClubName());
            preparedStatement.setString(2, clubs.getClubType());
            preparedStatement.setString(3, clubs.getClubDescription());
            // Execute the SQL query
            preparedStatement.executeUpdate();
            //to set the original creator as admin automaticaly
            addANewCAMember(creatorCA,clubs);

            System.out.println("Club data saved successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void addANewCAMember(ClubAdvisor clubAdvisor,Clubs clubs){
        String sql="INSERT INTO club_advisor_club(club_advisor_id,club_id,is_admin) VALUES (?, ?, ?)";
        MySqlConnect databaseLink= new MySqlConnect();

        try (Connection connection = databaseLink.getDatabaseLink();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, clubAdvisor.getClubAdvisorId());
            preparedStatement.setString(2, clubs.getClubId());
            preparedStatement.setBoolean(3, clubs.getClubAdmin()==clubAdvisor);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //to promote an existing clubAdvisor member to admin
    public void promoteToClubAdmin(ClubAdvisor newadmin,Clubs clubs){
        String sql = "UPDATE club_advisor_club SET is_admin = ? WHERE club_advisor_id = ? AND club_id=?";
        MySqlConnect databaseLink= new MySqlConnect();

        try (Connection connection = databaseLink.getDatabaseLink();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setBoolean(1, clubs.getClubAdmin()==newadmin);
            preparedStatement.setString(2, newadmin.getClubAdvisorId());
            preparedStatement.setString(3, clubs.getClubId());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //to load data of a certain club
    public ArrayList<Clubs> loadClubData(String clubID){
        String sql="SELECT * FROM clubs WHERE Club_Id = ?";
        MySqlConnect databaseLink= new MySqlConnect();
        ArrayList<Clubs> clubsArrayList = new ArrayList<>();

        try (Connection connection = databaseLink.getDatabaseLink();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, clubID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String clubiD=resultSet.getString("Club_id");
                    String clubName=resultSet.getString("Club_name");
                    String clubType=resultSet.getString("Club_type");
                    String clubDescription=resultSet.getString("Club_description");

                    Clubs club=new Clubs(clubiD,clubName,clubType,clubDescription);
                    clubsArrayList.add(club);


                }
            }

        }catch (SQLException e) {
            e.printStackTrace();
            // Handle database connection or query errors
        }
        return clubsArrayList;
    }

    public void loadClubDataRelevantToCA(ClubAdvisor clubAdvisor){
        String sql="SELECT * FROM Club_Advisor_Club WHERE Club_id = ?";
        MySqlConnect databaseLink= new MySqlConnect();
        try (Connection connection = databaseLink.getDatabaseLink();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, clubAdvisor.getClubAdvisorId());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String clubAdvisorIdID=resultSet.getString("Club_advisor_id");
                    String clubID=resultSet.getString("Club_id");
                    Boolean admin=resultSet.getBoolean("Is_admin");

                    if(admin){
                        Clubs clubWithAdminAccess=loadClubData(clubID).get(0);
                        clubAdvisor.getClubsWithAdminAccess().add(clubWithAdminAccess);

                    }else{
                        Clubs clubsWithoutAdminAccess=loadClubData(clubID).get(0);
                        clubAdvisor.getClubsWithoutAdminAccess().add(clubsWithoutAdminAccess);
                    }
                }
            }

        }catch (SQLException e) {
            e.printStackTrace();
            // Handle database connection or query errors
        }
    }

    public void loadClubDataRelevantToStudent(Student student){
        String sql="SELECT * FROM Student_Club WHERE Club_id = ?";
        MySqlConnect databaseLink= new MySqlConnect();
        try (Connection connection = databaseLink.getDatabaseLink();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, student.getStudentId());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String clubAdvisorIdID=resultSet.getString("Club_advisor_id");
                    String clubID=resultSet.getString("Club_id");
                    Boolean admin=resultSet.getBoolean("Is_admin");

//                    if(admin){
//                        Clubs clubWithAdminAccess=loadClubData(clubID).get(0);
//                        student.getClubsWithAdminAccess().add(clubWithAdminAccess);
//
//                    }else{
//                        Clubs clubsWithoutAdminAccess=loadClubData(clubID).get(0);
//                        student.getClubsWithoutAdminAccess().add(clubsWithoutAdminAccess);
//                    }
                }
            }

        }catch (SQLException e) {
            e.printStackTrace();
            // Handle database connection or query errors
        }
    }



}
