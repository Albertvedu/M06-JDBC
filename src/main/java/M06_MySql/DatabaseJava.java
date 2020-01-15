package M06_MySql;
import java.sql.*;

public class DatabaseJava {
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/";

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "badalona1965";

    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;
        try{
            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            //STEP 4: Execute a query
            System.out.println("Creating database...");
            stmt = conn.createStatement();

            String sql = "CREATE DATABASE Films";
            stmt.executeUpdate(sql);
            System.out.println("Database created successfully...");
            System.out.println("Creating tablas...");
            stmt.executeUpdate("USE Films");

            try {
                PreparedStatement stms = null;
                stms = conn.prepareStatement("USE Films");
                stms = conn.prepareStatement("CREATE TABLE film (title VARCHAR(50) PRIMARY KEY,produced VARCHAR(12), director VARCHAR(30), country VARCHAR(30))");
                stms.execute();
                stms.close();
            } catch (SQLException sqle) {
                System.out.println("Error en la ejecución: "
                        + sqle.getErrorCode() + " " + sqle.getMessage());
            }
            System.out.println("Table created successfully...");
        }catch(SQLException se){
            //Handle errors for JDBC
            System.out.println("La Base de datos ya existe");
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println("Goodbye!");
    }//end main
}//end JDBCExample