package org.aegisdefender.Model;

import org.aegisdefender.Model.Entities.Player;

public class GameModel{
    private Player player;

    public GameModel(){
        player = new Player();
    }
    public void setPLayerX(int x){
        player.setX(x);
    }
    public void setPlayerY(int y){
        player.setY(y);
    }
    public int getPlayerX(){
        return player.getX();
    }
    public int getPlayerY(){
        return player.getY();
    }
}
