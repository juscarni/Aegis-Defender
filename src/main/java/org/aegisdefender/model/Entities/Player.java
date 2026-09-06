package org.aegisdefender.model.Entities;

import org.aegisdefender.config.UIConfig;
import org.aegisdefender.model.Entities.Enemies.Enemy;
import org.aegisdefender.model.Projectiles.PlayerLaser;
import org.aegisdefender.model.Projectiles.Projectile;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class Player {

    private final int width = UIConfig.TILES * 4;
    private final int height = UIConfig.TILES * 5;
    private final int attackPower = 25;
    private final int DELAY = 150; // ms

    // player limit to move
    private final int PLAYER_MIN_X = UIConfig.TILES - 12;
    private final int PLAYER_MAX_X = UIConfig.WINDOW_WIDTH - UIConfig.TILES*3 + 20;
    private final int PLAYER_MIN_Y = UIConfig.WINDOW_HEIGHT - 40*4;
    private final int PLAYER_MAX_Y = UIConfig.WINDOW_HEIGHT - 40*2;

    //
    private final long SHOOT_DURATION = 30000; // 60 seconds
    private final long COOLDOWN_DURATION = 15000; // 30 seconds;

    private long shootingStartTime = System.currentTimeMillis();
    private long cooldownStartTime = 0;
    private boolean isInCooldown = false;

    // health bar data
    private final int HEALTH_BAR_WIDTH = 180;
    private final int HEALTH_BAR_HEIGHT = 15;
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
    private boolean isShieldActive = false;

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
        if(this.x > PLAYER_MAX_X){
            this.x = PLAYER_MAX_X;
        }else if(this.x < PLAYER_MIN_X){
            this.x = PLAYER_MIN_X;
        }
        return this.x;
    }

    public int getY(){
        if(this.y < PLAYER_MIN_Y){
            this.y = PLAYER_MIN_Y;
        }else if(this.y > PLAYER_MAX_Y){
            this.y = PLAYER_MAX_Y;
        }
       return this.y;
    }

    public int getPlayerWidth(){
        return width;
    }

    public int getPlayerHeight(){
        return this.height;
    }

    public boolean isAlive(){
        return isAlive;
    }

    public void setShieldActive(boolean active){
        this.isInCooldown = active;
    }

    public boolean getShieldActive(){
        return this.isInCooldown;
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
                x - 10,
                y + 15,
                width - UIConfig.TILES * 2 + 10,
                height - UIConfig.TILES * 4
        );
    }

    public void shoot(){
        long now = System.currentTimeMillis();
        // cooldown handled
        if(isInCooldown){

            if(now - cooldownStartTime >= COOLDOWN_DURATION){
                isInCooldown = false;
                shootingStartTime = now;
            }else{
                return;
            }
        }
        // end of cool down period
        if(now - shootingStartTime >= SHOOT_DURATION){
            isInCooldown = true;
            cooldownStartTime = now;
            return;
        }
        // normal shoot
        if(now - lastShotTime >= DELAY){
            projectiles.add(new PlayerLaser(this));
            lastShotTime = now;
        }
    }

    public void updateProjectiles(){
        for (int i = 0; i < projectiles.size(); i++) {
            projectile = projectiles.get(i);

            projectile.setLaserY(
                    projectile.getLaserY() + projectile.getSpeed()
            );

            if (projectile.getLaserY() < -50) {
                projectiles.remove(i);
                i--;
            }
        }
        //System.out.println(this.getRemainingCooldown());
    }

    public List<Projectile> playerProjectiles(){
        return this.projectiles;
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

    public boolean isInCooldown(){
        return this.isInCooldown;
    }

    public long getRemainingCooldown(){
        if(!isInCooldown){
            return 0;
        }

        return Math.max(
                0,
                (COOLDOWN_DURATION - (System.currentTimeMillis() - cooldownStartTime)) / 1000
        );
    }

    public int getPLAYER_MIN_X() {
        return PLAYER_MIN_X;
    }

    public int getPLAYER_MAX_X() {
        return PLAYER_MAX_X;
    }

    public int getPLAYER_MIN_Y() {
        return PLAYER_MIN_Y;
    }

    public int getPLAYER_MAX_Y() {
        return PLAYER_MAX_Y;
    }
}