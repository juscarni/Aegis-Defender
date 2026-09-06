package org.aegisdefender.model.Projectiles;

import org.aegisdefender.model.Entities.Enemies.Enemy;

public class EnemyLaser extends Projectile{

    public EnemyLaser(Enemy enemy){
        this.projectileX = enemy.getProjectileSpawnPoint().x;
        this.projectileY = enemy.getProjectileSpawnPoint().y;
        this.projectileWidth = enemy.getProjectileSize().width;
        this.projectileHeight = enemy.getProjectileSize().height;
        this.type = enemy.getType();
        this.enemy = enemy;
    }
}
