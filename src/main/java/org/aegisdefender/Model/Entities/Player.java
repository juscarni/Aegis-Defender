package org.aegisdefender.Model.Entities;

import org.aegisdefender.Config.UIConfig;
import org.aegisdefender.Model.Entities.Enemies.Enemy;

public class Player {

    private int x = 285;
    private int y = 660;
    private final int width = UIConfig.TILES * 4;
    private final int height = UIConfig.TILES * 5;

    private int health = 100;
    private boolean isAlive = true;
    private int attackPower = 25;



    public void setX(int playerX){
        this.x= playerX;
    }
    public void setY(int playerY){
        this.y = playerY;
    }
    public int getX(){
        return this.x;
    }
    public int  getY(){
        return this.y;
    }
    public int getPlayerWidth(){return width;}
    public int getPlayerHeight(){return this.height;}
    public boolean isAlive(){
        return isAlive;
    }

    public void takeDamaged(int amount){
        health -= amount;
        if(health <= 0){
            health = 0;
            isAlive = false;
        }
    }
    public void attack(Enemy enemy){
        enemy.takeDamage(this, attackPower);
    }
}
