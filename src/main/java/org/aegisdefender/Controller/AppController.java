package org.aegisdefender.Controller;

import org.aegisdefender.Model.GameModel;
import org.aegisdefender.View.GameFrame;

public class AppController {

    private GameFrame gameFrame;
    private GameModel gameModel;
    private GameController gameController;

    public AppController(){
        gameFrame = new GameFrame();
        gameModel = new GameModel();

        gameFrame.setVisible(true);
        gameController = new GameController(gameFrame,gameModel);
        gameModel.addObserver(gameController);

        gameModel.init();
    }
}