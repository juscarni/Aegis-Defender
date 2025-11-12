package org.aegisdefender.Model.Entities;

public class Player {

    private int initialPositionPlayerX = 285;
    private int initialPositionPlayerY = 660;

    public void setX(int playerX){
        this.initialPositionPlayerX = playerX;
    }
    public void setY(int playerY){
        this.initialPositionPlayerY = playerY;
    }
    public int getX(){
        return this.initialPositionPlayerX;
    }
    public int  getY(){
        return this.initialPositionPlayerY;
    }
}
