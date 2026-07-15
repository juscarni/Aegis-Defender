package org.aegisdefender.Model.Entities.Enemies;

import org.aegisdefender.Model.Entities.Player;
import org.aegisdefender.Model.Projectiles.Projectile;

import java.awt.Rectangle;
import java.awt.Point;
import java.awt.Dimension;

import java.util.List;

public abstract class Enemy {
    // enemy data
    protected int x;
    protected int y;
    protected int speed;
    protected int health;
    protected int width;
    protected int height;
    protected boolean isAlive = true;
    protected  boolean isExploding = false;
    protected int pointOnEnemyDead;

    // health bar data
    protected int maxHealth;
    protected int healthBarWidth;
    protected int healthBarHeight;
    protected int HEALTH_BAR_OFFSET_X;
    protected int HEALTH_BAR_OFFSET_Y;

    // explosion dataTimer
    private final int EXPLOSION_DURATION = 150; //ms
    protected long explosionStartTime = 0;

    public void update(Player player) {
        move(player);
        //enemyBehavior(player);
    }
    public void takeDamage(Player player, int amount){
        this.health -= amount;
        if (this.health <= 0) {
            this.explosionStartTime = System.currentTimeMillis(); //--
            this.isExploding = true;
            this.triggerExplosion(); //---
        }
    }
    public int getCurrentHealthBar(){
        return (int)(((double) this.health / this.maxHealth) * this.healthBarWidth);
    }
    public Rectangle getHealthBar() {
        return new Rectangle(
                this.x + HEALTH_BAR_OFFSET_X,
                this.y + HEALTH_BAR_OFFSET_Y,
                this.healthBarWidth,
                this.healthBarHeight);
    }

    public void triggerExplosion(){
        long elapsedTime = System.currentTimeMillis() - this.explosionStartTime;
        //System.out.println(elapsedTime);
        if(elapsedTime >= EXPLOSION_DURATION){
            this.isExploding = false;
            this.isAlive =  false;
        }
    }

    public void setX(int x) {this.x = x;}
    public void setY(int y) {this.y = y;}
    public int getEnemyX() {return this.x;}
    public int getEnemyY() {return this.y;}
    public int getWidth(){return this.width;}
    public int getHeight(){return this.height;}
    public boolean isExploding(){return this.isExploding;}
    public void setSpeed(int speed){this.speed = speed;}
    public int getSpeed(){return this.speed;}
    public int getPoints(){
        return this.pointOnEnemyDead;
    }

    public boolean isAlive() {return this.isAlive;}



    public abstract Rectangle getHitBox();
    public abstract String getType();

    // every single enemy has a particular behavior
    public abstract void enemyBehavior(Player player);
    public abstract void attack(Player player);
    public abstract void move(Player player);
    public abstract Projectile shoot();
    public abstract void updateEnemyProjectiles();
    public abstract List<Projectile> getProjectiles();
    public abstract Rectangle getProjectileHitBox();


    public abstract Point getProjectileSpawnPoint();
    public abstract Dimension getProjectileSize();

}
