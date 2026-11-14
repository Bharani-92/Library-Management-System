import java.sql.*;
public class Database {
        private  static final String URL = "jdbc:mysql://localhost:3306/college";
        private  static final String USER = "root";
        static Connection connect() throws SQLException{
            String password = System.getenv("LI_DB_PASSWORD");
            if(password == null || password.isEmpty()){
                throw new IllegalStateException("Please set LI_DB_PASSWORD");
            }
            return DriverManager.getConnection(URL,USER,password);
        }
}

