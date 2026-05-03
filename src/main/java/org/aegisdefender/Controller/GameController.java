package org.aegisdefender.Controller;

import org.aegisdefender.Config.GameConfig;
import org.aegisdefender.Model.Entities.Enemies.Enemy;
import org.aegisdefender.Model.GameModel;
import org.aegisdefender.Model.GameObserver;
import org.aegisdefender.Model.Projectiles.Projectile;
import org.aegisdefender.View.EnemyRenderData;
import org.aegisdefender.View.GameFrame;
import org.aegisdefender.View.ProjectileRenderData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

        setMousePosition(gameFrame, this.gameModel.getPlayerX(), this.gameModel.getPlayerY());

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
               this.gameModel.spawnPlayerProjectile(); // create a projectile
            }
            case MouseEvent.BUTTON2 -> {
               // to-do
            }
            case MouseEvent.BUTTON3 -> {
               //to-do
            }
            default -> System.out.println("Something goes wrong");
        }
    }//

    public void setMousePosition(GameFrame gameFrame, int x, int y) {
        try {
            Robot robot = new Robot();
            Point windowPosition = gameFrame.getLocationOnScreen(); // the window must be visible so that it works
            int targetX = windowPosition.x + x;
            int targetY = windowPosition.y + y;
            robot.mouseMove(targetX, targetY); // move the mouse in the target position x and y
        } catch (Exception e) {
            e.printStackTrace();
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
    }

    /******************************************************************************************************************#
     #************************* PLAYER : UPDATE PLAYER AND PLAYER-PROJECTILES ON THE SCREEN ***************************#
     #******************************************************************************************************************/

    @Override
    public void updatePlayer(int x, int y,int width, int height) {
        gameFrame.getGamePanel().updatePlayerPosition(x,y,width,height); // problem....
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
                    enemy.getType()
            ));
        }
        this.gameFrame.getGamePanel().updateEnemiesOnScreen(data);
    }

    public void updateEnemyProjectiles(List<List<Projectile>> projectiles){
        List<ProjectileRenderData> data = new ArrayList<>();
        for(List<Projectile> projectile : projectiles){
            for(Projectile p : projectile){
                data.add(new ProjectileRenderData(
                        p.getLaserX(),
                        p.getLaserY(),
                        p.getLaserWidth(),
                        p.getLaserHeight(),
                        p.getProjectileHitBox(),
                        p.getType()
                ));
            }
        }
        this.gameFrame.getGamePanel().updateEnemiesProjectileOnScreen(data);
    }
}
