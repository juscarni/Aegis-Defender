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
    private int playerX;
    private int playerY;
    private int playerWidth;
    private int playerHeight;

    private Image playerImage;
    private Image backgroundImage;
    private Image playerLaser;
    private Image kamikaze;
    private Image posamine;

    private Timer backgroundImageTimer;

    private List<ProjectileRenderData> playerProjectiles;
    private List<ProjectileRenderData> enemyProjectiles;
    private List<EnemyRenderData> enemies;

    public GamePanel(){
        this.setBackground(Color.black);
        this.setPreferredSize(new Dimension(UIConfig.WINDOW_WIDTH, UIConfig.WINDOW_HEIGHT));

        loadGameImages();

        backgroundImageTimer = new Timer(1000/ GameConfig.FPS, this);
        backgroundImageTimer.start();

        this.playerProjectiles = new ArrayList<>();
        this.enemies = new ArrayList<>(); ///
        this.enemyProjectiles = new ArrayList<>();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Y_SCROLL += 1;
        repaint();
    }

     @Override
    public void paintComponent(Graphics g){
         super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        drawBackgroundImage(g2);// backgroundImageScroll
         drawProjectiles(g2);
         drawEnemies(g2);
         drawEnemiesProjectiles(g2);
        drawPlayerImage(g2);
        drawPanel(g2);
    }

    public void drawPanel(Graphics2D g){
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

    private void drawPlayerImage(Graphics2D g) {
        g.drawImage(playerImage,
                playerX - UIConfig.DRAW_OFFSET_X,
                playerY - UIConfig.DRAW_OFFSET_Y,
                this.playerWidth,
                this.playerHeight, null);
    }

    private void drawBackgroundImage(Graphics2D g) {
        g.drawImage(backgroundImage, 0, Y_SCROLL, UIConfig.WINDOW_WIDTH, UIConfig.WINDOW_HEIGHT, null);
        g.drawImage(backgroundImage, 0, Y_SCROLL - UIConfig.WINDOW_HEIGHT, UIConfig.WINDOW_WIDTH, UIConfig.WINDOW_HEIGHT, null);
        if (Y_SCROLL >= UIConfig.WINDOW_HEIGHT) {
            Y_SCROLL = 0;
        }
    }

    private void drawProjectiles(Graphics2D g) {
        //projectile of the player draw
        g.setColor(Color.white);
       for(ProjectileRenderData projectile : this.playerProjectiles){
           g.drawImage(playerLaser,
                   projectile.x(),
                   projectile.y(),
                   projectile.width(),
                   projectile.height(),
                   null
           );
           // projectiles HitBox draw
           g.setColor(Color.RED);
           g.drawRect(projectile.hitbox().x, projectile.hitbox().y, projectile.hitbox().width, projectile.hitbox().height);
       }
    }

    private void drawEnemies(Graphics2D g) {
        // draw Enemies
        for(EnemyRenderData enemy : this.enemies){
            switch(enemy.type()){
                case "KAMIKAZE" ->  {
                    g.drawImage(kamikaze, enemy.x(), enemy.y(), enemy.width(), enemy.height(), null);
                    // Enemies HitBox draw
                    g.setColor(Color.RED);
                    g.drawRect(enemy.hitbox().x, enemy.hitbox().y, enemy.hitbox().width, enemy.hitbox().height);
                }
                case "POSAMINE" -> {
                    g.drawImage(posamine, enemy.x(), enemy.y(), enemy.width(), enemy.height(),null);
                    // Enemies HitBox draw
                    g.setColor(Color.RED);
                    g.drawRect(enemy.hitbox().x, enemy.hitbox().y, enemy.hitbox().width, enemy.hitbox().height);
                }
            }
        }
    }
    private void drawEnemiesProjectiles(Graphics2D g){
        for(ProjectileRenderData projectile : this.enemyProjectiles){
            switch (projectile.type()){
                case "POSAMINE" -> {
                   // g.drawImage(playerLaser, projectile.x(), projectile.y(), projectile.width(),projectile.height(),null);
                   // hitbox will be put here later
                }
                case "BERSEKER" -> {
                    // ---
                }
            }
        }
    }

    public void updatePlayerPosition(int x, int y, int width , int height) {
        this.playerX = x;
        this.playerY = y;
        this.playerWidth = width;
        this.playerHeight = height;
        repaint();
    }

    public void updatePlayerProjectilesOnScreen(List<ProjectileRenderData> projectiles){
        this.playerProjectiles = projectiles;
        repaint();
    }

    public void updateEnemiesOnScreen(List<EnemyRenderData> enemies){
        this.enemies = enemies;
        repaint();
    }
    public void updateEnemiesProjectileOnScreen(List<ProjectileRenderData> projectiles){
        this.enemyProjectiles = projectiles;
        repaint();
    }

    public void loadGameImages(){
        playerImage = new ImageIcon(getClass().getResource("/Images/AegisDefender.png")).getImage();
        backgroundImage = new ImageIcon(getClass().getResource("/Images/Background.png")).getImage();
        playerLaser  = new ImageIcon(getClass().getResource("/Images/Laser_Large.png")).getImage();
        kamikaze = new ImageIcon(getClass().getResource("/Images/Kamikaze_idle.png")).getImage();
        posamine = new ImageIcon(getClass().getResource("/Images/Artillery_Cruiser_Idel.png")).getImage();
    }
}
