package utils;

import main.Main;
import stake_holders.ClubAdvisor;
import stake_holders.Clubs;
import stake_holders.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class ClubDataHandling {

    public boolean clubIdValidation(String userIdToBeValidated){
        boolean clubIdAlreadyExists=false;
        String sql ="SELECT * FROM clubs WHERE club_id= ?";
        MySqlConnect databaseLink= new MySqlConnect();

        try (Connection connection = databaseLink.getDatabaseLink();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, userIdToBeValidated);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    //User Id already exists
                    clubIdAlreadyExists = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database connection or query errors
        }

        return clubIdAlreadyExists;

    }

    public void saveNewClubToDatabase(Clubs clubs) {
        String sql = "INSERT INTO clubs (club_id, club_name, club_type, club_description) VALUES (?,?, ?, ?)";

        MySqlConnect databaseLink= new MySqlConnect();

        try (Connection connection = databaseLink.getDatabaseLink();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Set parameters for the SQL query
            preparedStatement.setString(1, clubs.getClubId());
            preparedStatement.setString(2, clubs.getClubName());
            preparedStatement.setString(3, clubs.getClubType());
            preparedStatement.setString(4, clubs.getClubDescription());
            // Execute the SQL query
            preparedStatement.executeUpdate();
            //to set the original creator as admin automaticaly(to add the club to club_advisor_club table)
            addANewCAMember(clubs.getClubAdmin().get(0),clubs);

            System.out.println("Club data saved successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void addANewCAMember(ClubAdvisor clubAdvisor,Clubs clubs){
        String sql="INSERT INTO club_advisor_clubs(club_advisor_id,club_id,is_admin) VALUES (?, ?, ?)";
        MySqlConnect databaseLink= new MySqlConnect();

        try (Connection connection = databaseLink.getDatabaseLink();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, clubAdvisor.getClubAdvisorId());
            preparedStatement.setString(2, clubs.getClubId());

            ArrayList<String> array=new ArrayList<>();
            for(ClubAdvisor ca:clubs.getClubAdmin()){
                array.add(ca.getClubAdvisorId());
            }
            preparedStatement.setBoolean(3, array.contains(clubAdvisor.getClubAdvisorId()));
            System.out.println(clubAdvisor.getClubAdvisorId());
            System.out.println(clubs.getClubId());
            //System.out.println(Objects.equals(clubs.getClubAdmin().getClubAdvisorId(), clubAdvisor.getClubAdvisorId()));
            preparedStatement.executeUpdate();
            System.out.println("success");
        } catch (SQLException e) {;
            System.out.println(e.getMessage());
        }
    }

    //to promote an existing clubAdvisor member to admin
    public void promoteToClubAdmin(ClubAdvisor newadmin,Clubs clubs){
        String sql = "UPDATE club_advisor_clubs SET is_admin = ? WHERE club_advisor_id = ? AND club_id=?";
        MySqlConnect databaseLink= new MySqlConnect();

        try (Connection connection = databaseLink.getDatabaseLink();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            //preparedStatement.setBoolean(1, clubs.getClubAdmin()==newadmin);

            ArrayList<String> array=new ArrayList<>();
            for(ClubAdvisor clubAdvisor:clubs.getClubAdmin()){
                array.add(clubAdvisor.getClubAdvisorId());
            }
            preparedStatement.setBoolean(1, array.contains(newadmin.getClubAdvisorId()));
            preparedStatement.setString(2, newadmin.getClubAdvisorId());
            preparedStatement.setString(3, clubs.getClubId());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //to load data of a certain club
    public Clubs loadClubData(String clubID){
        Clubs club=null;
        String sql="SELECT * FROM clubs WHERE club_Id = ?";
        MySqlConnect databaseLink= new MySqlConnect();

        try (Connection connection = databaseLink.getDatabaseLink();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, clubID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String clubiD=resultSet.getString("club_id");
                    String clubName=resultSet.getString("club_name");
                    String clubType=resultSet.getString("club_type");
                    String clubDescription=resultSet.getString("club_description");
                    club=new Clubs(clubiD,clubName,clubType,clubDescription);
                    System.out.println(club.getClubName());
                }
            }

        }catch (SQLException e) {
            e.printStackTrace();
            // Handle database connection or query errors
        }
        return club;
    }

    public void loadClubDataRelevantToCA(ClubAdvisor clubAdvisor){
        ArrayList<Clubs>array1=new ArrayList<>();
        ArrayList<Clubs>array2=new ArrayList<>();
        String sql="SELECT * FROM club_advisor_clubs WHERE club_advisor_id = ?";
        MySqlConnect databaseLink= new MySqlConnect();
        try (Connection connection = databaseLink.getDatabaseLink();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, clubAdvisor.getClubAdvisorId());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String clubAdvisorIdID=resultSet.getString("club_advisor_id");
                    String clubID=resultSet.getString("club_id");
                    boolean admin=resultSet.getBoolean("is_admin");


                    if(admin){
                        Clubs clubWithAdminAccess=loadClubData(clubID);
                        array1.add(clubWithAdminAccess);
                        clubAdvisor.setClubsWithAdminAccess(array1);

                    }else{
                        Clubs clubsWithoutAdminAccess=loadClubData(clubID);
                        array2.add(clubsWithoutAdminAccess);
                        clubAdvisor.setClubsWithoutAdminAccess(array2);
                    }
                }
            }

        }catch (SQLException e) {
            e.printStackTrace();
            // Handle database connection or query errors
        }
    }

    public void loadClubMembershipData(Clubs club){
        String sql="SELECT * FROM club_advisor_clubs WHERE club_id = ?";
        MySqlConnect databaseLink= new MySqlConnect();
        try (Connection connection = databaseLink.getDatabaseLink();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, club.getClubId());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String clubAdvisorId=resultSet.getString("club_advisor_id");
                    String clubID=resultSet.getString("club_id");
                    boolean admin=resultSet.getBoolean("is_admin");

                    CADataHandling object=new CADataHandling();
                    if(admin){
                        club.setClubAdmin(object.loadClubAdvisorData(clubAdvisorId));
                    }else{
                        club.getClubAdvisorMembers().add(object.loadClubAdvisorData(clubAdvisorId));
                    }
                }
            }

        }catch (SQLException e) {
            e.printStackTrace();
            // Handle database connection or query errors
        }
    }



    public ArrayList<Clubs> loadAllClubs() throws SQLException {
        ArrayList<Clubs> clubs = new ArrayList<>();
        String sql="SELECT * FROM clubs";
        MySqlConnect databaseLink= new MySqlConnect();
        try (Connection connection = databaseLink.getDatabaseLink();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Clubs club = new Clubs(String.valueOf(resultSet.getString("club_id")),resultSet.getString("club_name"),resultSet.getString("club_type"),resultSet.getString("club_description"));

//                //loading the membership details of the club--this might unnecessary
//                loadClubMembershipData(club);
                clubs.add(club);
            }

        }catch (SQLException e) {
            e.printStackTrace();
            // Handle database connection or query errors
        }
        return clubs;
    }

    public void updateClubInDatabase(Clubs updatedClub){
        String sql = "UPDATE clubs SET club_name = ?, club_type = ?, club_description = ? WHERE club_id = ?";
        MySqlConnect databaseLink= new MySqlConnect();

        try (Connection connection = databaseLink.getDatabaseLink();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Set parameters for the SQL query
            preparedStatement.setString(1, updatedClub.getClubName());
            preparedStatement.setString(2, updatedClub.getClubType());
            preparedStatement.setString(3, updatedClub.getClubDescription());
            preparedStatement.setString(4, updatedClub.getClubId());

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


//===========================================================================
public void loadClubDataRelevantToStudent(Student student){
    String sql="SELECT * FROM student_clubs WHERE club_id = ?";
    MySqlConnect databaseLink= new MySqlConnect();
    try (Connection connection = databaseLink.getDatabaseLink();
         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

        preparedStatement.setString(1, student.getStudentId());
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                String clubAdvisorIdID=resultSet.getString("club_advisor_id");
                String clubID=resultSet.getString("club_id");
                Boolean admin=resultSet.getBoolean("is_admin");

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
