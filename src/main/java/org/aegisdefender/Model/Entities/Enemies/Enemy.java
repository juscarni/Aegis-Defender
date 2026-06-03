package org.aegisdefender.Model.Entities.Enemies;

import org.aegisdefender.Model.Entities.Player;
import org.aegisdefender.Model.Projectiles.Projectile;

import java.awt.*;
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

    // health bar data
    protected int maxHealth;
    protected int healthBarWidth;
    protected int healthBarHeight;
    protected int HEALTH_BAR_OFFSET_X;
    protected int HEALTH_BAR_OFFSET_Y;

    public void update(Player player) {
        move(player);
        enemyBehavior(player);
    }
    public void takeDamage(Player player, int amount){
        this.health -= amount;
        if (this.health <= 0) {
            this.health = 0;
            this.isAlive = false;
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

    public void setX(int x) {this.x = x;}
    public void setY(int y) {this.y = y;}
    public int getEnemyX() {return this.x;}
    public int getEnemyY() {return this.y;}
    public int getWidth(){return this.width;}
    public int getHeight(){return this.height;}

    public void setSpeed(int speed){this.speed = speed;}
    public int getSpeed(){return this.speed;}

    public int getHealth() {return this.health;}
    public boolean isAlive() {return this.health > 0;}



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
