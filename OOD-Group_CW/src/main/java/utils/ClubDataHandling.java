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

/**
 *
 *BY AKSHITH RAJENDRAN RGU ID: 2236761
 *
 */

public class ClubDataHandling {
    //This will be used to check if the automatically generated club id is already in use
    public boolean clubIdValidation(String userIdToBeValidated){
        boolean clubIdAlreadyExists=false;
        String sql ="SELECT * FROM clubs WHERE BINARY club_id= ?";
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
        }
        return clubIdAlreadyExists;

    }
// To save a new club in the databse
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
            //
            ArrayList<String> array=new ArrayList<>();
            for(ClubAdvisor ca:clubs.getClubAdmin()){
                array.add(ca.getClubAdvisorId());
            }
            preparedStatement.setBoolean(3, array.contains(clubAdvisor.getClubAdvisorId()));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {;
            System.out.println(e.getMessage());
        }
    }
    //This will be used when club advisor a club advisor wants to leave a club and then to update table
    public void removeClubAdvisor(ClubAdvisor clubAdvisor,Clubs clubs){
        String sql="DELETE FROM club_advisor_clubs WHERE BINARY club_advisor_id = ? AND BINARY club_id= ?";
        MySqlConnect databaseLink= new MySqlConnect();
        try (Connection connection = databaseLink.getDatabaseLink();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, clubAdvisor.getClubAdvisorId());
            preparedStatement.setString(2, clubs.getClubId());;
            preparedStatement.executeUpdate();
        } catch (SQLException e) {;
            e.printStackTrace();
        }
    }

    //to promote an existing clubAdvisor member to admin
    public void promoteToClubAdmin(ClubAdvisor newAdmin,Clubs clubs){
        String sql = "UPDATE club_advisor_clubs SET is_admin = ? WHERE BINARY club_advisor_id = ? AND BINARY club_id=?";
        MySqlConnect databaseLink= new MySqlConnect();

        try (Connection connection = databaseLink.getDatabaseLink();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setBoolean(1, true);
            preparedStatement.setString(2, newAdmin.getClubAdvisorId());
            preparedStatement.setString(3, clubs.getClubId());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Admin promotion successful");
            } else {
                System.out.println("Admin promotion failed");
            }

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
        }
        return club;
    }
//this method wil load the clubs relevant to club advisor(clubs with admin access and without admin access) and save
// them in the club advisor object using the setters.
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
                        loadClubMembershipData(clubWithAdminAccess);
                        array1.add(clubWithAdminAccess);
                    }else{
                        Clubs clubsWithoutAdminAccess=loadClubData(clubID);
                        loadClubMembershipData(clubsWithoutAdminAccess);
                        array2.add(clubsWithoutAdminAccess);
                    }
                }
                clubAdvisor.setClubsWithAdminAccess(array1);
                clubAdvisor.setClubsWithoutAdminAccess(array2);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadClubMembershipData(Clubs club){
        ArrayList<ClubAdvisor>array1=new ArrayList<>();
        ArrayList<ClubAdvisor>array2=new ArrayList<>();
        String sql="SELECT * FROM club_advisor_clubs WHERE BINARY club_id = ?";
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
                        ClubAdvisor clubAdvisor=object.loadClubAdvisorData(clubAdvisorId);
                        array1.add(clubAdvisor);
                        club.setClubAdmin(array1);
                    }else{
                        ClubAdvisor clubAdvisor=object.loadClubAdvisorData(clubAdvisorId);
                        array2.add(clubAdvisor);
                        club.setClubAdvisorMembers(array2);
                    }
                }
            }

        }catch (SQLException e) {
            e.printStackTrace();
            // Handle database connection or query errors
        }
    }
    //This method will load all the clubs in databse. Will be used to view all clubs in club navigation pane
    public ArrayList<Clubs> loadAllClubs() throws SQLException {
        ArrayList<Clubs> clubs = new ArrayList<>();
        String sql="SELECT * FROM clubs";
        MySqlConnect databaseLink= new MySqlConnect();
        try (Connection connection = databaseLink.getDatabaseLink();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Clubs club = new Clubs(String.valueOf(resultSet.getString("club_id")),resultSet.getString("club_name"),resultSet.getString("club_type"),resultSet.getString("club_description"));
                clubs.add(club);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return clubs;
    }
//This method will take a updated club object as an argument and update the table according to the data in updated club object
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
        ArrayList<Clubs>array1=new ArrayList<>();
        String sql="SELECT * FROM student_clubs WHERE student_id = ?";
        MySqlConnect databaseLink= new MySqlConnect();
        try (Connection connection = databaseLink.getDatabaseLink();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, student.getStudentId());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String studentId=resultSet.getString("student_id");
                    String clubID=resultSet.getString("club_id");

                    Clubs clubStudentHaveJoined=loadClubData(clubID);
                    array1.add(clubStudentHaveJoined);
                    student.setJoinedClubs(array1);
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void loadClubStudentMembershipData(Clubs club){
        ArrayList<Student>array1=new ArrayList<>();
        String sql="SELECT * FROM student_clubs WHERE club_id = ?";
        MySqlConnect databaseLink= new MySqlConnect();
        try (Connection connection = databaseLink.getDatabaseLink();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, club.getClubId());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String StudentId=resultSet.getString("student_id");
                    String clubID=resultSet.getString("club_id");
                    //boolean admin=resultSet.getBoolean("is_admin");


                    StudentDataHandling object=new StudentDataHandling();
                    Student student=object.loadStudentData(StudentId);
                    array1.add(student);


                }club.setStudentMembers(array1);
            }

        }catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public void addANewStudentMember(Student student,Clubs clubs){
        String sql="INSERT INTO student_clubs(student_id,club_id) VALUES (?, ?)";
        MySqlConnect databaseLink= new MySqlConnect();
        try (Connection connection = databaseLink.getDatabaseLink();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, student.getStudentId());
            preparedStatement.setString(2, clubs.getClubId());
            preparedStatement.executeUpdate();
            System.out.println("You joined the club successfully");
        } catch (SQLException e) {;
            System.out.println(e.getMessage());
        }
    }
    public void removeStudentMember(Student student,Clubs clubs){
        String sql="DELETE FROM student_clubs WHERE BINARY student_id = ? AND BINARY club_id= ?";
        MySqlConnect databaseLink= new MySqlConnect();

        try (Connection connection = databaseLink.getDatabaseLink();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, student.getStudentId());
            preparedStatement.setString(2, clubs.getClubId());;
            preparedStatement.executeUpdate();
            System.out.println("You left the club successfully");
        } catch (SQLException e) {;
            e.printStackTrace();
        }
    }








}
