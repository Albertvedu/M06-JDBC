package M06_MySql;

import java.sql.*;
import java.util.List;
import java.util.Scanner;

public class PuigCastellar {
    Connection connection = null;
    static PreparedStatement stmt = null;

    public PuigCastellar(Connection connection, PreparedStatement stmt) {
        this.connection = connection;
        this.stmt = stmt;
    }

    public PuigCastellar() {
    }
    public Connection getConnection( ) throws SQLException {


        // Funciona en clase.
//        String sDriver = "com.mysql.cj.jdbc.Driver";

        // Funciona en casa
        //String sDriver = "com.mysql.jdbc.Driver";

        String sURL = "jdbc:mysql://192.168.22.101:3306/ElPuigCastellar";


            connection = DriverManager.getConnection(sURL, "root", "badalona1965");
            System.out.println("BBDD Conectado");




        return connection;


    }

    public static void insertarAlumne(Connection connection) throws SQLException {

        int id; int edad = 38;
        String nom, cognoms, institut;
        Scanner sc = new Scanner(System.in);

        System.out.print("id: ");
        id = sc.nextInt();sc.nextLine();
        System.out.print("Nom: ");
        nom = sc.nextLine();
        System.out.print("Cognoms: ");
        cognoms = sc.nextLine();
        System.out.print("Institut: ");
        institut = sc.nextLine();
        System.out.print("Edad: ");
        edad = sc.nextInt();

        stmt = connection.prepareStatement("INSERT INTO Alumnes VALUES (?,?,?,?,?)");
        stmt.setInt(1, id);
        stmt.setString(2, nom);
        stmt.setString(3, cognoms);
        stmt.setInt(4, edad);
        stmt.setString(5, institut);

        int retorno = stmt.executeUpdate();
        if (retorno>0)
            System.out.println("Insertado correctamente");

    }

    public static void listarTablas(Connection connection) throws SQLException {
        stmt = connection.prepareStatement("SELECT * FROM Alumnes ");
        ResultSet resultSet = stmt.executeQuery();

        while (resultSet.next()) {

            System.out.println("Nom: " + resultSet.getString("Nom"));

            System.out.println("Edad: " + resultSet.getInt("Edad"));
            System.out.println();
        }
        resultSet.close();

    }

    public static void listarBusqueda(Connection connection) throws SQLException {
        stmt = connection.prepareStatement("SELECT * FROM Alumnes WHERE Edad > 25 AND Edad < 48");
        ResultSet resultSet = stmt.executeQuery();

        System.out.println("\n\t\t Busqueda de mas de 25 y menos de 48 \n");
        System.out.println("\t-------------------------------");

        while (resultSet.next()) {
            System.out.println("Id: " + resultSet.getString("id"));
            System.out.println("Nom: " + resultSet.getString("Nom"));
            System.out.println("Cognoms: " + resultSet.getString("cognoms"));
            System.out.println("Edad: " + resultSet.getInt("Edad"));
            System.out.println();
        }
        resultSet.close();

    }

    public static void main(String[] args) throws SQLException {


        PuigCastellar bdConnection = new PuigCastellar();
        Connection connection = bdConnection.getConnection();

        try{

            insertarAlumne(connection);
            listarTablas(connection);
            listarBusqueda(connection);

        } catch (SQLException sqle) {
            System.out.println("SQLState: "
                    + sqle.getSQLState());
            System.out.println("SQLErrorCode: "
                    + sqle.getErrorCode());
            sqle.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    stmt.close();
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
