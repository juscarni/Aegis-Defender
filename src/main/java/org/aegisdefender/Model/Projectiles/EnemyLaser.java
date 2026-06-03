package org.aegisdefender.Model.Projectiles;

import org.aegisdefender.Model.Entities.Enemies.Enemy;

import java.awt.*;

public class EnemyLaser extends Projectile{

    public EnemyLaser(Enemy enemy){
        this.projectileX = enemy.getProjectileSpawnPoint().x;
        this.projectileY = enemy.getProjectileSpawnPoint().y;
        this.projectileWidth = enemy.getProjectileSize().width;
        this.projectileHeight = enemy.getProjectileSize().height;
        this.type = enemy.getType();
    }
}
