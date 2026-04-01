package org.aegisdefender.View;

import org.aegisdefender.Model.Projectiles.PlayerLaser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class GamePanel extends JPanel implements ActionListener{

    public final int TILES = 40;
    public final int LASER_OFFSET_PLAYERX = 7;
    public final int LASER_OFFSET_PLAYERY = TILES - 20;
    public final  int FPS = 30;

    private final int ROWS  = 18;
    private final int COLS = 14;

    private final int SCREEN_WIDTH =  COLS * TILES;// 560px
    private final int SCREEN_HEIGHT = ROWS * TILES;; // 720px

    private final int DRAW_OFFSET_X = 45;
    private final int DRAW_OFFSET_Y = 65;


    private static int Y_SCROLL = 0;

    private int PlayerX;
    private int PlayerY;

    private Image playerImage = null;
    private Image backgroundImage = null;
    private Timer backgroundImageTimer = null;

    //playerlaser...
    private List<PlayerLaser> laserList;


    public GamePanel(){
        this.setBackground(Color.black);
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        playerImage = new ImageIcon(getClass().getResource("/Images/AegisDefender_shield_transition_actif.png")).getImage();
        backgroundImage = new ImageIcon(getClass().getResource("/Images/2.jpg")).getImage();
        backgroundImageTimer = new Timer(1000/FPS, this);
        this.laserList = new ArrayList<>();
        backgroundImageTimer.start();
    }

     @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        drawBackgroundImage(g); // backgroundImageScroll
         drawProjectiles(g);
        drawPlayerImage(g); // player image
        drawPanel(g);
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
        g.drawImage(backgroundImage, 0, Y_SCROLL - SCREEN_HEIGHT, SCREEN_WIDTH, SCREEN_HEIGHT, null);
        if (Y_SCROLL >= SCREEN_HEIGHT) {
            Y_SCROLL = 0;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Y_SCROLL += 1;
        repaint();
    }

    private void drawProjectiles(Graphics g) {
        //playerlaser
        g.setColor(Color.white);
       for(PlayerLaser laser : this.laserList){
           g.fillRect(
                   laser.getPositionLaserX(),
                   laser.getPositionLaserY(),
                   laser.getLaserWidth(),
                   laser.getLaserHeight()
           );
       }

    }
    public void setPlayerLasers(List<PlayerLaser>  projectiles) {
        this.laserList = projectiles;
    }
}
