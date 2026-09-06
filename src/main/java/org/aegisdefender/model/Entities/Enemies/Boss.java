package org.aegisdefender.model.Entities.Enemies;

import org.aegisdefender.model.Entities.Player;
import org.aegisdefender.model.Projectiles.Projectile;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class Boss extends Enemy{

    @Override
    public Rectangle getHitBox() {
        return null;
    }

    @Override
    public void enemyBehavior(Player player) {

    }

    @Override
    public void attack(Player player) {

    }

    @Override
    public String getType() {
        return "";
    }

    @Override
    public void move(Player player) {

    }

    @Override
    public Projectile shoot() {
        return null;
    }

    @Override
    public void updateEnemyProjectiles() {}

    @Override
    public List<Projectile> getProjectiles() {
        return new ArrayList<>();
    } //

    @Override
    public Rectangle getProjectileHitBox() {
        return null;
    }


    @Override
    public Point getProjectileSpawnPoint() {
        return null;
    }

    @Override
    public Dimension getProjectileSize() {
        return null;
    }

}
