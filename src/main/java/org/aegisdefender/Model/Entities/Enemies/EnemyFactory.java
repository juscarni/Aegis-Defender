package org.aegisdefender.Model.Entities.Enemies;

import java.util.ArrayList;
import java.util.List;

public class EnemyFactory {

    public EnemyFactory(){}

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

    // Create a group of enemies whose type is the same
    public List<Enemy> createEnemyGroup(EnemyType type, int count) {
        List<Enemy> group = new ArrayList<>();
        int startX = 50;   // Position de départ
        int spacing = 80;  // Espacement entre chaque ennemi

        for (int i = 0; i < count; i++) {
            Enemy enemy = createEnemy(type);
            enemy.setX(startX + (i * spacing));  // Espacer horizontalement
            enemy.setY(-50);  // Commencer hors écran
            group.add(enemy);
        }
        return group;
    }

    // Create mix group enemy
    public List<Enemy> createMixedWave(int kamikazeCount, int berserkerCount, int posamineCount) {
        List<Enemy> wave = new ArrayList<>();
        wave.addAll(createEnemyGroup(EnemyType.KAMIKAZE, kamikazeCount));
        wave.addAll(createEnemyGroup(EnemyType.BERSERKER, berserkerCount));
        wave.addAll(createEnemyGroup(EnemyType.POSAMINE, posamineCount));
        return wave;
    }
}