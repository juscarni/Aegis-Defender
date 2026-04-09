package org.aegisdefender.Model.Entities.Enemies;

import org.aegisdefender.Config.UIConfig;
import org.aegisdefender.Model.Entities.Player;

import java.awt.Rectangle;

public class Kamikaze extends Enemy{

    private int attackPower = 30;

    public Kamikaze(){
        this.x = 100;
        this.y = -10;
        this.speed = 5;
        this.health = 100;
        this.width = UIConfig.TILES;
        this.height = UIConfig.TILES;
    }

    @Override
    public void attack(Player player) {
        player.takeDamaged(attackPower);
    }

    @Override
    public void move() {
        this.y += speed;
    }

    @Override
    public void enemyBehavior() {

    }

    @Override
    public Rectangle getHitBox() {
        return new Rectangle(
                this.x + 2 ,
                this.y + 2,
                this.width - 4,
                this.height - 4);
    }

    @Override
    public String getType(){
        return EnemyFactory.EnemyType.KAMIKAZE.name();
    }
}
