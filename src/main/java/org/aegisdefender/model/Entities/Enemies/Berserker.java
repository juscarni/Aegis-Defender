package org.aegisdefender.model.Entities.Enemies;

import org.aegisdefender.config.UIConfig;
import org.aegisdefender.model.Entities.Player;
import org.aegisdefender.model.Projectiles.EnemyLaser;
import org.aegisdefender.model.Projectiles.Projectile;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class Berserker extends Enemy {

    private final int contactDamage = 15;

    private List<Projectile> projectiles = new ArrayList<>();
    private long lastShootTime = 0;
    private static final int SHOOT_DELAY_MS = 650;

    // Berserker: entra -> “aggancia” il player orizzontalmente -> carica in basso -> cooldown -> ripete
    private enum Phase { ENTERING, TRACKING, CHARGING, COOLDOWN }
    private Phase phase = Phase.ENTERING;

    private int chargeSpeed = 10;
    private int trackSpeed = 4;
    private int cooldownFrames = 0;
    private static final int COOLDOWN_DURATION_FRAMES = 45;

    public Berserker() {
        this.x = UIConfig.WINDOW_WIDTH / 2;
        this.y = -UIConfig.TILES * 4;

        this.speed = 3;
        this.health = 180;   // più tanky
        this.isExploding = false;

        this.width = UIConfig.TILES * 4;
        this.height = UIConfig.TILES * 4;

        this.healthBarWidth = 80;
        this.healthBarHeight = 6;
        this.maxHealth = 180;
        this.HEALTH_BAR_OFFSET_X = 40;
        this.HEALTH_BAR_OFFSET_Y = 80;
        this.pointOnEnemyDead = 150;
    }

    @Override
    public boolean isAlive() {
        if (!super.isAlive()) {
            return false;
        }
        int margin = UIConfig.TILES * 2;
        return this.y < UIConfig.WINDOW_HEIGHT + this.height + margin || this.health == 0;
    }

    @Override
    public Rectangle getHitBox() {
        return new Rectangle(
                x + UIConfig.TILES,
                y + UIConfig.TILES,
                width - UIConfig.TILES * 2,
                height - UIConfig.TILES * 2
        );
    }

    @Override
    public void enemyBehavior(Player player) {
        // Qui ci limitiamo a sparare + muovere.
        updateEnemyProjectiles();
    }

    @Override
    public void attack(Player player) {
        player.takeDamaged(contactDamage);
    }

    @Override
    public String getType() {
        return EnemyFactory.EnemyType.BERSERKER.name();
    }

    @Override
    public void move(Player player) {
        int targetY = UIConfig.WINDOW_HEIGHT / 4;

        switch (phase) {
            case ENTERING -> {
                this.y += this.speed;
                if (this.y >= targetY) {
                    this.y = targetY;
                    phase = Phase.TRACKING;
                }
            }

            case TRACKING -> {
                // segue il player solo in X
                int playerX = player.getX();
                if (playerX > this.x) {
                    this.x += trackSpeed;
                } else if (playerX < this.x) {
                    this.x -= trackSpeed;
                }

                // “trigger” della carica quando è abbastanza allineato
                if (Math.abs(playerX - this.x) < UIConfig.TILES) {
                    phase = Phase.CHARGING;
                }
            }

            case CHARGING -> {
                this.y += chargeSpeed;

                // se esce sotto, lo riportiamo sopra e ricominciamo
                if (this.y > UIConfig.WINDOW_HEIGHT + UIConfig.TILES * 2) {
                    this.y = -this.height;
                    phase = Phase.COOLDOWN;
                    cooldownFrames = 0;
                }
            }

            case COOLDOWN -> {
                cooldownFrames++;
                // durante cooldown rientra lentamente
                this.y += this.speed;

                if (cooldownFrames >= COOLDOWN_DURATION_FRAMES) {
                    phase = Phase.TRACKING;
                }
            }
        }

        // clamp X dentro lo schermo
        int minX = 0;
        int maxX = UIConfig.WINDOW_WIDTH - this.width;
        if (this.x < minX) this.x = minX;
        if (this.x > maxX) this.x = maxX;
    }

    @Override
    public Projectile shoot() {
        return new EnemyLaser(this);
    }

    @Override
    public void updateEnemyProjectiles() {
        long now = System.currentTimeMillis();

        // spara solo quando è in scena e non in piena carica “off-screen”
        if (phase != Phase.ENTERING) {
            if (now - lastShootTime > SHOOT_DELAY_MS) {
                projectiles.add(shoot());
                lastShootTime = now;
            }
        }

        // update proiettili verso il basso
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
        if (projectiles.isEmpty()) {
            return new Rectangle(0, 0, 0, 0);
        }
        return projectiles.get(projectiles.size() - 1).getProjectileHitBox();
    }

    @Override
    public Point getProjectileSpawnPoint() {
        // centro-basso (coerente anche se cambia size dello sprite)
        return new Point(this.x + this.width / 2, this.y + (this.height * 3) / 4);
    }

    @Override
    public Dimension getProjectileSize() {
        // proiettile un po’ più “grosso” del Posamine
        return new Dimension(UIConfig.TILES / 2, UIConfig.TILES / 2);
    }

    public int getProjectileSpeed() {
        return 11;
    }
}