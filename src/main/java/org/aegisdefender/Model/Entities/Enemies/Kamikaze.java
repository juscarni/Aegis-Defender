package org.aegisdefender.Model.Entities.Enemies;

import org.aegisdefender.Model.Entities.Player;

import java.awt.Rectangle;

public class Kamikaze implements Enemy{
    @Override
    public void takeDamage(Player player, int amount) {

    }

    @Override
    public void attack(Player player) {

    }

    @Override
    public void update() {

    }

    @Override
    public void move() {

    }

    @Override
    public void enemyBehavior() {

    }

    @Override
    public void setX(int x) {

    }

    @Override
    public void setY(int y) {

    }

    @Override
    public int getEnemyX() {
        return 0;
    }

    @Override
    public int getEnemyY() {
        return 0;
    }

    @Override
    public int getHealth() {
        return 0;
    }

    @Override
    public boolean isAlive() {
        return false;
    }

    @Override
    public Rectangle getHitBox() {
        return null;
    }
}
