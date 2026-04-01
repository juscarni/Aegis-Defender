package org.aegisdefender.Controller;

import org.aegisdefender.Model.GameModel;
import org.aegisdefender.View.GameFrame;

public class AppController {

    private GameFrame gameFrame = null;
    private GameModel gameModel = null;
    private GameController gameController = null;

    public AppController(){
        gameFrame = new GameFrame();
        gameModel = new GameModel();
        gameFrame.setVisible(true);
        gameController = new GameController(gameFrame,gameModel);
    }
}