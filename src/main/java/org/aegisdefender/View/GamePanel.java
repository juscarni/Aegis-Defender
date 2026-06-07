package org.aegisdefender.View;

import org.aegisdefender.Config.GameConfig;
import org.aegisdefender.Config.UIConfig;

import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.Timer;

import java.awt.Image;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.RenderingHints;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class GamePanel extends JPanel implements ActionListener{

    private int Y_SCROLL = 0;
    private PlayerRenderData playerRenderData;

    private Image playerImage;
    private Image backgroundImage;

    private Image kamikaze;
    private Image posamine;
    private Image artiliere;

    private Image posamine_projectile;
    private Image artiliere_projectile;
    private Image playerLaser;

    private Timer backgroundImageTimer;

    private List<ProjectileRenderData> playerProjectiles;
    private List<List<ProjectileRenderData>> enemyProjectiles;
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
                this.playerRenderData.x() - UIConfig.DRAW_OFFSET_X,
                this.playerRenderData.y() - UIConfig.DRAW_OFFSET_Y,
                this.playerRenderData.width(),
                this.playerRenderData.height(), null);

        // player hitbox
        g.setColor(Color.red);
        g.drawRect(
                this.playerRenderData.hitbox().x,
                this.playerRenderData.hitbox().y,
                this.playerRenderData.hitbox().width,
                this.playerRenderData.hitbox().height
        );

        //player healthBar on the screen
        drawHealthBar(g,
                this.playerRenderData.healthBar().x,
                this.playerRenderData.healthBar().y,
                this.playerRenderData.healthBar().width,
                this.playerRenderData.healthBar().height,
                this.playerRenderData.currentHealth()
        );
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
        for(EnemyRenderData enemy : this.enemies){
            Image enemyImage = null;
            switch(enemy.type()){
                case "KAMIKAZE" ->  {
                    enemyImage = kamikaze;
                }
                case "POSAMINE" -> {
                    enemyImage = posamine;
                }
                case "ARTILIERE" -> {
                    enemyImage = artiliere;
                }
            }
            // draw enemy on the screen
            g.drawImage(enemyImage, enemy.x(), enemy.y(), enemy.width(),enemy.height(),null);
            // draw enemy hitbox
            g.setColor(Color.RED);
            g.drawRect(enemy.hitbox().x, enemy.hitbox().y, enemy.hitbox().width, enemy.hitbox().height);
            // draw enemy healthBar
            drawHealthBar(
                    g,
                    enemy.healthBar().x,
                    enemy.healthBar().y,
                    enemy.healthBar().width,
                    enemy.healthBar().height,
                    enemy.currentHealth()); //------
        }
    }
    private void drawEnemiesProjectiles(Graphics2D g){
        for(List<ProjectileRenderData> projectile : this.enemyProjectiles) {
            Image enemyProjectile = null;
            for(ProjectileRenderData p : projectile){
                String type = p.type();
                if(type == null){
                    continue;
                }
                switch (p.type().toUpperCase()){
                    case "KAMIKAZE" -> {
                       continue; // because a kamikaze doesn't have projectiles (this problem will be fixed at the end)
                    }
                    case "POSAMINE" -> {
                        enemyProjectile = this.posamine_projectile;
                    }
                    case "ARTILIERE" -> {
                        enemyProjectile = this.artiliere_projectile;
                    }
                }
                // draw artiliere
                g.drawImage(enemyProjectile, p.x(), p.y(), p.width(), p.height(),null);
                // draw artiliere hitbox
                g.setColor(Color.red);
                g.drawRect(p.hitbox().x, p.hitbox().y, p.hitbox().width, p.hitbox().height);
            }
        }
    }

    public void updatePlayerPosition(PlayerRenderData playerData) {
        this.playerRenderData = playerData;
        repaint();
    }

    // Player on the screen
    public void updatePlayerProjectilesOnScreen(List<ProjectileRenderData> projectiles){
        this.playerProjectiles = projectiles;
        repaint();
    }
    // Enemies on the screen
    public void updateEnemiesOnScreen(List<EnemyRenderData> enemies){
        this.enemies = enemies;
        repaint();
    }
    // Enemies projectiles
    public void updateEnemiesProjectileOnScreen(List<List<ProjectileRenderData>> projectiles){
        this.enemyProjectiles = projectiles;
        repaint();
    }

    public void loadGameImages(){
        //
        playerImage = new ImageIcon(getClass().getResource("/Images/AegisDefender.png")).getImage();
        backgroundImage = new ImageIcon(getClass().getResource("/Images/Background.png")).getImage();

        // Player and enemies projectiles
        playerLaser  = new ImageIcon(getClass().getResource("/Images/Laser_Large.png")).getImage();
        posamine_projectile = new ImageIcon(getClass().getResource("/Images/posamine_laser.png")).getImage();
        artiliere_projectile = new ImageIcon(getClass().getResource("/Images/artiliere_projectile.png")).getImage(); // --

        // Enemies
        kamikaze = new ImageIcon(getClass().getResource("/Images/Kamikaze_idle.png")).getImage();
        posamine = new ImageIcon(getClass().getResource("/Images/Artillery_Cruiser_Idel.png")).getImage();
        artiliere = new ImageIcon(getClass().getResource("/Images/Artillery_Cruiser_Idel (1).png")).getImage(); //
    }


    public void drawHealthBar(Graphics g, int x, int y, int width, int height, int currentHealth){
        g.setColor(Color.RED);
        g.fillRect(x, y, width, height);

        // current health
        g.setColor(Color.GREEN);
        g.fillRect(x,y ,currentHealth, height);

        //current health border
        g.setColor(Color.black);
        g.drawRect(x, y, width, height);
    }
}
