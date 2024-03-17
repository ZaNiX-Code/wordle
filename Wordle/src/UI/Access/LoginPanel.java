package UI.Access;

import Backend.Benutzer;
import Backend.WordleDB;
import UI.Game.GameWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginPanel extends JPanel implements ActionListener {

    private JTextField username_JTF;
    private JPasswordField password_JTF;
    private JButton login_JBtn;
    private AccessFrame accessFrame;


    private WordleDB wordleDB;
    private Connection connection;
    private Statement statement;


    public LoginPanel(AccessFrame accessFrame){


        try {
            wordleDB = WordleDB.getInstance();
            connection = wordleDB.getConnection();
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        this.accessFrame = accessFrame;

        setLayout(null);
        initComponents();
        setPreferredSize(new Dimension(accessFrame.SCREEN_WIDTH, accessFrame.SCREEN_HEIGHT));

    }



    private void initComponents(){

        // Username field config
        JLabel userLabel = new JLabel("Username:");
        userLabel.setSize(300,30);
        userLabel.setLocation(75,20);

        username_JTF = new JTextField();
        username_JTF.setSize(300,30);
        username_JTF.setLocation(75,50);


        // Password field config
        JLabel passLabel = new JLabel("Password:");
        passLabel.setSize(300,30);
        passLabel.setLocation(75,80);

        password_JTF = new JPasswordField();
        password_JTF.setSize(300,30);
        password_JTF.setLocation(75,110);


        login_JBtn = new JButton("Login");
        login_JBtn.setSize(300,30);
        login_JBtn.setLocation(75,160);


        password_JTF.addActionListener(this);
        username_JTF.addActionListener(this);
        login_JBtn.addActionListener(this);



        add(userLabel);
        add(passLabel);

        add(username_JTF);
        add(password_JTF);
        add(login_JBtn);





    }


    @Override
    public void actionPerformed(ActionEvent e) {


        if (e.getSource() == login_JBtn){

            String username = username_JTF.getText().trim();
            String psw = "";
            for (char c : password_JTF.getPassword()) psw += c;



            try {
                PreparedStatement ps = connection.prepareStatement(
                        "SELECT bid, busername, bpsw FROM benutzer " +
                                "WHERE busername LIKE ? AND bpsw LIKE ?"
                );


                ps.setString(1, username);
                ps.setString(2,psw);

                ResultSet rs = ps.executeQuery();

                int rowCount = 0;
                int bid = 0;
                while(rs.next()) {
                    bid = rs.getInt("bid");
                    rowCount++;
                }


                if(rowCount == 1){
                    // TODO: log in
                    System.out.println("LOGGED");
                    accessFrame.dispose();
                    new GameWindow(new Benutzer(bid));
                }else{
                    System.out.println("Wrong inputs");
                }

            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }



        }

    }
}
