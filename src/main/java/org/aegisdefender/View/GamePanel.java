package org.aegisdefender.View;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    // Game parameters Player...
    private final int  ROWS  = 18;
    private final int COLS = 14;
    private final int TILES = 40;
    private final int Width =  COLS * TILES;// 560px
    private final int Height = ROWS * TILES;; // 720px
    // it will not be initialized here
    private int PlayerXInitial = -12;
    private int PlayerYInitial = Height - 372 ;//Height - TILES*3;
    private Image playerImage = null;


    public GamePanel(){
        this.setBackground(Color.black);
        this.setPreferredSize(new Dimension(Width,Height));
        playerImage = new ImageIcon(getClass().getResource("/Images/AegisDefender_shield_transition_actif.png")).getImage();
    }
     @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(playerImage,PlayerXInitial, PlayerYInitial,TILES*2,TILES*2,null);
       drawPanel(g);
    }

    // gridGame a delete.
    public void drawPanel(Graphics g){
        g.setColor(Color.gray);
        int y = 0;
        int x = 0;
        for(int i = 0; i <= this.ROWS; i++){
            y = i * (getHeight()/ROWS);
            if(i==this.ROWS){
                y = getHeight()-1;
            }
            g.drawLine(0,y,getWidth()-1,y);
        }//
        for(int i = 0; i <= this.COLS; i++){
            x = i * (getWidth()/COLS);
            if(i==this.COLS){
                x = getWidth()-1;
            }
            g.drawLine(x,0,x,getHeight()-1);
        }
    }
}
