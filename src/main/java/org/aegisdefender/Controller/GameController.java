package org.aegisdefender.Controller;

import org.aegisdefender.Model.GameModel;
import org.aegisdefender.View.GameFrame;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameController extends MouseAdapter {
    private GameFrame gameFrame = null;
    private GameModel gameModel = null;

    public GameController(GameFrame gameFrame, GameModel gameModel) {
        this.gameFrame = gameFrame;
        this.gameModel = gameModel;

        this.gameFrame.addMouseMotionListener(this);

       // setMousePosition(gameFrame,this.gameModel.getPlayerX(),this.gameModel.getPlayerY());

        //initialPlayerPosition before moving the mouse
        this.gameFrame.gamePanelInstance().setPlayerPositionX(this.gameModel.getPlayerX());
        this.gameFrame.gamePanelInstance().setPlayerPositionY(this.gameModel.getPlayerY());
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        super.mouseMoved(e);
        this.gameModel.setPLayerX(e.getX());
        this.gameModel.setPlayerY(e.getY());
        this.gameFrame.gamePanelInstance().setPlayerPositionX(this.gameModel.getPlayerX());
        this.gameFrame.gamePanelInstance().setPlayerPositionY(this.gameModel.getPlayerY());
        //this.gameFrame.gamePanelInstance().repaint();
        //System.out.println(this.gameModel.getPlayerX() + " , " + this.gameModel.getPlayerY());
    }
    public  void setMousePosition(GameFrame gameFrame, int x, int y) {
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
}
