package org.aegisdefender.Model.Core;

import org.aegisdefender.Model.Entities.Enemies.Enemy;
import org.aegisdefender.Model.Entities.Player;
import org.aegisdefender.Model.Projectiles.Projectile;

import java.awt.*;
import java.util.List;

public class CollisionDetector {
    private final Player player;
    private final List<Enemy> enemies;
    private final List<List<Projectile>> enemyProjectiles;

    public CollisionDetector(List<Enemy> enemies, Player player, List<List<Projectile>> projectiles){
        this.enemies = enemies;
        this.player = player;
        this.enemyProjectiles = projectiles;
    }

    private static Rectangle intersection(Rectangle a, Rectangle b) {
        if (a == null || b == null) return null;
        Rectangle r = a.intersection(b);
        if (r.isEmpty()) return null;
        return r;
    }

    private static Point centerOf(Rectangle r) {
        return new Point(r.x + r.width / 2, r.y + r.height / 2);
    }

    private static boolean intersects(Rectangle a, Rectangle b) {
        return intersection(a, b) != null;
    }

    public boolean checkCollisionPlayerEnemy(Enemy enemy){
        if (player == null || enemy == null) return false;
        return intersects(player.HitBox(), enemy.getHitBox());
    }

    public boolean collisionPlayerAndEnemy(){
        return getCollisionPointPlayerAndEnemy() != null;
    }

    public boolean collisionEnemyProjectileAndPlayer(){
        return getCollisionPointEnemyProjectileAndPlayer() != null;
    }

    public boolean collisionPlayerProjectileAndEnemy(List<Projectile> playerProjectiles){
        return getCollisionPointPlayerProjectileAndEnemy(playerProjectiles) != null;
    }

    /** Punto di collisione (centro overlap) tra Player e un Enemy specifico */
    public Point getCollisionPointPlayerEnemy(Enemy enemy) {
        if (player == null || enemy == null) return null;

        Rectangle overlap = intersection(player.HitBox(), enemy.getHitBox());
        return (overlap == null) ? null : centerOf(overlap);
    }

    /** Punto di collisione tra Player e QUALSIASI Enemy (ritorna il primo trovato) */
    public Point getCollisionPointPlayerAndEnemy(){
        if (player == null || enemies == null) return null;

        Rectangle playerBox = player.HitBox();
        for (Enemy e : enemies) {
            if (e == null) continue;

            Rectangle overlap = intersection(playerBox, e.getHitBox());
            if (overlap != null) {
                return centerOf(overlap);
            }
        }
        return null;
    }

    /** Punto di collisione tra QUALSIASI proiettile nemico e il Player (primo trovato) */
    public Point getCollisionPointEnemyProjectileAndPlayer(){
        if (player == null || enemyProjectiles == null) return null;

        Rectangle playerBox = player.HitBox();

        for (List<Projectile> list : enemyProjectiles) {
            if (list == null) continue;

            for (Projectile p : list) {
                if (p == null) continue;

                Rectangle overlap = intersection(playerBox, p.getProjectileHitBox());
                if (overlap != null) {
                    return centerOf(overlap);
                }
            }
        }
        return null;
    }

    /** Punto di collisione tra QUALSIASI proiettile del Player e QUALSIASI Enemy (primo trovato) */
    public Point getCollisionPointPlayerProjectileAndEnemy(List<Projectile> playerProjectiles){
        if (playerProjectiles == null || enemies == null) return null;

        for (Projectile p : playerProjectiles) {
            if (p == null) continue;
            Rectangle pBox = p.getProjectileHitBox();
            if (pBox == null) continue;

            for (Enemy e : enemies) {
                if (e == null) continue;

                Rectangle overlap = intersection(pBox, e.getHitBox());
                if (overlap != null) {
                    return centerOf(overlap);
                }
            }
        }
        return null;
    }
}