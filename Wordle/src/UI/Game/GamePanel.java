package UI.Game;

import Backend.Benutzer;
import Backend.WordleDB;
import UI.CharRect;
import UI.State;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


import javax.swing.JTextField;
import javax.swing.text.*;

public class GamePanel extends JPanel implements ActionListener {

    GameWindow gameWindow;
    JTextField[] charFields = new JTextField[5];
    JButton checkButton = new JButton("Check");
    JButton newButton = new JButton("New");
    int fieldSize = 50;
    WordleDB wordleDB;
    String rightWord;
    JLabel scoreLabel = new JLabel("Score: ");
    int focusedField = 0;

    ArrayList<CharRect>[] guessedWords = new ArrayList[6];

    public GamePanel(GameWindow gameWindow) {
        this.gameWindow = gameWindow;

        try {
            wordleDB = WordleDB.getInstance();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        getWordFromDB();


        initComponents();
        setLayout(null);
        setPreferredSize(new Dimension(gameWindow.SCREEN_WIDTH, gameWindow.SCREEN_HEIGHT));

    }


    private void initComponents() {


        checkButton.setBounds(gameWindow.SCREEN_WIDTH / 2 - (charFields.length * fieldSize) / 2 + (5 * fieldSize), 50, fieldSize * 2, fieldSize);
        checkButton.addActionListener(this);
        add(checkButton);


        newButton.setBounds(gameWindow.SCREEN_WIDTH / 2 - (charFields.length * fieldSize) / 2 - (2 * fieldSize), 50, fieldSize * 2, fieldSize);
        newButton.addActionListener(this);
        newButton.setFocusable(false);
        add(newButton);



        for (int i = 0; i < charFields.length; i++) {
            charFields[i] = new JTextField(1);
            charFields[i].setBounds(gameWindow.SCREEN_WIDTH / 2 - (charFields.length * fieldSize) / 2 + (i * fieldSize), 50, fieldSize, fieldSize);
            charFields[i].setHorizontalAlignment(JTextField.CENTER);

            charFields[i].getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    SwingUtilities.invokeLater(() -> filter(e.getDocument()));
                }
                @Override
                public void removeUpdate(DocumentEvent e) {
                    
                }
                @Override
                public void changedUpdate(DocumentEvent e){}

                private void filter(Document document){
                    SwingUtilities.invokeLater(() -> {
                        repaint();

                        if (document.getLength() > 1) {
                            try {
                                document.remove(0,1);
                            } catch (BadLocationException e) {
                                throw new RuntimeException(e);
                            }
                        }

                        if(focusedField < 4){
                            charFields[++focusedField].requestFocus();
                        }else{
                            focusedField = 0;
                            charFields[focusedField].requestFocus();
                        }

                        System.out.println(focusedField);
                    });
                }
            });

            add(charFields[i]);
        }


        scoreLabel.setBounds(charFields[0].getX(), charFields[0].getY() - 20, 400, 20);
        updateScoreLabel();
        add(scoreLabel);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        for (int y = 0; y < guessedWords.length; y++) {
            if (guessedWords[y] == null) break;
            for (int x = 0; x < guessedWords[y].size(); x++) {
                CharRect charRect = guessedWords[y].get(x);
                charRect.x = gameWindow.SCREEN_WIDTH / 2 - (charFields.length * fieldSize) / 2 + (x * fieldSize);
                charRect.y = 100 + (fieldSize * y);

                g2.setColor(charRect.color);
                g2.fill(charRect);
                g2.setColor(Color.BLACK);
                g2.draw(charRect);
                g2.drawString(String.valueOf(charRect.c), charRect.x + fieldSize / 2, charRect.y + fieldSize / 2);
            }
        }

    }


    @Override
    public void actionPerformed(ActionEvent e) {


        if (e.getSource() == checkButton) {
            String word = "";

            if (guessedWords[guessedWords.length - 1] != null) return;
            for (JTextField cf : charFields) {
                if (cf.getText().isBlank()) return;
                word += cf.getText().trim();
            }

            char[] guessChars = word.toCharArray();
            char[] rightChars = rightWord.toCharArray();

            ArrayList<CharRect> charRects = new ArrayList<>();
            for (int i = 0; i < guessChars.length; i++) {
                CharRect charRect = new CharRect(guessChars[i]);
                charRects.add(charRect);
            }


            // Sets charRects color
            int greens = 0;
            for (int i = 0; i < charRects.size(); i++) {
                for (int j = 0; j < rightChars.length; j++) {
                    if (charRects.get(i).color != Color.green) {
                        if (Character.toUpperCase(charRects.get(i).c) == Character.toUpperCase(rightChars[j])) {
                            charRects.get(i).color = Color.yellow;
                            if (i == j) {
                                charRects.get(i).color = Color.green;
                                greens++;
                            }
                        }
                    }
                }
            }


            for (int i = 0; i < guessedWords.length; i++) {
                if (guessedWords[i] == null) {
                    guessedWords[i] = charRects;
                    break;
                }
            }


            if (greens == 5) {
                // WIN
                checkButton.setEnabled(false);
                increamentScore();
                updateScoreLabel();

            } else if (guessedWords[guessedWords.length-1] != null) {
                System.out.println("Word was: " + rightWord);
            }

            for (JTextField cf : charFields) {
                cf.setText("");
            }
            focusedField = 0;
            charFields[focusedField].requestFocus();

        }


        if (e.getSource() == newButton) {
            getWordFromDB();

            guessedWords = new ArrayList[6];

            for (JTextField charField : charFields) {
                charField.setText("");
            }

            checkButton.setEnabled(true);
        }

        repaint();

    }


    public void getWordFromDB() {


        int wordId = new Random().nextInt(1500) + 1;

        try {
            PreparedStatement ps = wordleDB.getConnection().prepareStatement(
                    "SELECT wort FROM woerter " +
                            "WHERE wid = ?"
            );
            ps.setInt(1, wordId);
            ResultSet rs = ps.executeQuery();
            rs.next();

            rightWord = rs.getString("wort");


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }


    private void increamentScore() {
        gameWindow.getBenutzer().bscore++;
        try {
            PreparedStatement ps = wordleDB.getConnection().prepareStatement(
                    "UPDATE benutzer " +
                            "SET bscore = ? " +
                            "WHERE bid = ?"
            );
            ps.setInt(2, gameWindow.getBenutzer().getBid());
            ps.setInt(1, gameWindow.getBenutzer().bscore);

            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void updateScoreLabel() {
        String scoreLabelText = "Score: " + gameWindow.getBenutzer().bscore;
        scoreLabel.setText(scoreLabelText);
    }


}
