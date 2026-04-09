package org.aegisdefender.View;

import org.aegisdefender.Config.GameConfig;
import org.aegisdefender.Config.UIConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class GamePanel extends JPanel implements ActionListener{

    private int Y_SCROLL = 0;
    private int PlayerX;
    private int PlayerY;

    private Image playerImage;
    private Image backgroundImage;
    private Image playerLaser;
    private Image kamikaze;

    private Timer backgroundImageTimer;

    private List<EntityRenderData> playerProjectiles;
    private List<EntityRenderData> enemies;


    public GamePanel(){
        this.setBackground(Color.black);
        this.setPreferredSize(new Dimension(UIConfig.WINDOW_WIDTH, UIConfig.WINDOW_HEIGHT));

        loadGameImages();

        backgroundImageTimer = new Timer(1000/ GameConfig.FPS, this);
        backgroundImageTimer.start();

        this.playerProjectiles = new ArrayList<>();
        this.enemies = new ArrayList<>(); ///
    }

     @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        drawBackgroundImage(g);// backgroundImageScroll
         drawProjectiles(g);
         drawEnemies(g);
        drawPlayerImage(g);
        drawPanel(g);
    }
    public void drawPanel(Graphics g){
        g.setColor(Color.gray);
        int y = 0;
        int x = 0;
        for(int i = 0; i <= UIConfig.ROWS; i++){
            y = i * (getHeight()/UIConfig.ROWS);
            if(i == UIConfig.ROWS){
                y = getHeight()-1;
            }
            g.drawLine(0,y,getWidth()-1,y);
        }//
        for(int i = 0; i <= UIConfig.COLS; i++){
            x = i * (getWidth()/UIConfig.COLS);
            if(i == UIConfig.COLS){
                x = getWidth()-1;
            }
            g.drawLine(x,0,x,getHeight()-1);
        }
    }

    private void drawPlayerImage(Graphics g) {
        g.drawImage(playerImage,
                PlayerX - UIConfig.DRAW_OFFSET_X,
                PlayerY - UIConfig.DRAW_OFFSET_Y,
                UIConfig.TILES * 2,
                UIConfig.TILES * 2, null);
    }

    private void drawBackgroundImage(Graphics g) {
        g.drawImage(backgroundImage, 0, Y_SCROLL, UIConfig.WINDOW_WIDTH, UIConfig.WINDOW_HEIGHT, null);
        g.drawImage(backgroundImage, 0, Y_SCROLL - UIConfig.WINDOW_HEIGHT, UIConfig.WINDOW_WIDTH, UIConfig.WINDOW_HEIGHT, null);
        if (Y_SCROLL >= UIConfig.WINDOW_HEIGHT) {
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
       for(EntityRenderData projectile : this.playerProjectiles){
           g.drawImage(playerLaser,
                   projectile.x,
                   projectile.y,
                   projectile.width,
                   projectile.height,
                   null
           );
           // HitBox draw
           g.setColor(Color.RED);
           g.drawRect(projectile.hitbox.x, projectile.hitbox.y, projectile.hitbox.width, projectile.hitbox.height);
       }
    }

    public void updatePlayerPosition(int x, int y) {
        this.PlayerX = x;
        this.PlayerY = y;
        repaint();
    }
    public void updatePlayerProjectiles(List<EntityRenderData> projectiles){
        this.playerProjectiles = projectiles;
        repaint();
    }

    public void loadGameImages(){
        playerImage = new ImageIcon(getClass().getResource("/Images/AegisDefender.png")).getImage();
        backgroundImage = new ImageIcon(getClass().getResource("/Images/2.jpg")).getImage();
        playerLaser  = new ImageIcon(getClass().getResource("/Images/AegisDefender_Bullet.png")).getImage();
        kamikaze = new ImageIcon(getClass().getResource("/Images/Kamikaze_idle.png")).getImage();
    }
    public void updateEnemies(List<EntityRenderData> enemies){
        this.enemies = enemies;
        repaint();
    }

    private void drawEnemies(Graphics g) {
        for(EntityRenderData enemy : this.enemies){
            g.drawImage(kamikaze,
                    enemy.x,
                    enemy.y,
                    enemy.width,
                    enemy.height,
                    null
            );
            // HitBox draw
            g.setColor(Color.RED);
            g.drawRect(enemy.hitbox.x, enemy.hitbox.y, enemy.hitbox.width, enemy.hitbox.height);
        }
    }
}
