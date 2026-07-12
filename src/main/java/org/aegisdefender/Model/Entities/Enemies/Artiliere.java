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

public class Artiliere extends Enemy {

    private final int attackPower = 20;
    private final int SPAWN_POINT_OFFSET_X = 70;
    private final int SPAWN_POINT_OFFSET_Y = UIConfig.TILES*2 + 10;
    private final int PROJECTILE_WIDTH = UIConfig.TILES/2;
    private final int PROJECTILE_HEIGHT = UIConfig.TILES/2;

    private long lastShootTime = 0;
    private static final int SHOOT_DELAY_MS = 200;

    private List<Projectile> projectiles = new ArrayList<>();

    private enum Phase { DESCENDING, PATROLLING }
    private Phase phase = Phase.DESCENDING;

    private int startX;
    private boolean startXInitialized = false;

    public Artiliere() {
        this.x = UIConfig.WINDOW_WIDTH / 2;
        this.y = - UIConfig.TILES * 4;
        this.width = UIConfig.TILES * 4;
        this.height = UIConfig.TILES * 4;

        this.speed = 2;
        this.health = 120;
        this.isExploding = false;

        this.healthBarWidth = 80;
        this.healthBarHeight = 6;
        this.maxHealth = 120;
        this.HEALTH_BAR_OFFSET_X = 40;
        this.HEALTH_BAR_OFFSET_Y = 80;
        this.pointOnEnemyDead = 150;
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
        if (phase == Phase.PATROLLING) {
            updateEnemyProjectiles();
        }
    }

    @Override
    public void attack(Player player) {
        player.takeDamaged(attackPower);
    }

    @Override
    public String getType() {
        return EnemyFactory.EnemyType.ARTILIERE.name();
    }

    @Override
    public void move(Player player) {

        if(isExploding){
            this.triggerExplosion();
            return;
        }

        if (!startXInitialized) {
            startX = this.x - 70;
            startXInitialized = true;
        }

        int targetY = UIConfig.WINDOW_HEIGHT / 3;

        switch (phase) {
            case DESCENDING -> {
                this.y += this.speed;
                if (this.y >= targetY) {
                    this.y = targetY;
                    phase = Phase.PATROLLING;
                }
            }
            case PATROLLING -> {
                // Oscillazione lenta orizzontale
                double amplitude = UIConfig.TILES * 0.3;
                double frequency = 0.01;
                this.x = (int) (startX + amplitude * Math.sin(frequency * this.y + System.currentTimeMillis() / 200.0));
            }
        }
    }

    @Override
    public Projectile shoot() {
        return new EnemyLaser(this);
    }

    @Override
    public void updateEnemyProjectiles() {
        long now = System.currentTimeMillis();
        if (now - lastShootTime > SHOOT_DELAY_MS) {
            projectiles.add(shoot());
            lastShootTime = now;
        }

        // Avanza verso il basso e rimuove fuori schermo
        for (int i = 0; i < projectiles.size(); i++) {
            Projectile p = projectiles.get(i);
            p.setLaserY(p.getLaserY() + getProjectileSpeed());

            if (p.getLaserY() > UIConfig.WINDOW_HEIGHT + UIConfig.TILES) {
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
       //--
        if (projectiles.isEmpty()) {
            return new Rectangle(0, 0, 0, 0);
        }
        return projectiles.get(projectiles.size() - 1).getProjectileHitBox();
    }

    @Override
    public Point getProjectileSpawnPoint() {
        return new Point(this.x + SPAWN_POINT_OFFSET_X, this.y + this.SPAWN_POINT_OFFSET_Y);
    }

    @Override
    public Dimension getProjectileSize() {
        return new Dimension(PROJECTILE_WIDTH, PROJECTILE_HEIGHT);
    }

    public int getProjectileSpeed() {
        return 7;
    }
}