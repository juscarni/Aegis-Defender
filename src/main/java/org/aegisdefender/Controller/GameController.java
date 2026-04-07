package org.aegisdefender.Controller;

import org.aegisdefender.Config.GameConfig;
import org.aegisdefender.Config.UIConfig;
import org.aegisdefender.Model.GameModel;
import org.aegisdefender.Model.GameObserver;
import org.aegisdefender.Model.Projectiles.Projectile;
import org.aegisdefender.View.EntityRenderData;
import org.aegisdefender.View.GameFrame;

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
    private Timer projectilleTimer;

    public GameController(GameFrame gameFrame, GameModel gameModel) {
        this.gameFrame = gameFrame;
        this.gameModel = gameModel;

        this.gameFrame.addMouseMotionListener(this);
        this.gameFrame.addMouseListener(this);

        setMousePosition(gameFrame, this.gameModel.getPlayerX(), this.gameModel.getPlayerY());

        projectilleTimer = new Timer(1000/ GameConfig.FPS , this);
        projectilleTimer.start();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        super.mouseMoved(e);
        this.gameModel.setPLayerX(e.getX());
        this.gameModel.setPlayerY(e.getY());
    }//

    @Override
    public void mousePressed(MouseEvent e) {
        switch (e.getButton()) {
            case MouseEvent.BUTTON1 -> {
               this.gameModel.setPlayerProjectile(UIConfig.TILES, GameConfig.LASER_OFFSET_PLAYERX, GameConfig.LASER_OFFSET_PLAYERY);
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
    }

    @Override
    public void updatePlayerPosition(int x, int y) {
        gameFrame.getGamePanel().updatePlayerPosition(x,y);
    }

    @Override
    // use of Data-transfert-Object (DTO)
    public void updateProjectiles(List<Projectile> projectiles) {
        List<EntityRenderData> data = new ArrayList<>();
        for(Projectile projectile : projectiles){
            data.add(new EntityRenderData(
                 projectile.getPositionLaserX(),
                 projectile.getPositionLaserY(),
                 projectile.getLaserWidth(),
                 projectile.getLaserHeight(),
                 projectile.getHitBox()   // ---
            ));
        }
        gameFrame.getGamePanel().updatePlayerProjectiles(data);
    }
}
