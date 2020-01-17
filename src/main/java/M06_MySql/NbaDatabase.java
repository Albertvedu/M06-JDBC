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
    private ResultSet rs;
    Connection conn = null;

    public static void main(String[] args) {

        Statement stmt = null;
        NbaDatabase nba = new NbaDatabase();
        try{
            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            nba.conn = DriverManager.getConnection(DB_URL, USER, PASS);


            try {
                nba.consulta1();
                nba.consulta2();
                nba.consulta3();
                nba.consulta4();
                nba.consulta5();
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
                if(nba.conn!=null)
                    nba.conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println("Goodbye!");
    }
    void consulta1() throws SQLException {
        System.out.println( "\n ++++++++++++++++  CONSULTA 1   ++++++++++++++++++++++++++++++++++\n");
        PreparedStatement stms = conn.prepareStatement("SELECT Procedencia, posicion FROM jugadores WHERE Nombre_equipo = ?;");

        stms.setString(1,"Cavaliers");
        rs = stms.executeQuery();
        while (rs.next()) {
            System.out.println("Nombre: " + rs.getString("Procedencia") + "                           Posición: " +rs.getString("Posicion"));
        }
        stms.close();
    }

    void consulta2() throws SQLException {
        System.out.println( "\n ++++++++++++++++  CONSULTA 2   ++++++++++++++++++++++++++++++++++\n");
        PreparedStatement stms2 = conn.prepareStatement("SELECT count(*) AS contador FROM jugadores WHERE procedencia = ?;");
        stms2.setString(1,"Spain");
        rs = stms2.executeQuery();
        while (rs.next()) {
            System.out.println("Número de Jugadores: " + rs.getString("contador")  );
        }
        stms2.close();
    }

    void consulta3() throws SQLException {
        System.out.println( "\n ++++++++++++++++  CONSULTA 3   ++++++++++++++++++++++++++++++++++\n");
        PreparedStatement stms3b = conn.prepareStatement("SELECT * FROM jugadores WHERE codigo = ?;");
        stms3b.setInt(1, 666);
        rs = stms3b.executeQuery();

        if (!rs.next()){
            PreparedStatement stms3 = null;

            String insertsql = "INSERT INTO jugadores ( Codigo, Nombre, Procedencia, Altura, Peso, Posicion, Nombre_equipo) VALUES(?,?,?,?,?,?,?)";
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
        }else{
            System.out.println("Este jugador ya existe...");
        }
        stms3b.close();

    }

    void consulta4() throws SQLException {
        System.out.println( "\n ++++++++++++++++  CONSULTA 4   ++++++++++++++++++++++++++++++++++\n");
        PreparedStatement stms4 = conn.prepareStatement("SELECT j.Nombre, e.temporada, e.puntos_por_partido FROM jugadores AS j JOIN estadisticas AS e ON j.codigo = e.jugador WHERE temporada =? and Puntos_por_partido > ?;");
        stms4.setString(1,"04/05");
        stms4.setInt(2, 10);
        rs = stms4.executeQuery();
        while (rs.next()) {
            System.out.println("Nombre Jugador: " + rs.getString("Nombre") +
                    "       Temporada: " + rs.getString("temporada") +
                    "       puntuación: " + rs.getString("Puntos_por_partido") );
        }
        stms4.close();

    }
    void consulta5() throws SQLException {
        System.out.println( "\n ++++++++++++++++  CONSULTA 5   ++++++++++++++++++++++++++++++++++\n");
        PreparedStatement stms5 = conn.prepareStatement("SELECT * FROM partidos  WHERE temporada = ?");
        stms5.setString(1, "05/06");

        //     AQUI ARA FAREM LA CONSULTA USAN EL CODIG
        rs = stms5.executeQuery();
        int contador = 0;

        while (rs.next()) {

            if (rs.getString("equipo_visitante").equals("Warriors") && (rs.getInt("puntos_visitante") - rs.getInt("puntos_local") > 15)) {
                contador++;
            } else if (rs.getString("equipo_local").equals("Warriors") && (rs.getInt("puntos_local") - rs.getInt("puntos_visitante") > 15)) {
                contador++;
            }
        }
        System.out.println("Partidos en que los Warriors ganan por más de 15 puntos: " + contador + "\n-----------------");
        rs.close();
        stms5.close();
    }
}
