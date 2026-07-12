package org.aegisdefender.Model.Core;

import org.aegisdefender.Model.Entities.Enemies.Enemy;
import org.aegisdefender.Model.Entities.Player;
import org.aegisdefender.Model.Projectiles.Projectile;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CollisionDetector {
    private final Player player;
    private final List<Enemy> enemies;
    private List<List<Projectile>> enemyProjectiles;

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

    public boolean collisiOnPlayerEnemy(Enemy enemy){
        if (player == null || enemy == null) return false;
        return intersects(player.HitBox(), enemy.getHitBox());
    }

    public boolean collisionPlayerAndEnemy(){
        return getCollisionPointPlayerAndEnemy() != null;
    }

    public boolean collisionEnemyProjectileAndPlayer(){
        return getCollisionPointEnemyProjectileAndPlayer() != null;
    }

    public boolean collisionPlayerProjectileAndEnemy(){
        return getCollisionPointPlayerProjectileAndEnemy(player.playerProjectiles()) != null;
    }

    public Point getCollisionPointPlayerEnemy(Enemy enemy) {
        if (player == null || enemy == null) return null;

        Rectangle overlap = intersection(player.HitBox(), enemy.getHitBox());
        return (overlap == null) ? null : centerOf(overlap);
    }

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
    /**
     * PlayerProjectile -> Enemy
     * Ritorna tutti gli hit del frame.
     * Regola: un proiettile colpisce al massimo 1 nemico (break sulla prima intersezione).
     */
    public List<HitResult> findAllPlayerProjectileHits(){
        List<HitResult> hits = new ArrayList<>();
        List<Projectile> playerProjectiles = player.playerProjectiles();

        if (playerProjectiles == null || enemies == null) return hits; //

        for (Projectile p : playerProjectiles) {
            if (p == null) continue;

            Rectangle pBox = p.getProjectileHitBox();
            if (pBox == null) continue;
            for (Enemy e : enemies) {
                if (e == null) continue;

                Rectangle overlap = intersection(pBox, e.getHitBox());
                if (overlap != null) {
                    hits.add(new HitResult(p, e, centerOf(overlap)));
                    break;
                }
            }
        }
        return hits;
    }
    /*
     * EnemyProjectile -> Player
     * Ritorna tutti gli hit del frame.
     */
    public List<HitResult> findAllEnemyProjectileHits(){
        List<HitResult> hits = new ArrayList<>();

        if(this.enemyProjectiles == null || player == null) return hits;

        for(List<Projectile> list : this.enemyProjectiles){
            if(list == null) continue;
            for (Projectile p : list) {
                if (p == null) continue;
                Rectangle pBox = p.getProjectileHitBox();
                if (pBox == null) continue;
                Rectangle overlap  = intersection(pBox, player.HitBox());
                if (overlap != null) {
                    hits.add(new HitResult(p , p.getProjectileOwner(), centerOf(overlap))); // this bug has been fixed.
                    break; // a projectile can touch a player once...
                }
            }
        }
        return  hits;
    }
}