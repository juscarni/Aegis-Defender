package org.aegisdefender.controller;

import org.aegisdefender.config.GameConfig;

import org.aegisdefender.model.Entities.Enemies.Enemy;
import org.aegisdefender.model.GameModel;
import org.aegisdefender.model.GameObserver;
import org.aegisdefender.model.Projectiles.Projectile;

import org.aegisdefender.view.GameFrame;

import org.aegisdefender.dto.EnemyRenderData;
import org.aegisdefender.dto.PlayerRenderData;
import org.aegisdefender.dto.ProjectileRenderData;

import javax.swing.Timer;

import java.awt.Robot;
import java.awt.Insets;
import java.awt.Point;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

public class GameController extends MouseAdapter implements ActionListener , GameObserver {

    private GameFrame gameFrame;
    private GameModel gameModel;
    private Timer gameLoop;

    public GameController(GameFrame gameFrame, GameModel gameModel) {
        this.gameFrame = gameFrame;
        this.gameModel = gameModel;

        this.gameFrame.addMouseMotionListener(this);
        this.gameFrame.addMouseListener(this);

        //
        setAllScores(this.gameModel.getPlayerData());
    }

    public void startGameLoop(){
        gameLoop = new Timer(1000/ GameConfig.FPS , this);
        gameLoop.start();
        setAllScores(this.gameModel.getPlayerData()); // read-all if we restart the game.
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // Récupère la position relative au GamePanel
        int mouseX = e.getX();
        int mouseY = e.getY();

        // Clamp sur les vraies limites du joueur
        int newX = Math.clamp(mouseX,
                gameModel.getPLAYER_MIN_X(),
                gameModel.getPLAYER_MAX_X());

        int newY = Math.clamp(mouseY,
                gameModel.getPLAYER_MIN_Y(),
                gameModel.getPLAYER_MAX_Y());

        gameModel.setPlayerX(newX);
        gameModel.setPlayerY(newY);

    }

