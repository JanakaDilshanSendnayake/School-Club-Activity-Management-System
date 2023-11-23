package utils;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Class which is for initializing connection between program and database
 */
public class MySqlConnect {
    public Connection databaseLink;
    public Connection getDatabaseLink(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            databaseLink = DriverManager.getConnection("jdbc:mysql://localhost/SACMS","root","#Wolf8me");
          
        }catch(Exception e){
            e.printStackTrace();
        }
        return databaseLink;
    }
}
