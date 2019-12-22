package M;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 *
 */
public class App 
{
    public static void main( String[] args ) throws SQLException, ClassNotFoundException {
        //el nombre del conector
        String m_sconector = "com.mysql/cj/jdbc/Driver";

        String m_surl = "jdbc:mysql://192.168.1.132:3306";


        String user = "root";

        String password = "badalona1965";


        Class.forName(m_sconector);

        Connection conexion;


        conexion = DriverManager.getConnection(m_surl, user, password);

        Statement sentencia;

        sentencia = conexion.createStatement();

        //sentencia.execute("insert into usuario values('0002','panfilo')");

        System.out.println("nos hemos conectados a la bd");

    }
}