    @Override
    public void mousePressed(MouseEvent e) {
        switch (e.getButton()) {
            case MouseEvent.BUTTON1 -> {
               //this.gameModel.spawnPlayerProjectile(); // create a projectile
            }
            case MouseEvent.BUTTON2 -> {
                // --
            }
            case MouseEvent.BUTTON3 -> {
                this.gameModel.setShieldActivated();
            }
            default -> System.out.println("Something goes wrong");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        this.gameModel.updateProjectiles();

        this.gameModel.updateEnemy();
        updateEnemies(this.gameModel.getActiveEnemies()); //....

        // I have to see this ambiguity later
        this.gameModel.spawnPlayerProjectile(); // create a projectile
        this.gameModel.updateEnemyProjectiles();

        updateEnemyProjectiles(this.gameModel.getEnemyProjectiles());
        this.gameModel.checkCollision(); // this method checks all collision on the game

        this.gameFrame.getGamePanel().updateImpactPoints(this.gameModel.impactPointOnPlayerAttackEnemy());

        // display the information on the gameOverPanel when the player dies
        this.gameFrame
                .getMainMenuPanel()
                .getGameOverPanel()
                .setStats(this.gameModel.getScore(),
                          this.gameModel.getWaveIndex(),
                          this.gameModel.getKills(),
                          this.gameModel.getBestScore());

        // display Score , wave and Best Score on the gamePanel
        this.gameFrame.getGamePanel().setScore(this.gameModel.getScore());
        this.gameFrame.getGamePanel().setWave(this.gameModel.getWaveIndex());
        this.gameFrame.getGamePanel().setBest(this.gameModel.getBestScore());

        if(!this.gameModel.isPlayerAlive()){
            this.gameLoop.stop();

            this.gameModel.setPlayerData(
                            getUsername(),
                            this.gameModel.getScore(),
                            this.gameModel.getKills(),
                            LocalDateTime.now()
            );
            this.gameModel.savePlayerData();
            setAllScores(this.gameModel.getPlayerData());//

            calibrateMouse(this.gameFrame);//--
        }
    }

    /******************************************************************************************************************#
     #************************* PLAYER : UPDATE PLAYER AND PLAYER-PROJECTILES ON THE SCREEN ***************************#
     #******************************************************************************************************************/

    @Override
    public void updatePlayer(int x, int y, int width, int height) {
        PlayerRenderData playerRenderData = new PlayerRenderData(
                x,
                y,
                width,
                height,
                gameModel.playerHitBox(),
                gameModel.getHealthBar(),
                gameModel.getPlayerCurrentHealth(),
                gameModel.isInCoolDown(),
                gameModel.isPlayerAlive()
        );
        gameFrame.getGamePanel().updatePlayerPosition(playerRenderData); // problem....
    }

    @Override
    public void updateProjectiles(List<Projectile> projectiles) {
        List<ProjectileRenderData> data = new ArrayList<>();
        for(Projectile projectile : projectiles){
            data.add(new ProjectileRenderData(
                 projectile.getLaserX(),
                 projectile.getLaserY(),
                 projectile.getLaserWidth(),
                 projectile.getLaserHeight(),
                 projectile.getProjectileHitBox(),   // --- just for the debug
                 null // we don't need projectile.getType() here.
            ));
        }
        gameFrame.getGamePanel().updatePlayerProjectilesOnScreen(data);
    }

    /******************************************************************************************************************#
     #******************************** ENEMIES : UPDATE ENEMIES AND PROJECTILES ON THE SCREEN *************************#
     #************************************** USE OF DATA-TRANSFERT-OBJECT (DTO)****************************************#
     #******************************************************************************************************************/

    public void updateEnemies(List<Enemy> enemies){
        List<EnemyRenderData> data = new ArrayList<>();
        for(Enemy enemy : enemies){
            data.add(new EnemyRenderData(
                    enemy.getEnemyX(),
                    enemy.getEnemyY(),
                    enemy.getWidth(),
                    enemy.getHeight(),
                    enemy.getHitBox(),  // --- just for the debug
                    enemy.getType(),
                    enemy.getHealthBar(),
                    enemy.getCurrentHealthBar(),
                    enemy.isExploding() // so that to know if the enemy is exploding
            ));
        }
        this.gameFrame.getGamePanel().updateEnemiesOnScreen(data);
    }

    public void updateEnemyProjectiles(List<List<Projectile>> projectiles){
        List<List<ProjectileRenderData>> data = new ArrayList<>(projectiles.size());

        for (List<Projectile> EnemyProjectiles : projectiles) {
            List<ProjectileRenderData> d = new ArrayList<>(EnemyProjectiles.size());
            for (Projectile p : EnemyProjectiles) {
                d.add(new ProjectileRenderData(
                        p.getLaserX(),
                        p.getLaserY(),
                        p.getLaserWidth(),
                        p.getLaserHeight(),
                        p.getProjectileHitBox(),
                        p.getType()
                ));
            }
            data.add(d);
        }
        this.gameFrame.getGamePanel().updateEnemiesProjectileOnScreen(data);
    }

    public String getUsername(){
        return this.gameFrame.getMainMenuPanel().getUsernamePanel().getUsername();
    }

    public void calibrateMouse(GameFrame gameFrame) {
        try {
            Robot robot = new Robot();
            Point windowPosition = gameFrame.getLocationOnScreen();
            Insets insets = gameFrame.getInsets();

            robot.mouseMove(
                    windowPosition.x + insets.left  + this.gameModel.getPlayerX(),
                    windowPosition.y + insets.top   + this.gameModel.getPlayerY()
            );
        } catch (Exception e) {
            System.err.println("Failed to calibrate mouse : " + e.getMessage());
        }
    }

    public void setAllScores(List<String[]> allScores){
        this.gameFrame.getMainMenuPanel().setAllScore(allScores);
    }

}
