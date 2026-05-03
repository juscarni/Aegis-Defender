package org.aegisdefender.Model.Entities.Enemies;

import org.aegisdefender.Config.UIConfig;

import java.util.ArrayList;
import java.util.List;

public class EnemyFactory {

    public static enum EnemyType {
        KAMIKAZE,
        BERSERKER,
        POSAMINE,
        ARTILIERE,
        BOSS
    }

    // Create one enemy
    public Enemy createEnemy(EnemyType type) {
        return switch (type) {
            case KAMIKAZE -> new Kamikaze();
            case BERSERKER -> new Berserker();
            case POSAMINE -> new Posamine();
            case ARTILIERE -> new Artiliere();
            case BOSS -> new Boss();
            default -> throw new IllegalArgumentException("Unknown enemy type: " + type);
        };
    }
    /**
     * Méthode helper qui assigne un pattern intéressant selon la vague et la zone
     */
    public List<Enemy> createEnemyGroup(EnemyType type, int count, int pattern) {
        List<Enemy> group = new ArrayList<>();

        if (type == EnemyType.KAMIKAZE) {
            for (int i = 0; i < count; i++) {
                Kamikaze k = (Kamikaze) createEnemy(type);

                // Position X aléatoire avec marge
                int x = 60 + (int)(Math.random() * (UIConfig.WINDOW_WIDTH - 120));
                int y = -60 - (i * 45) - (int)(Math.random() * 80); // spawn échelonné

                k.setX(x);
                k.setY(y);
                k.setSpeed(7 /*rand.nextInt(4)*/);
                k.setPattern(pattern);        // ← pattern unique pour toute la vague

                group.add(k);
            }
        } else if(type == EnemyType.POSAMINE){
            int startX = UIConfig.TILES;
            int spacing = UIConfig.TILES*3;

            for(int i = 0; i < count; i++) {
                Posamine p = (Posamine) createEnemy(type);
                p.setX(startX +(spacing*i));
                p.setY(-UIConfig.TILES);
                p.setSpeed(p.getSpeed());
                p.setStartX(startX + (spacing * i));
                group.add(p);
            }
        }
        return group;
    }
}