package utils;

import java.sql.Connection;
import java.sql.DriverManager;


public class MySqlConnect {
    public Connection databaseLink;
    public Connection getDatabaseLink(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            databaseLink = DriverManager.getConnection("jdbc:mysql://localhost/SCAMS","root","#Wolf8me");
          
        }catch(Exception e){
            e.printStackTrace();
        }
        return databaseLink;
    }
}
