package utils;

import java.sql.Connection;
import java.sql.DriverManager;


public class MySqlConnect {
    public Connection databaseLink;
    public Connection getDatabaseLink(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            //databaseLink = DriverManager.getConnection("jdbc:mysql://localhost/SACMS","root","#Wolf8me");
            // Include character set and collation settings in the connection URL
            String url = "jdbc:mysql://localhost/SACMS?useUnicode=true&characterEncoding=UTF-8&useSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC";
            databaseLink = DriverManager.getConnection(url, "root", "#Wolf8me");
          
        }catch(Exception e){
            e.printStackTrace();
        }
        return databaseLink;
    }
}
