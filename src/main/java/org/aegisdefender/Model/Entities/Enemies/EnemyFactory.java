package org.aegisdefender.Model.Entities.Enemies;

import org.aegisdefender.Config.UIConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EnemyFactory {

    private static final Random rand = new Random();

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

    // Create an enemy in known position specified by x and y
    public Enemy createEnemy(EnemyType type, int x, int y) {
        Enemy enemy = createEnemy(type);
        enemy.setX(x);
        enemy.setY(y);
        return enemy;
    }

    /**
     * Crée un groupe de Kamikazes avec un comportement d'attaque beaucoup plus dynamique
     */
    /*public List<Enemy> createEnemyGroup(EnemyType type, int count) {
        List<Enemy> group = new ArrayList<>();

        if (type == EnemyType.KAMIKAZE) {
            int waves = Math.max(1, count / 3);   // ex: 9 kamikazes = 3 vagues de 3

            for (int wave = 0; wave < waves; wave++) {
                for (int j = 0; j < 3 && (wave * 3 + j) < count; j++) {
                    Kamikaze k = (Kamikaze) createEnemy(type);

                    // Répartition en 3 zones : gauche / centre / droite
                    int zone = j % 3;
                    int x;
                    if (zone == 0) x = 80 + (int)(Math.random() * 100);           // gauche
                    else if (zone == 1) x = UIConfig.WINDOW_WIDTH / 2 - 50 + (int)(Math.random() * 100); // centre
                    else x = UIConfig.WINDOW_WIDTH - 180 + (int)(Math.random() * 100); // droite

                    int y = -80 - (wave * 90) - (int)(Math.random() * 60);

                    k.setX(x);
                    k.setY(y);
                    k.setSpeed(7 + (int)(Math.random() * 3));

                    group.add(k);
                }
            }
        }

        else {
            // Comportement par défaut pour les autres ennemis (formation classique)
            for (int i = 0; i < count; i++) {
                Enemy enemy = createEnemy(type);
                int randomX = (int)(Math.random() * (UIConfig.WINDOW_WIDTH - UIConfig.TILES));
                enemy.setX(randomX);
                enemy.setY(-50 - (i * 35));
                group.add(enemy);
            }
        }

        return group;
    }

    // Méthode helper pour mieux répartir les X des kamikazes
    private int getRandomKamikazeX() {
        // Évite les bords extrêmes pour que les kamikazes aient toujours de la place pour manœuvrer
        int margin = 60;
        return margin + (int)(Math.random() * (UIConfig.WINDOW_WIDTH - margin * 2));
    }

    // Create mix group enemy
    public List<Enemy> createMixedWave(int kamikazeCount, int berserkerCount, int posamineCount) {
        List<Enemy> wave = new ArrayList<>();
        wave.addAll(createEnemyGroup(EnemyType.KAMIKAZE, kamikazeCount));
        wave.addAll(createEnemyGroup(EnemyType.BERSERKER, berserkerCount));
        wave.addAll(createEnemyGroup(EnemyType.POSAMINE, posamineCount));
        return wave;
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
                p.setStartX(startX + (spacing * i)); // ← ajoute cette ligne
                group.add(p);
            }
        }
        return group;
    }
}