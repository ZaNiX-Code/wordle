package Backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class WordleDB {

    private static WordleDB wordleDB;
    private Connection connection;
    private String jdbcURL = "jdbc:postgresql://localhost:5432/wordle";
    private String user = "postgres";
    private String psw = "f7f7";


    private WordleDB(){
        try {
            connection = DriverManager.getConnection(jdbcURL,user,psw);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection(){
        return connection;
    }
    public static WordleDB getInstance() throws SQLException {

        if(wordleDB == null){
            wordleDB = new WordleDB();
        }else if(wordleDB.connection.isClosed()){
            wordleDB = new WordleDB();
        }

        return wordleDB;
    }







}
