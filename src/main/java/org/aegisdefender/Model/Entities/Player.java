package org.aegisdefender.Model.Entities;

import org.aegisdefender.Config.UIConfig;
import org.aegisdefender.Model.Entities.Enemies.Enemy;
import org.aegisdefender.Model.Projectiles.PlayerLaser;
import org.aegisdefender.Model.Projectiles.Projectile;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Player {

    private int x = 285;
    private int y = 660;
    private final int width = UIConfig.TILES * 4;
    private final int height = UIConfig.TILES * 5;

    private int health = 100;
    private boolean isAlive = true;
    private int attackPower = 25;

    private Projectile projectile;
    private List<Projectile> projectiles;
    private long lastShotTime = 0;
    private int DELAY = 150; //ms;


    public Player(){
        projectiles = new ArrayList<>();
    }

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

    // I'll do it after
    public Rectangle HitBox(){
        return new Rectangle();
    }

    public void shoot(){
        projectile = new PlayerLaser(this);
        long now = System.currentTimeMillis();

        if (now - lastShotTime > DELAY) {
            projectiles.add(projectile);
            lastShotTime = now;
        }
    }
    public void updateProjectiles(){
        for (int i = 0; i < projectiles.size(); i++) {
            projectile = projectiles.get(i);
            projectile.setLaserY(projectile.getLaserY() + projectile.getSpeed());
            if (projectile.getLaserY() < - 50) {
                projectiles.remove(i);
                i--;
            }
        }
    }
    public List<Projectile> playerProjectiles(){
        return this.projectiles;
    }
}
