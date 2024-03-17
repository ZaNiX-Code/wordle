package Backend;

import java.sql.*;

public class Benutzer {

    private static WordleDB wordleDB;
    private static Connection connection;
    private static Statement statement;

    private int bid;
    private String busername;
    public int bscore;

    static{
        try {

            wordleDB = WordleDB.getInstance();
            connection = wordleDB.getConnection();
            statement = connection.createStatement();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }





    public Benutzer(int bid){

        getDataById(bid);



    }


    public int getBid() {
        return bid;
    }



    public String getBusername() {
        return busername;
    }

    public void getDataById(int bid){


        try {
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT bid, busername, bscore FROM benutzer " +
                            "WHERE bid = ?"
            );
            ps.setInt(1,bid);
            ResultSet rs = ps.executeQuery();
            rs.next();

            this.bid = rs.getInt("bid");
            this.busername = rs.getString("busername");
            this.bscore = rs.getInt("bscore");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }




    public ResultSet getBenutzerTableData(){
        try {
            return statement.executeQuery(
                    "SELECT * FROM benutzer"
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean register(String username, String psw){

        if (username.trim().isBlank() || psw.isBlank()){
            return false;
        }

        try {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO benutzer(busername, bpsw)" +
                            "VALUES(?, ?)"
            );

            ps.setString(1, username);
            ps.setString(2, psw);

            ps.execute();


            return true;
        } catch (SQLException e){
            e.getErrorCode();
        }

        return false;

    }

}
