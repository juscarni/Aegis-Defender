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
        gameController = new GameController(gameFrame, gameModel);

        // this is a listener , if we click to the button play the game starts
        this.gameFrame.getMainMenuPanel().setOnPlayCallBack(this::startGame);
    }

    public void startGame(){
        this.gameModel.modelInit();
        gameModel.addObserver(gameController);
        gameModel.init();
        this.gameController.startGameLoop(); // ----
    }
}