package UI.Access;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AccessFrame extends JFrame implements ActionListener {

    public final int SCREEN_WIDTH = 450;
    public final int SCREEN_HEIGHT = 270;
    private RegisterPanel registerPanel = new RegisterPanel(this);
    private LoginPanel loginPanel = new LoginPanel(this);


    private JMenuBar menuBar = new JMenuBar();
    private JMenu view = new JMenu("View");
    private JMenuItem loginMenu = new JMenuItem("Login");
    private JMenuItem registerMenu = new JMenuItem("Register");


    public AccessFrame(){

        loginMenu.addActionListener(this);
        registerMenu.addActionListener(this);

        view.add(loginMenu);
        view.add(registerMenu);
        menuBar.add(view);


        setFocusable(true);
        requestFocus();

        setJMenuBar(menuBar);

        setTitle("Wordle Login/Register");
        switchTo(loginPanel);
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);

    }


    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource() == loginMenu) {
            switchTo(loginPanel);
        }

        if(e.getSource() == registerMenu){
            switchTo(registerPanel);
        }


    }


    public void switchTo(JPanel wantedPanel){
        setContentPane(wantedPanel);
        revalidate();
        repaint();
    }


}
