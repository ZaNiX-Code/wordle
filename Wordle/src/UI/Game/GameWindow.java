package UI.Game;

import Backend.Benutzer;

import javax.swing.*;

public class GameWindow extends JFrame {

    public final int SCREEN_WIDTH = 500;
    public final int SCREEN_HEIGHT = 450;
    private Benutzer benutzer;

    private GamePanel gamePanel;

    private JMenuBar menuBar = new JMenuBar();
    private JMenu viewMenu = new JMenu("view");
    private JMenuItem playMenu = new JMenuItem("Play");


    public GameWindow(Benutzer benutzer) {
        this.benutzer = benutzer;
        gamePanel = new GamePanel(this);

        viewMenu.add(playMenu);
        menuBar.add(viewMenu);
        setJMenuBar(menuBar);


        setContentPane(gamePanel);


        setTitle("Wordle");
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    public Benutzer getBenutzer() {
        return benutzer;
    }

    public void switchTo(JPanel wantedPanel) {
        setContentPane(wantedPanel);
        revalidate();
        repaint();
    }
}
