package utils;

import stake_holders.ClubAdvisor;
import stake_holders.Student;

import java.sql.*;

public class StudentDataHandling {

    public boolean studentLogin(String studentId, String password){
        boolean isAuthenticated = false;
        String sql = "SELECT * FROM student WHERE Student_id = ? AND Student_password = ?";

        MySqlConnect databaseLink= new MySqlConnect();

        try (Connection connection = databaseLink.getDatabaseLink();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, studentId);
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
            // Handle database connection or query errors
        }

        return userIdAlreadyExists;

    }

    public static void saveStudentToDatabase(Student student) {
        String sql = "INSERT INTO student (student_id, student_name, student_email, student_telephone., student_password) VALUES (?, ?, ?, ?, ?)";

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

            System.out.println("Student data saved successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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



}
