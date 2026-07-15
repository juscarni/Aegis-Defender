package org.aegisdefender.View;

import org.aegisdefender.Config.UIConfig;
import org.aegisdefender.DTO.EnemyRenderData;
import org.aegisdefender.DTO.PlayerRenderData;
import org.aegisdefender.DTO.ProjectileRenderData;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class GamePanel extends StarBackgroundPanel{

    private int Y_SCROLL = 0; //
    private List<Point> impactPoints;
    private PlayerRenderData playerRenderData;

    // scores on the screen
    private int score = 0;
    private int wave  = 0;
    private int best  = 0;

    private Image playerImage;
    private Image backgroundImage;

    private Image kamikaze;
    private Image posamine;
    private Image artiliere;
    private Image berseker;

    private Image kamikazeExplosion;
    private Image posamineExplosion;
    private Image artiliereExplosion;
    private Image explosionImage;
    private Image playerInCoolDown;
    private Image bersekerExplosion;

    private Image posamine_projectile;
    private Image artiliere_projectile;
    private Image playerLaser;

    private Timer backgroundImageTimer;

    private List<ProjectileRenderData> playerProjectiles;
    private List<List<ProjectileRenderData>> enemyProjectiles;
    private List<EnemyRenderData> enemies;

    public GamePanel(){
        //this.setBackground(Color.black);
        this.setPreferredSize(new Dimension(UIConfig.WINDOW_WIDTH, UIConfig.WINDOW_HEIGHT));

        loadGameImages();

       // backgroundImageTimer = new Timer(1000/ GameConfig.FPS, this);
        // backgroundImageTimer.start();

        this.playerProjectiles = new ArrayList<>();
        this.enemies = new ArrayList<>(); ///
        this.enemyProjectiles = new ArrayList<>();

        impactPoints = new ArrayList<>();
        this.setCursor(setCursorInvisible());// get an invisible cursor
    }

     @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //drawBackgroundImage(g2);// backgroundImageScroll
         drawProjectiles(g2);
         drawEnemies(g2);
         drawEnemiesProjectiles(g2);
         drawImpactPoints(g2); // --
         drawPlayerImage(g2);
        //drawPanel(g2);
         drawScore(g2);
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
        // if the playerRenderData is null we don't execute the rest
        if(playerRenderData == null) return;
        if(!playerRenderData.isPlayerAlive()){
            clearScreen();
            GameFrame.getInstance().getMainMenuPanel().showGameOverPanel(); //
            return;
        }

        Image player = playerImage;
        if(playerRenderData.isInCoolDown()){
            player = playerInCoolDown;
        }

        g.drawImage(player,
                this.playerRenderData.x() - UIConfig.DRAW_OFFSET_X,
                this.playerRenderData.y() - UIConfig.DRAW_OFFSET_Y,
                this.playerRenderData.width(),
                this.playerRenderData.height(), null);

        // player hitbox
       /* g.setColor(Color.red);
        g.drawRect(
                this.playerRenderData.hitbox().x,
                this.playerRenderData.hitbox().y,
                this.playerRenderData.hitbox().width,
                this.playerRenderData.hitbox().height
        );*/

        //player healthBar on the screen
        drawHealthBar(g,
                5,//this.playerRenderData.healthBar().x,
                UIConfig.WINDOW_HEIGHT - 35,//this.playerRenderData.healthBar().y,
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
           //g.setColor(Color.RED);
           //g.drawRect(projectile.hitbox().x, projectile.hitbox().y, projectile.hitbox().width, projectile.hitbox().height);
       }
    }

    private void drawEnemies(Graphics2D g) {
        for(EnemyRenderData enemy : this.enemies){
            Image enemyImage = getImage(enemy);
            // draw enemy on the screen
            g.drawImage(enemyImage, enemy.x(), enemy.y(), enemy.width(),enemy.height(),null);
            // draw enemy hitbox

            //g.setColor(Color.RED);
            //g.drawRect(enemy.hitbox().x, enemy.hitbox().y, enemy.hitbox().width, enemy.hitbox().height);
            // draw enemy healthBar
            drawHealthBar(
                    g,
                    enemy.healthBar().x,
                    enemy.healthBar().y,
                    enemy.healthBar().width,
                    enemy.healthBar().height,
                    enemy.currentHealth()); //------
            // draw enemy explosionImage
            if(enemy.isExploding()){
                g.drawImage(explosionImage, enemy.x(), enemy.y(), enemy.width(), enemy.height(),null );
            }
        }

    }
    // this method very important so that to extract an image ....
    private Image getImage(EnemyRenderData enemy) {
        Image enemyImage = null;
        this.explosionImage = null;

        switch(enemy.type()){
            case "KAMIKAZE" ->  {
                enemyImage = kamikaze;
                if(enemy.isExploding())
                    this.explosionImage = kamikazeExplosion;
            }
            case "POSAMINE" -> {
                enemyImage = posamine;
                if(enemy.isExploding()){
                    this.explosionImage = posamineExplosion;
                }
            }
            case "ARTILIERE" -> {
                enemyImage = artiliere;
                if(enemy.isExploding()){
                    this.explosionImage = artiliereExplosion;
                }
            }
            case "BERSERKER" -> {
                enemyImage = berseker;
                System.out.println("berserker");
                if(enemy.isExploding()){
                    //this.explosionImage = artiliereExplosion;
                }
            }
        }
        return enemyImage;
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

                /*g.setColor(Color.red);
                g.drawRect(p.hitbox().x, p.hitbox().y, p.hitbox().width, p.hitbox().height);*/
            }
        }
    }
    // ImpactPoint on the screen
    private void drawImpactPoints(Graphics2D g2) {
        g2.setColor(Color.MAGENTA);
        int r = 6; // raggio cerchio

        if(impactPoints == null ) {
            System.out.println("empty");
            return;
        }

        for (Point p : impactPoints) {
            // impact point
            g2.drawOval(p.x - r, p.y - r, r * 2, r * 2);
            g2.drawLine(p.x - r, p.y, p.x + r, p.y);
            g2.drawLine(p.x, p.y - r, p.x, p.y + r);
        }
    }

    public void updatePlayerPosition(PlayerRenderData playerData) {
        this.playerRenderData = playerData;
        repaint();
    }

    // Player on the screen
    public void updatePlayerProjectilesOnScreen(List<ProjectileRenderData> projectiles){
        this.playerProjectiles = (projectiles == null) ? null : new ArrayList<>(projectiles);
        repaint();
    }
    // Enemies on the screen
    public void updateEnemiesOnScreen(List<EnemyRenderData> enemies){
        this.enemies = (enemies == null) ? null : new  ArrayList<>(enemies);
        repaint();
    }
    // Enemies projectiles
    public void updateEnemiesProjectileOnScreen(List<List<ProjectileRenderData>> projectiles){
        this.enemyProjectiles = (projectiles == null) ? null : new ArrayList<>(projectiles);
        repaint();
    }
    // ImpactPoint on player projectile with enemy
    public void updateImpactPoints(List<Point> points) {
        this.impactPoints = (points == null) ? null : new ArrayList<>(points);
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
        berseker =  new ImageIcon(getClass().getResource("/Images/Berserker_Idel.png")).getImage();
        kamikazeExplosion = new ImageIcon(getClass().getResource("/Images/Kamikaze_Explosion.png")).getImage();
        posamineExplosion = new ImageIcon(getClass().getResource("/Images/posamine_explosion(1).png")).getImage();
        artiliereExplosion = new ImageIcon(getClass().getResource("/Images/artiliere_explosion.png")).getImage();
        playerInCoolDown = new ImageIcon(getClass().getResource("/Images/player_cooldown.png")).getImage();
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

    public void setScore(int score) { this.score = score; repaint(); }
    public void setWave(int wave)   { this.wave  = wave;  repaint(); }
    public void setBest(int best)   { this.best  = best;  repaint(); }

    private void drawScore(Graphics2D g2) {
        Font labelFont = new Font("Arial", Font.PLAIN, 10);
        Font valueFont = new Font("Monospaced", Font.BOLD, 22);
        Font smallFont = new Font("Monospaced", Font.BOLD, 18);

        Color panelBg     = new Color(0, 0, 0, 115);
        Color cyanBorder  = new Color(0, 200, 255, 46);
        Color grayBorder  = new Color(255, 255, 255, 25);
        Color goldBorder  = new Color(250, 199, 117, 64);
        Color labelColor  = new Color(255, 255, 255, 100);
        Color cyanLabel   = new Color(0, 200, 255, 153);
        Color valueColor  = new Color(224, 247, 255);
        Color goldColor   = new Color(250, 199, 117);

        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // ── Score top-left ──────────────────────────────────────────
        String scoreStr = String.format("%07d", score);
        g2.setFont(valueFont);
        int scoreW = g2.getFontMetrics().stringWidth(scoreStr) + 32;

        drawHudPanel(g2, 14, 14, scoreW, 52, panelBg, cyanBorder);

        g2.setFont(labelFont);
        g2.setColor(cyanLabel);
        g2.drawString("SCORE", 26, 27);

        g2.setFont(valueFont);
        g2.setColor(valueColor);
        g2.drawString(scoreStr, 18, 52);

        // ── Wave top-right ───────────────────────────────────────────
        String waveStr = String.format("%02d", wave);
        int wx = UIConfig.WINDOW_WIDTH - 90;

        drawHudPanel(g2, wx, 14, 72, 52, panelBg, grayBorder);

        g2.setFont(labelFont);
        g2.setColor(labelColor);
        g2.drawString("WAVE", wx + 18, 27);

        g2.setFont(smallFont);
        g2.setColor(valueColor);
        g2.drawString(waveStr, wx + 22, 52);

        // ── Best score down-right ──────────────────────────────────────
        String bestStr = String.format("%07d", best);
        g2.setFont(new Font("Monospaced", Font.BOLD, 13));
        int bestW = g2.getFontMetrics().stringWidth(bestStr) + 60;
        int bx    = UIConfig.WINDOW_WIDTH  - bestW - 14;
        int by    = UIConfig.WINDOW_HEIGHT - 40;

        drawHudPanel(g2, bx, by, bestW, 28, panelBg, goldBorder);

        g2.setFont(labelFont);
        g2.setColor(new Color(250, 199, 117, 128));
        g2.drawString("BEST", bx + 10, by + 18);

        g2.setFont(new Font("Monospaced", Font.BOLD, 13));
        g2.setColor(goldColor);
        g2.drawString(bestStr, bx + bestW - g2.getFontMetrics().stringWidth(bestStr) - 10, by + 18);
    }

    private void drawHudPanel(Graphics2D g2, int x, int y, int w, int h, Color bg, Color border) {
        g2.setColor(bg);
        g2.fillRoundRect(x, y, w, h, 8, 8);
        g2.setColor(border);
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect(x, y, w, h, 8, 8);
    }

    public void clearScreen() {
        this.enemies = new ArrayList<>();
        this.playerProjectiles = new ArrayList<>();
        this.enemyProjectiles = new ArrayList<>();
        this.impactPoints = new ArrayList<>();
        this.playerRenderData = null;
        repaint();
    }

    public Cursor setCursorInvisible(){
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        BufferedImage image = new BufferedImage(UIConfig.CURSOR_IMG_SIZE, UIConfig.CURSOR_IMG_SIZE, BufferedImage.TYPE_INT_ARGB);
        return toolkit.createCustomCursor(
                image,
                new Point(UIConfig.HOTSPOT_X,UIConfig.HOTSPOT_Y),
                UIConfig.CURSOR_NAME
        );
    }
}
