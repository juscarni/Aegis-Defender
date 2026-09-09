package org.aegisdefender.model.Entities.Enemies;

import org.aegisdefender.config.UIConfig;

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

    public List<Enemy> createEnemyGroup(EnemyType type, int count, int pattern) {
        List<Enemy> group = new ArrayList<>();

        if (type == EnemyType.KAMIKAZE) {
            for (int i = 0; i < count; i++) {
                Kamikaze k = (Kamikaze) createEnemy(type);

                int x = 60 + (int)(Math.random() * (UIConfig.WINDOW_WIDTH - 120));
                int y = -60 - (i * 45) - (int)(Math.random() * 80);

                k.setX(x);
                k.setY(y);
                k.setSpeed(k.getSpeed() /*rand.nextInt(4)*/);
                k.setPattern(pattern);        //

                group.add(k);
            }
        }
        else if(type == EnemyType.POSAMINE){
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
        else if(type == EnemyType.ARTILIERE) {
            int margin = UIConfig.TILES;
            int usableWidth = UIConfig.WINDOW_WIDTH - (margin*4); // *2
            int spacing = (count <= 1) ? 0 : (usableWidth / (count - 1));
            //int spacing = UIConfig.TILES*3;
            int x = 0;
            for (int i = 0; i < count; i++) {
                Artiliere a = (Artiliere) createEnemy(type);
                if(i == 0){
                    x = margin + 25;
                }else{
                    x = margin + (spacing * i);
                }
                int y = -UIConfig.TILES * 4 - (i * UIConfig.TILES * 2); // spawn “a scalini” fuori schermo

                a.setX(x);
                a.setY(y);

                a.setSpeed(2);

                group.add(a);
            }
        }
        return group;
    }
}