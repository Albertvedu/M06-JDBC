package M.app;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class prueba {
    public prueba (){};

    public Connection getConnection( ){

        Connection connection = null;

        try{
            connection = DriverManager.getConnection("jdbc:mysql://192.168.1.132:3306", "root", "badalona1965");
            System.out.println("BBDD Conectado");

        }catch (SQLException e){
            System.out.println("error");

        }return connection;

    }

    public static void main(String[] args) {

        prueba bdConnection = new prueba();
        Connection connection = bdConnection.getConnection();

    }

}
