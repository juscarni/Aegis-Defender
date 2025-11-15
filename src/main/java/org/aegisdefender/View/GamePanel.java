package org.aegisdefender.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GamePanel extends JPanel implements ActionListener{

    public static final int ROWS  = 18;
    public static final int COLS = 14;
    public static final int TILES = 40;

    public static final int SCREEN_WIDTH =  COLS * TILES;// 560px
    public static final int SCREEN_HEIGHT = ROWS * TILES;; // 720px

    private final int DRAW_OFFSET_X = 45;
    private final int DRAW_OFFSET_Y = 65;
    private static int Y_SCROLL = 0;
    private static int FPS = 60;

    private int PlayerX;
    private int PlayerY;

    private Image playerImage = null;
    private Image backgroundImage = null;
    private Timer backgroundImageTimer = null;


    public GamePanel(){
        this.setBackground(Color.black);
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        playerImage = new ImageIcon(getClass().getResource("/Images/AegisDefender_shield_transition_actif.png")).getImage();
        backgroundImage = new ImageIcon(getClass().getResource("/Images/2.jpg")).getImage();
        backgroundImageTimer = new Timer(1000/FPS, this);
        backgroundImageTimer.start();
    }

     @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        drawBackgroundImage(g); // backgroundImageScroll
        drawPlayerImage(g); // player image
        //drawPanel(g);
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
    private void drawPlayerImage(Graphics g) {
        g.drawImage(playerImage,
                getPlayerX() - DRAW_OFFSET_X,
                getPlayerY() - DRAW_OFFSET_Y,
                TILES * 2,
                TILES * 2, null);
    }

    private void drawBackgroundImage(Graphics g) {
        g.drawImage(backgroundImage, 0, Y_SCROLL, SCREEN_WIDTH, SCREEN_HEIGHT, null);
        g.drawImage(backgroundImage, 0, Y_SCROLL - SCREEN_HEIGHT + 1, SCREEN_WIDTH, SCREEN_HEIGHT, null);
        if (Y_SCROLL >= SCREEN_HEIGHT) {
            Y_SCROLL = 0;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Y_SCROLL += 1;
        repaint();
    }
}
