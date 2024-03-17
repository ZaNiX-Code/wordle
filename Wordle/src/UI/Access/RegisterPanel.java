package UI.Access;

import Backend.Benutzer;
import Backend.WordleDB;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class RegisterPanel extends JPanel implements ActionListener {


    private JTextField username_JTF;
    private JPasswordField password_JTF;
    private JButton regiter_JBtn;
    AccessFrame accessFrame;
    private WordleDB wordleDB;
    private Connection connection;
    private Statement statement;



    public RegisterPanel(AccessFrame accessFrame){

        try {
            wordleDB = WordleDB.getInstance();
            connection = wordleDB.getConnection();
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        setLayout(null);

        this.accessFrame = accessFrame;
        setPreferredSize(new Dimension(accessFrame.SCREEN_WIDTH, accessFrame.SCREEN_HEIGHT));

        initComponents();

    }







    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource() == regiter_JBtn){

            String username = username_JTF.getText().trim();
            String psw = "";
            for (char c : password_JTF.getPassword()) psw += c;


            if((!username.isBlank() || !psw.isBlank()) && Benutzer.register(username, psw)) {
                System.out.println("You are registered");
            }else{
                System.out.println("Failed to register");
            }

        }

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


        regiter_JBtn = new JButton("Register");
        regiter_JBtn.setSize(300,30);
        regiter_JBtn.setLocation(75,160);


        password_JTF.addActionListener(this);
        username_JTF.addActionListener(this);
        regiter_JBtn.addActionListener(this);



        add(userLabel);
        add(passLabel);

        add(username_JTF);
        add(password_JTF);
        add(regiter_JBtn);



    }






}
