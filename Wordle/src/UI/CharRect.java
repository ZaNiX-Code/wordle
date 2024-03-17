package UI;

import java.awt.*;

public class CharRect extends Rectangle {

    public Character c;
    public Color color;

    public CharRect(char c){
        setSize(50,50);
        this.c = c;
        color = Color.darkGray;
    }

}
