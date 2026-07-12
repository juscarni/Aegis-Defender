package org.aegisdefender.Controller;

import org.aegisdefender.Config.GameConfig;

import org.aegisdefender.Config.UIConfig;
import org.aegisdefender.Model.Entities.Enemies.Enemy;
import org.aegisdefender.Model.GameModel;
import org.aegisdefender.Model.GameObserver;
import org.aegisdefender.Model.Projectiles.Projectile;

import org.aegisdefender.View.EnemyRenderData;
import org.aegisdefender.View.GameFrame;
import org.aegisdefender.View.PlayerRenderData;
import org.aegisdefender.View.ProjectileRenderData;

import javax.swing.*;
import java.awt.*;
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

        gameLoop = new Timer(1000/ GameConfig.FPS , this);
        gameLoop.start();

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        super.mouseMoved(e);
        this.gameModel.setPLayerX(e.getX());
        this.gameModel.setPlayerY(e.getY());
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
    }//

    public void setMousePosition(GameFrame gameFrame, int x, int y) {
        try {
            Robot robot = new Robot();
            Point windowPosition = gameFrame.getLocationOnScreen();
            Insets insets = gameFrame.getInsets();

            int minX = windowPosition.x + insets.left;
            int minY = windowPosition.y + insets.top;
            int maxX = minX + UIConfig.WINDOW_WIDTH  - insets.right;
            int maxY = minY + UIConfig.WINDOW_HEIGHT - insets.bottom;

            Point mouse = MouseInfo.getPointerInfo().getLocation();

            if(mouse.x < minX || mouse.x > maxX || mouse.y < minY || mouse.y > maxY) {
                robot.mouseMove(
                        windowPosition.x + UIConfig.WINDOW_WIDTH  / 2,
                        windowPosition.y + UIConfig.WINDOW_HEIGHT / 2
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // à voir demain
        setMousePosition(gameFrame,
                Math.clamp(this.gameModel.getPlayerX(), this.gameModel.getPLAYER_MIN_X(), this.gameModel.getPLAYER_MAX_X()),
                Math.clamp(this.gameModel.getPlayerY(), this.gameModel.getPLAYER_MIN_Y(), this.gameModel.getPLAYER_MAX_Y())
        );

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
                          this.gameModel.getScore());

        // display Score , wave and Best Score on the gamePanel
        this.gameFrame.getGamePanel().setScore(this.gameModel.getScore());
        this.gameFrame.getGamePanel().setWave(this.gameModel.getWaveIndex());
        this.gameFrame.getGamePanel().setBest(52030); // this will be updated later

        if(!this.gameModel.isPlayerAlive()){
            this.gameLoop.stop();

            this.gameModel.setPlayerData(
                            getUsername(),
                            this.gameModel.getScore(),
                            this.gameModel.getKills(),
                            LocalDateTime.now()
            );
            this.gameModel.getInfos(); //--ok
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
}
