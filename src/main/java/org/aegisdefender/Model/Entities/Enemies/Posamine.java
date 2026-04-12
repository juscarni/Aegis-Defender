package org.aegisdefender.Model.Entities.Enemies;

import org.aegisdefender.Config.UIConfig;
import org.aegisdefender.Model.Entities.Player;

import java.awt.*;

public class Posamine extends Enemy{

    private int attackPower = 30;
    private int startX;                // position X de référence pour le zigzag

    private enum Phase { DESCENDING, STABILIZING, ASCENDING }
    private Phase phase = Phase.DESCENDING;
    private int stabilizeTimer = 0;
    private static final int STABILIZE_DURATION = 60; // frames de stabilisation

    public Posamine(){
        this.x = -5;
        this.y = -10;
        this.speed = 1;
        this.health = 100;
        this.width = UIConfig.TILES*4;
        this.height = UIConfig.TILES*4;
    }

    @Override
    public Rectangle getHitBox() {
        return new Rectangle(
                x + 2,
                y + 2,
                width - 4,
                height - 4);
    }

    @Override
    public void enemyBehavior(Player player) {

    }

    @Override
    public void attack(Player player) {
        player.takeDamaged(attackPower);
    }

    @Override
    public String getType() {
        return EnemyFactory.EnemyType.POSAMINE.name();
    }


    @Override
    public void move(Player player) {
        // Zigzag horizontal (actif dans toutes les phases)
        double amplitude = UIConfig.TILES ;
        double frequency = 0.05;
        this.x = (int) (startX + amplitude * Math.sin(frequency * this.y));

        int bottomLimit = UIConfig.WINDOW_HEIGHT / 2 - 60;

        switch (phase) {
            case DESCENDING:
                this.y += this.speed;
                if (this.y >= bottomLimit) {
                    this.y = bottomLimit;
                    phase = Phase.STABILIZING;
                }
                break;

            case STABILIZING:
                stabilizeTimer++;
                if (stabilizeTimer >= STABILIZE_DURATION) {
                    phase = Phase.ASCENDING;
                }
                break;

            case ASCENDING:
                this.y -= this.speed;
                break;
        }
    }
    public void setStartX(int startX) {
        this.startX = startX;
    }
}
