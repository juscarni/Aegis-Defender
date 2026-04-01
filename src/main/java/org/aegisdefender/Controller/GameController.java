package org.aegisdefender.Controller;

import org.aegisdefender.Model.GameModel;
import org.aegisdefender.View.GameFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameController extends MouseAdapter implements ActionListener {

    private GameFrame gameFrame = null;
    private GameModel gameModel = null;

    private final int TILES;
    private final int LASER_OFFSET_PLAYERX;
    private final int LASER_OFFSET_PLAYERY;
    private final int FPS;

    private Timer projectilleTimer = null;

    public GameController(GameFrame gameFrame, GameModel gameModel) {
        this.gameFrame = gameFrame;
        this.gameModel = gameModel;

        this.gameFrame.addMouseMotionListener(this);
        this.gameFrame.addMouseListener(this);
        setMousePosition(gameFrame, this.gameModel.getPlayerX(), this.gameModel.getPlayerY());

        this.gameFrame.gamePanelInstance().setPlayerPositionX(this.gameModel.getPlayerX());
        this.gameFrame.gamePanelInstance().setPlayerPositionY(this.gameModel.getPlayerY());

        //
        TILES = this.gameFrame.gamePanelInstance().TILES;
        LASER_OFFSET_PLAYERX = this.gameFrame.gamePanelInstance().LASER_OFFSET_PLAYERX;
        LASER_OFFSET_PLAYERY = this.gameFrame.gamePanelInstance().LASER_OFFSET_PLAYERY;
        FPS = this.gameFrame.gamePanelInstance().FPS;

        projectilleTimer = new Timer(1000/FPS , this);
        projectilleTimer.start();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        super.mouseMoved(e);
        this.gameModel.setPLayerX(e.getX());
        this.gameModel.setPlayerY(e.getY());
        this.gameFrame.gamePanelInstance().setPlayerPositionX(this.gameModel.getPlayerX());
        this.gameFrame.gamePanelInstance().setPlayerPositionY(this.gameModel.getPlayerY());
        this.gameFrame.gamePanelInstance().repaint();
        //System.out.println(this.gameModel.getPlayerX() + " , " + this.gameModel.getPlayerY());
    }//

    @Override
    public void mousePressed(MouseEvent e) {
        switch (e.getButton()) {
            case MouseEvent.BUTTON1 -> {
               this.gameModel.setPlayerProjectile(TILES, LASER_OFFSET_PLAYERX, LASER_OFFSET_PLAYERY);
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
        // update model
        this.gameModel.updateProjectiles();
        var projectiles = this.gameModel.getPlayerProjectile();
        this.gameFrame.gamePanelInstance().setPlayerLasers(projectiles);
        this.gameFrame.gamePanelInstance().repaint();
    }
}
