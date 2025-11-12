package org.aegisdefender.View;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {

    public static final int ROWS  = 18;
    public static final int COLS = 14;
    public static final int TILES = 40;

    public static final int SCREEN_WIDTH =  COLS * TILES;// 560px
    public static final int SCREEN_HEIGHT = ROWS * TILES;; // 720px

    private final int DRAW_OFFSET_X = 45;
    private final int DRAW_OFFSET_Y = 65;

    private int PlayerX;
    private int PlayerY;

    private Image playerImage = null;

    public GamePanel(){
        this.setBackground(Color.black);
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        playerImage = new ImageIcon(getClass().getResource("/Images/AegisDefender_shield_transition_actif.png")).getImage();

    }

     @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        drawPlayerImage(g); // player image
        drawPanel(g); // gridGame delete will be deleted at the end of the game.
    }

    public void drawPanel(Graphics g){
        g.setColor(Color.gray);
        int y = 0;
        int x = 0;
        for(int i = 0; i <= ROWS; i++){
            y = i * (getHeight()/ROWS);
            if(i == ROWS){
                y = getHeight()-1;
            }
            g.drawLine(0,y,getWidth()-1,y);
        }//
        for(int i = 0; i <= COLS; i++){
            x = i * (getWidth()/COLS);
            if(i == COLS){
                x = getWidth()-1;
            }
            g.drawLine(x,0,x,getHeight()-1);
        }
    }
    public void setPlayerPositionX(int x){
        this.PlayerX = x;
    }
    public void setPlayerPositionY(int y){
        this.PlayerY = y;
    }

    public int getPlayerX(){
        return this.PlayerX;
    }

    public int getPlayerY(){
        return this.PlayerY;
    }
    private void drawPlayerImage(Graphics g){
        g.drawImage(playerImage,
                getPlayerX() - DRAW_OFFSET_X,
                getPlayerY() - DRAW_OFFSET_Y,
                TILES*2,
                TILES*2,null);
        repaint();
    }
}
