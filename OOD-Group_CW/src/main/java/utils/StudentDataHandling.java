package utils;

import main.Main;
import stake_holders.ClubAdvisor;
import stake_holders.Student;

import java.sql.*;

public class StudentDataHandling {
    //This method will be used for validate the user id and the name provide by student members when login
    //First it checks if there is a field in club_advisor table with the user id and password user entered
    //If it's there then a club advisor object is made using the information from the relevant columns and then that object will be assigned
    //to Main.currentStudentUser which will be used to track the user who's currently using the system
    public boolean studentLogin(String studentId, String password){
        boolean isAuthenticated = false;
        String sql = "SELECT * FROM student WHERE BINARY student_id = ? AND BINARY student_password = ?";
        MySqlConnect databaseLink= new MySqlConnect();
        try (Connection connection = databaseLink.getDatabaseLink();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, studentId);
            preparedStatement.setString(2, password);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Student ID and password match
                    isAuthenticated = true;
                    String studentName=resultSet.getString("student_name");
                    String studentEmail=resultSet.getString("student_email");
                    String studentTele=resultSet.getString("student_telephone");

                    Student logggedInStudent=new Student(studentName,studentEmail,studentTele,password);
                    logggedInStudent.setStudentId(studentId);
                    Main.currentStudentUser=logggedInStudent;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isAuthenticated;
    }
    //This will be used during student registration To check if the username is already being used by another student member.
    public boolean studentUserNameValidation(String userIdToBeValidated){
        boolean userIdAlreadyExists=false;
        String sql =sql="SELECT * FROM student WHERE student_id= ?";
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
    //This will be used to save newly registered students in the databse
    public void saveStudentToDatabase(Student student) {
        String sql = "INSERT INTO student (student_id, student_name, student_email, student_telephone, student_password) VALUES (?, ?, ?, ?, ?)";
        MySqlConnect databaseLink= new MySqlConnect();
        try (Connection connection = databaseLink.getDatabaseLink();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            // Set parameters for the SQL query
            preparedStatement.setString(1, student.getStudentId());
            preparedStatement.setString(2, student.getName());
            preparedStatement.setString(3, student.getEmail());
            preparedStatement.setString(4, student.getMobileNumber());
            preparedStatement.setString(5, student.getPassword());
            // Execute the SQL query
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //This will be used to update the table when user changes account details account management page
    public void updateStudentInDatabase(Student updatedStudent) {
        String sql = "UPDATE student SET student_name = ?, student_email = ?, student_telephone = ?, student_password = ? WHERE student_id = ?";
        MySqlConnect databaseLink= new MySqlConnect();
        try (Connection connection = databaseLink.getDatabaseLink();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            // Set parameters for the SQL query
            preparedStatement.setString(1, updatedStudent.getName());
            preparedStatement.setString(2, updatedStudent.getEmail());
            preparedStatement.setString(3, updatedStudent.getMobileNumber());
            preparedStatement.setString(4, updatedStudent.getPassword());
            preparedStatement.setString(5, updatedStudent.getStudentId());
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
    // this method will be used to load data of a certain user using just the student_id of him
    public Student loadStudentData(String studentID){
        Student student=null;
        String sql="SELECT * FROM student WHERE BINARY student_id = ?";
        MySqlConnect databaseLink= new MySqlConnect();

        try (Connection connection = databaseLink.getDatabaseLink();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, studentID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String studentiD=resultSet.getString("student_id");
                    String studentName=resultSet.getString("student_name");
                    String studentEmail=resultSet.getString("student_email");
                    String studentTele=resultSet.getString("student_telephone");
                    String studentPassword=resultSet.getString("student_password");
                    student=new Student(studentName,studentEmail,studentTele,studentPassword);
                    student.setStudentId(studentID);
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
            // Handle database connection or query errors
        }
        return student;
    }



}
