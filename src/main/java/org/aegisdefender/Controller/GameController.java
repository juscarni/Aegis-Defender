package org.aegisdefender.Controller;

import org.aegisdefender.Model.GameModel;
import org.aegisdefender.View.GameFrame;

public class GameController {
    private GameFrame gameFrame = null;
    private GameModel gameModel = null;

    public GameController(GameFrame gameFrame, GameModel gameModel){
        this.gameFrame = gameFrame;
        this.gameModel = gameModel;
    }

}
