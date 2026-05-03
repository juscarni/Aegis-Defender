package org.aegisdefender.Model.Core;

import org.aegisdefender.Model.Entities.Enemies.Enemy;
import org.aegisdefender.Model.Entities.Player;
import org.aegisdefender.Model.Projectiles.Projectile;

import java.util.List;

public class CollisionDetector {

    private List<Enemy> enemies;
    private Player player;
    private List<Projectile> projectile;

    public CollisionDetector(List<Enemy> enemies, Player player, List<Projectile> projectile){
        this.enemies = enemies;
        this.player = player;
        this.projectile = projectile;
    }

    public boolean checkCollisionPlayerEnemy(Enemy enemy){
        return player.HitBox().intersects(enemy.getHitBox());
    }
}
