package org.aegisdefender.Model.Entities.Enemies;

import org.aegisdefender.Config.UIConfig;
import org.aegisdefender.Model.Entities.Player;
import org.aegisdefender.Model.Projectiles.EnemyLaser;
import org.aegisdefender.Model.Projectiles.Projectile;

import java.util.ArrayList;
import java.util.List;
import java.awt.*;

public class Posamine extends Enemy{

    private int attackPower = 30;

    private long currentTime = 0;
    private long lastShootTime = 0;
    private final int DELAY = 500; //ms

    private int startX;                // position X de référence pour le zigzag
    private Projectile projectile;
    private List<Projectile> projectiles;


    private enum Phase { DESCENDING, STABILIZING, ASCENDING }
    private Phase phase = Phase.DESCENDING;
    private int stabilizeTimer = 0;
    private static final int STABILIZE_DURATION = 60; // frames de stabilisation

    public Posamine(){
        this.x = -5;
        this.y = -10;
        this.speed = 10;
        this.health = 100;
        this.width = UIConfig.TILES*4;
        this.height = UIConfig.TILES*4;
        projectiles = new ArrayList<>();
    }

    @Override
    public Rectangle getHitBox() {
        return new Rectangle(
                x +UIConfig.TILES+10,
                y + UIConfig.TILES+20,
                width - UIConfig.TILES*2-16,
                height - UIConfig.TILES*3+5);
    }

    @Override
    public void enemyBehavior(Player player) {

    }

    @Override
    public void attack(Player player) {
        player.takeDamaged(attackPower);
    }

    @Override
    public String getType() {
        return EnemyFactory.EnemyType.POSAMINE.name();
    }


    @Override
    public void move(Player player) {
        // Zigzag horizontal (actif dans toutes les phases)
        double amplitude = UIConfig.TILES ;
        double frequency = 0.05;
        this.x = (int) (startX + amplitude * Math.sin(frequency * this.y));

        int bottomLimit = UIConfig.WINDOW_HEIGHT / 2 - 60;

        switch (phase) {
            case DESCENDING:
                this.y += this.speed;
                if (this.y >= bottomLimit) {
                    this.y = bottomLimit;
                    phase = Phase.STABILIZING;
                }
                break;

            case STABILIZING:
                stabilizeTimer++;
                if (stabilizeTimer >= STABILIZE_DURATION) {
                    phase = Phase.ASCENDING;
                }
                break;

            case ASCENDING:
                this.y -= this.speed;
                break;
        }
    }

    @Override
    public Projectile shoot() {
        return new EnemyLaser(
          this.x,
          this.y,
          this.width,
          this.height,
                "POSAMINE"
        );
    }

    @Override
    public void updateEnemyProjectiles() {
        currentTime = System.currentTimeMillis();
        if(currentTime - lastShootTime > DELAY){
            projectile = shoot();
            projectiles.add(projectile);
            lastShootTime = currentTime;
        }
        for(int i = 0; i < projectiles.size(); i++){
            projectile = projectiles.get(i);
            projectile.setLaserY(this.y + this.speed);

            if(projectile.getLaserY() > UIConfig.WINDOW_HEIGHT + UIConfig.TILES){
                projectiles.remove(i);
                i--;
            }
        }
    }

    @Override
    public List<Projectile> getProjectiles() {
        return this.projectiles;
    }

    @Override
    public Rectangle getProjectileHitBox() {
        return new Rectangle(
                this.x,
                this.y,
                this.width,
                this.height
        );
    }

    public void setStartX(int startX) {
        this.startX = startX;
    }
}
