package org.aegisdefender.Model.Entities.Enemies;

import org.aegisdefender.Model.Entities.Player;

import java.awt.Rectangle;

public interface Enemy {
    void takeDamage(Player player, int amount);
    void attack(Player player);

    void update();
    void move();
    void enemyBehavior();

    void setX(int x);
    void setY(int y);
    int getEnemyX();
    int getEnemyY();

    int getHealth();
    boolean isAlive();

    Rectangle getHitBox();
}
