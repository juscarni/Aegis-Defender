package org.aegisdefender.Model.Core;

import org.aegisdefender.Model.Entities.Enemies.Enemy;
import org.aegisdefender.Model.Entities.Enemies.EnemyFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WaveManager {

    private final List<Wave> waves = new ArrayList<>();
    private final List<Enemy> activeEnemies;

    private EnemyFactory enemyFactory;

    private int currentWaveIndex = 0;
    private long timer = 0;
    private boolean waveInProgress = false;
    private long DELAY = 3000; // three seconds before spawning another wave

    //
    private int kills;
    private int score;

    public WaveManager(){
        enemyFactory = new EnemyFactory();
        activeEnemies = new ArrayList<>();
        initWaves();
        //stats ---
        kills = 0;
        score = 0;
    }

    public void initWaves(){
        // --- WAVE 1 ---
        List<EnemyGroup> group1 = new ArrayList<>();
        group1.add(new EnemyGroup(EnemyFactory.EnemyType.KAMIKAZE, 5, 2, 2.5));
        waves.add(new Wave(1, group1));

        // --- WAVE 2 ---
        List<EnemyGroup> group2 = new ArrayList<>();
        group2.add(new EnemyGroup(EnemyFactory.EnemyType.KAMIKAZE, 6, 1, 2.5));
        group2.add(new EnemyGroup(EnemyFactory.EnemyType.POSAMINE, 2, 0, 1.5));
        waves.add(new Wave(2, group2));

        // --- VAGUE 3 : Pression ---
        List<EnemyGroup> group3 = new ArrayList<>();
        group3.add(new EnemyGroup(EnemyFactory.EnemyType.KAMIKAZE, 8, 2, 2.0));
        group3.add(new EnemyGroup(EnemyFactory.EnemyType.POSAMINE, 3, 0, 1.5));
        waves.add(new Wave(3, group3));

        // --- WAVE 4 ---
        List<EnemyGroup> group4 = new ArrayList<>();
        group4.add(new EnemyGroup(EnemyFactory.EnemyType.KAMIKAZE, 6, 2, 2.0));
        group4.add(new EnemyGroup(EnemyFactory.EnemyType.ARTILIERE, 2, 1, 1.5));
        waves.add(new Wave(4, group4));

        // --- WAVE 5 --- : Chaos contrôlé ---
        List<EnemyGroup> group5 = new ArrayList<>();
        group5.add(new EnemyGroup(EnemyFactory.EnemyType.KAMIKAZE, 8, 2, 2.0));
        group5.add(new EnemyGroup(EnemyFactory.EnemyType.POSAMINE, 4, 0, 1.5));
        group5.add(new EnemyGroup(EnemyFactory.EnemyType.ARTILIERE, 3, 1, 1.5));
        waves.add(new Wave(5, group5));

        // --- WAVE 6 ---
        List<EnemyGroup> group6 = new ArrayList<>();
        group6.add(new EnemyGroup(EnemyFactory.EnemyType.KAMIKAZE, 5, 3, 2.5));
        group6.add(new EnemyGroup(EnemyFactory.EnemyType.POSAMINE, 6, 0, 1.0));
        group6.add(new EnemyGroup(EnemyFactory.EnemyType.ARTILIERE, 4, 1, 1.5));
        waves.add(new Wave(6, group6));

        // --- WAVE 7 ---
        List<EnemyGroup> group7 = new ArrayList<>();
        group7.add(new EnemyGroup(EnemyFactory.EnemyType.KAMIKAZE, 12, 3, 3.0));
        group7.add(new EnemyGroup(EnemyFactory.EnemyType.POSAMINE, 3, 0, 1.5));
        waves.add(new Wave(7, group7));

        // --- WAVE 8 ---
        List<EnemyGroup> group8 = new ArrayList<>();
        group8.add(new EnemyGroup(EnemyFactory.EnemyType.ARTILIERE, 6, 2, 1.5));
        group8.add(new EnemyGroup(EnemyFactory.EnemyType.POSAMINE, 5, 0, 1.0));
        group8.add(new EnemyGroup(EnemyFactory.EnemyType.KAMIKAZE, 4, 2, 2.0));
        waves.add(new Wave(8, group8));

        // --- WAVE 9 ---
        List<EnemyGroup> group9 = new ArrayList<>();
        group9.add(new EnemyGroup(EnemyFactory.EnemyType.KAMIKAZE, 10, 3, 2.5));
        group9.add(new EnemyGroup(EnemyFactory.EnemyType.POSAMINE, 6, 0, 1.5));
        group9.add(new EnemyGroup(EnemyFactory.EnemyType.ARTILIERE, 5, 2, 1.5));
        waves.add(new Wave(9, group9));

        // --- WAVE 10 : Pré-boss ---
        List<EnemyGroup> group10 = new ArrayList<>();
        group10.add(new EnemyGroup(EnemyFactory.EnemyType.KAMIKAZE, 14, 3, 3.0));
        group10.add(new EnemyGroup(EnemyFactory.EnemyType.POSAMINE, 8, 0, 1.0));
        group10.add(new EnemyGroup(EnemyFactory.EnemyType.ARTILIERE, 6, 2, 1.5));
        waves.add(new Wave(10, group10));
    }

    public void update(long deltaTimeMs) {
        if (isFinished()) {
            // boss case will be put here ...
            return;
        }

        timer += deltaTimeMs;

        Iterator<Enemy> it = activeEnemies.iterator();
        while (it.hasNext()) {
            Enemy enemy = it.next();
            if(!enemy.isAlive()){
                kills++;
                this.score += enemy.getPoints();
                it.remove(); // if the enemy is dead we canceled the enemy in the current list
            }
        }

        if (!waveInProgress) {
            if (timer < DELAY) {
                return;
            }
            spawnCurrentWave();
            waveInProgress = true;
            timer = 0;
        }

        // 3. Passaggio alla wave successiva solo quando tutti sono morti
        if (activeEnemies.isEmpty()) {
            waveInProgress = false;
            currentWaveIndex++;
            timer = 0;
            //System.out.print("there aren't enemies anymore");
        }
    }

    //
    private void spawnCurrentWave() {
        Wave wave = waves.get(currentWaveIndex);
        for (EnemyGroup group : wave.group()) {
            activeEnemies.addAll(enemyFactory.createEnemyGroup(
                    group.type(), group.count(), group.pattern()
            ));
        }
    }

    public List<Enemy> getActiveEnemies() { return this.activeEnemies; }
    public boolean isFinished() { return currentWaveIndex == waves.size(); }

    public int getCurrentWaveIndex() { return currentWaveIndex; }
    public int getKills() { return kills; }
    public int getScore() { return score; }
}