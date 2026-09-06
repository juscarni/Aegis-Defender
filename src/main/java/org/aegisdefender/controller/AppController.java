package org.aegisdefender.controller;

import org.aegisdefender.model.GameModel;
import org.aegisdefender.view.GameFrame;

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