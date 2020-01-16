package M06_MySql;

import com.mysql.cj.x.protobuf.MysqlxCrud;

import java.sql.*;

public class NbaDatabase {
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/NBA_BBDD";

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


            try {
                System.out.println( "\n ++++++++++++++++  CONSULTA 1   ++++++++++++++++++++++++++++++++++\n");
                PreparedStatement stms = conn.prepareStatement("SELECT Procedencia, posicion FROM jugadores WHERE Nombre_equipo = ?;");

                stms.setString(1,"Cavaliers");
                ResultSet rs = stms.executeQuery();
                while (rs.next()) {
                    System.out.println("Nombre: " + rs.getString("Procedencia") + "  Posición: " +rs.getString("Posicion"));
                }
                stms.close();

                System.out.println( "\n ++++++++++++++++  CONSULTA 2   ++++++++++++++++++++++++++++++++++\n");
                PreparedStatement stms2 = conn.prepareStatement("SELECT count(*) AS contador FROM jugadores WHERE procedencia = ?;");
                stms2.setString(1,"Spain");
                rs = stms2.executeQuery();
                while (rs.next()) {
                    System.out.println("Número de Jugadores: " + rs.getString("contador")  );
                }
                stms2.close();

                System.out.println( "\n ++++++++++++++++  CONSULTA 3   ++++++++++++++++++++++++++++++++++\n");
                PreparedStatement stms3 = null;



                String insertsql="INSERT INTO jugadores ( Codigo, Nombre, Procedencia, Altura, Peso, Posicion, Nombre_equipo) VALUES(?,?,?,?,?,?,?)";
                stms3 = conn.prepareStatement(insertsql);

                stms3.setInt(1, 666);
                stms3.setString(2, " Luka Dončić");
                stms3.setString(3, "Slovenia");
                stms3.setString(4, "6-7");
                stms3.setInt(5, 230);

                stms3.setString(6, "G-F");
                stms3.setString(7, "Mavericks");
                stms3.executeUpdate();

                System.out.println("Insertado correctamente");

                System.out.println("datos insertados successfully...");

                stms.close();
            } catch (SQLException sqle) {
                System.out.println("Error en la ejecución: "
                        + sqle.getErrorCode() + " " + sqle.getMessage());
            }
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
}
//rs = stms.executeQuery("SELECT * FROM jugadores WHERE Nombre = ?;");