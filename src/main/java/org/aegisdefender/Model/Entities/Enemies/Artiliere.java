package org.aegisdefender.Model.Entities.Enemies;

import org.aegisdefender.Model.Entities.Player;
import org.aegisdefender.Model.Projectiles.Projectile;

import java.awt.*;
import java.util.List;

public class Artiliere extends Enemy{


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
    public void updateEnemyProjectiles() {

    }

    @Override
    public List<Projectile> getProjectiles() {
        return List.of();
    }

    @Override
    public Rectangle getProjectileHitBox() {
        return null;
    }

}
