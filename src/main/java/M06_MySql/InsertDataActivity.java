package M06_MySql;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static M06_MySql.UserHandlerPlats.getListPelis;


public class InsertDataActivity {  // CON SAX LEER XML, CREAR DATABASE Y TABLAS

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/";
    static final String USER = "root";
    static final String PASS = "badalona1965";


    public static void main(String[] args) throws SQLException {

        Connection connection = null;
        Statement stmt = null;

        leerXml();
        connection = connectionDataBase(connection, stmt);
        crearDatabase(connection, stmt);
        crearTablas(connection, stmt);
        hacerQuerys(connection);

    }
    static void leerXml(){
        try{
            List<Pelis> pelis;
            File imputFile=new File("films.xml");
            SAXParserFactory factory=SAXParserFactory.newInstance();
            SAXParser saxParser=factory.newSAXParser();
            UserHandlerPlats usehandler=new UserHandlerPlats();
            saxParser.parse(imputFile,usehandler);

        }catch(Exception e){
            e.printStackTrace();
        }
    }
    static Connection  connectionDataBase(Connection connection, Statement stmt){

        try{
            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            connection = DriverManager.getConnection(DB_URL, USER, PASS);


        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }
        return connection;
    }

    static void crearDatabase(Connection connection, Statement stmt) throws SQLException {
        try {
            //STEP 4: Execute a query
            System.out.println("Creating database...");
            stmt = connection.createStatement();
            String sql = "DROP DATABASE Films";
            stmt.executeUpdate(sql);
            String sql2 = "CREATE DATABASE Films";
            stmt.executeUpdate(sql2);
            System.out.println("Database created successfully...");
            System.out.println("Creating tablas...");
            stmt.executeUpdate("USE Films");

        }catch(SQLException se){
            //Handle errors for JDBC
            System.out.println("La Base de datos ya existe");
        }
    }
    static void crearTablas(Connection connection, Statement stmt) throws SQLException {
        try {
            PreparedStatement stms = null;
            stms = connection.prepareStatement("CREATE TABLE Film (title VARCHAR(50) PRIMARY KEY,produced VARCHAR(12), director VARCHAR(30), country VARCHAR(30))");

            stms.execute();
        } catch (SQLException sqle) {
            System.out.println("Error en la ejecución: "
                    + sqle.getErrorCode() + " " + sqle.getMessage());
        }
        System.out.println("Table created successfully...");
    }
    static void hacerQuerys(Connection connection) throws SQLException {
        PreparedStatement stmt = null;
        List<Pelis> pelisList = getListPelis();
        try {

            String insertsql="INSERT INTO Film ( title, produced, director, country) VALUES(?,?,?,?)";
            stmt = connection.prepareStatement(insertsql);

            for (Pelis pelis: pelisList){
                stmt.clearParameters();
                stmt.setString(1, pelis.getTitol());
                stmt.setString(2, pelis.getProducida());
                stmt.setString(3, pelis.getDirector());
                stmt.setString(4, pelis.getCountry());
                stmt.executeUpdate();
            }
            System.out.println("Insertado correctamente");

            stmt.close();
            System.out.println("datos insertados successfully...");
        } catch (SQLException sqle) {
            System.out.println("Error al insertar datos: "
                    + sqle.getErrorCode() + " " + sqle.getMessage());
        }finally{
            //finally block used to close resources
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                if(connection!=null)
                    connection.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
        System.out.println("Goodbye!");

    }
}



class UserHandlerPlats extends DefaultHandler {

    boolean produced = false;
    boolean title = false;
    boolean director = false;
    boolean country = false;
    private Pelis actualPeli = new Pelis();
    private static List<Pelis> pelisList = new ArrayList<>();

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        //super.startElement(uri, localName, qName, attributes);

        //TODO atributes  String nomatribut = atributes.getValue("nomatribut");
        // TODO sout "atributo : " + nomatributvariable);
        if (qName.equalsIgnoreCase("Film")) {
            String producido = attributes.getValue("produced");
            actualPeli.setProducida(producido);
        }else
        if (qName.equalsIgnoreCase("Title")){
            title = true;
        }else if (qName.equalsIgnoreCase("Director")){
            director = true;
        }else if (qName.equalsIgnoreCase("Country")){
            country = true;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        //super.endElement(uri, localName, qName);

        if (qName.equalsIgnoreCase("Film")){
            pelisList.add(actualPeli);
            actualPeli = new Pelis();
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        //super.characters(ch, start, length);
        if (title) {
            actualPeli.setTitol(new String(ch, start, length));
            title = false;
        }else
        if (director){
            actualPeli.setDirector(new String(ch, start, length));
            director = false;
        }else  if (country){
            actualPeli.setCountry(new String(ch, start, length));
            country = false;
        }
    }
    public static List<Pelis> getListPelis() {
        return pelisList;
    }
}

class Pelis {

    private String titol, producida, director, country;

    public String getTitol() {
        return titol;
    }

    public void setTitol(String titol) {
        this.titol = titol;
    }

    public String getProducida() {
        return producida;
    }

    public void setProducida(String producida) {
        this.producida = producida;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

}