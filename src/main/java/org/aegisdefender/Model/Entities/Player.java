package org.aegisdefender.Model.Entities;

import org.aegisdefender.Config.UIConfig;

import org.aegisdefender.Model.Entities.Enemies.Enemy;
import org.aegisdefender.Model.Projectiles.PlayerLaser;
import org.aegisdefender.Model.Projectiles.Projectile;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Player {

    private final int width = UIConfig.TILES * 4;
    private final int height = UIConfig.TILES * 5;
    private final int attackPower = 25;
    private final int DELAY = 150; //ms;
    // health bar data
    private final int HEALTH_BAR_WIDTH = 80;
    private final int HEALTH_BAR_HEIGHT = 6;
    private final int HEALTH_BAR_OFFSET_X = -5;
    private final int HEALTH_BAR_OFFSET_Y = 25;
    private final int HEALTH_MAX = 100;

    private int x = 285;
    private int y = 660;
    private int health = 100;
    private boolean isAlive = true;
    private Projectile projectile;
    private List<Projectile> projectiles;
    private long lastShotTime = 0;

    public Player(){
        projectiles = new ArrayList<>();
    }

    public void setX(int playerX){
        this.x = playerX;
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

    public Rectangle HitBox(){
        return new Rectangle(
                x-10,
                y + 15,
                width - UIConfig.TILES*2 + 10,
                height - UIConfig.TILES*4);
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
        return new ArrayList<>(this.projectiles);
    }
    public Rectangle gethealthBar(){
        return new Rectangle(
                this.x + HEALTH_BAR_OFFSET_X,
                this.y + HEALTH_BAR_OFFSET_Y,
                HEALTH_BAR_WIDTH,
                HEALTH_BAR_HEIGHT
        );
    }
    public int getCurrentHealth(){
        return (int)(((double) this.health / this.HEALTH_MAX) * this.HEALTH_BAR_WIDTH);
    }
}
