package org.aegisdefender.Model.Entities.Enemies;

import org.aegisdefender.Config.UIConfig;
import org.aegisdefender.Model.Entities.Player;
import org.aegisdefender.Model.Projectiles.EnemyLaser;
import org.aegisdefender.Model.Projectiles.Projectile;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class Posamine extends Enemy{

    private final int attackPower = 8;
    private long currentTime = 0;
    private long lastShootTime = 0;

    private final int DELAY = 1000; //ms
    private final int SPAWN_POINT_OFFSET_X = 70;
    private final int SPAWN_POINT_OFFSET_Y = UIConfig.TILES*2;
    private final int PROJECTILE_WIDTH = UIConfig.TILES/2;
    private final int PROJECTILE_HEIGHT = UIConfig.TILES/2;

    private int startX;
    private List<Projectile> projectiles;
    private EnemyLaser enemyLaser;


    private enum Phase { DESCENDING, STABILIZING, ASCENDING }
    private Phase phase = Phase.DESCENDING;
    private int stabilizeTimer = 0;
    private static final int STABILIZE_DURATION = 120;


    public Posamine(){
        this.x = -5;
        this.y = -10;
        this.speed = 1;
        this.health = 100;
        this.width = UIConfig.TILES*4;
        this.height = UIConfig.TILES*4;
        this.healthBarWidth = 80;
        this.healthBarHeight = 6;
        this.maxHealth = 100;
        this.isExploding = false;

        this.HEALTH_BAR_OFFSET_X = 40;
        this.HEALTH_BAR_OFFSET_Y = 80;

        this.projectiles = new ArrayList<>();
        this.pointOnEnemyDead = 200;
    }

    @Override
    public Rectangle getHitBox() {
        return new Rectangle(
                x + UIConfig.TILES+10,
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
        //explosion simulation
        if(isExploding){
            this.triggerExplosion();
            return;
        }

        // Zigzag horizontal

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
        enemyLaser = new EnemyLaser(this);
        return enemyLaser;
    }

    @Override
    public void updateEnemyProjectiles() {
        // 1) Aggiorna SEMPRE i proiettili già esistenti (così non restano “congelati”)
        for(int i = 0; i < this.projectiles.size(); i++){
            Projectile p = this.projectiles.get(i);
            p.setLaserY(p.getLaserY() + 7);

            if(p.getLaserY() > UIConfig.WINDOW_HEIGHT + UIConfig.TILES){
                this.projectiles.remove(i);
                i--;
            }
        }

        // 2) Se il nemico è fuori scena/morto, NON spawna nuovi proiettili
        if (!isAlive()) {
            return;
        }

        // (opzionale) spara solo quando è entrato nello schermo
        if (this.y + UIConfig.TILES*2 < 0) {
            return;
        }

        // 3) Spawn a cadenza
        currentTime = System.currentTimeMillis();
        if(currentTime - lastShootTime > DELAY){
            projectiles.add(shoot());
            lastShootTime = currentTime;
        }
    }


    @Override
    public List<Projectile> getProjectiles() {
        return this.projectiles;
    }

    @Override
    public Rectangle getProjectileHitBox() {
      return enemyLaser.getProjectileHitBox(); //---
    }


    @Override
    public Point getProjectileSpawnPoint() {
        return new Point(this.x + SPAWN_POINT_OFFSET_X, this.y + SPAWN_POINT_OFFSET_Y);
    }

    @Override
    public Dimension getProjectileSize() {
        return new Dimension(PROJECTILE_WIDTH, PROJECTILE_HEIGHT);
    }

    public void setStartX(int startX) {
        this.startX = startX;
    }

    @Override
    public boolean isAlive(){ //
        int margin = UIConfig.TILES;

        boolean tooLow = this.y > UIConfig.WINDOW_HEIGHT + this.height + margin;
        boolean tooHigh = (this.y + this.height) < - margin;

       boolean outOfScreen = tooLow || tooHigh;

        // Se è fuori schermo ma ha ancora proiettili attivi, resta “vivo”
        // solo per permettere l’update dei proiettili (ma non ne spawnerà di nuovi).
        if (outOfScreen) {
            return !projectiles.isEmpty();
        }
       return this.isAlive;
    }
}
