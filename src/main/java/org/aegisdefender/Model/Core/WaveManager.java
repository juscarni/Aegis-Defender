package org.aegisdefender.Model.Core;

import org.aegisdefender.Model.Entities.Enemies.Enemy;
import org.aegisdefender.Model.Entities.Enemies.EnemyFactory;

import java.util.ArrayList;
import java.util.List;

public class WaveManager {
    private final List<Wave> waves = new ArrayList<>();
    private final List<Enemy> activeEnemies;
    private EnemyFactory enemyFactory;

    private int currentWaveIndex = 0;
    private long timer = 0;
    private boolean waveInProgress = false;
    private long DELAY = 3000; // three seconds before spawning another wave

    public WaveManager(){
        enemyFactory = new EnemyFactory();
        activeEnemies = new ArrayList<>();
        initWaves();
    }

    public void initWaves(){
        // wave 1
        List<EnemyGroup> group1 = new ArrayList<>();
        group1.add(new EnemyGroup(EnemyFactory.EnemyType.KAMIKAZE,6,1,2.0));
        waves.add(new Wave(1,group1));

        // wave 2
        List<EnemyGroup> group2 = new ArrayList<>();
        group2.add(new EnemyGroup(EnemyFactory.EnemyType.KAMIKAZE,6,2,2.0));
        group2.add(new EnemyGroup(EnemyFactory.EnemyType.POSAMINE,3,0,1.0));
        waves.add(new Wave(2, group2));

        //wave 3
        List<EnemyGroup> group3 = new ArrayList<>();
        group3.add(new EnemyGroup(EnemyFactory.EnemyType.ARTILIERE,3,1,2.0));
        group3.add(new EnemyGroup(EnemyFactory.EnemyType.POSAMINE,3,0,1.0));
        waves.add(new Wave(3, group3));
    }

    public void update(long deltaTimeMs) {
        if (isFinished()) {
            // boss case will be put here ...
            return;
        }

        timer += deltaTimeMs;

        activeEnemies.removeIf(e -> !e.isAlive());

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
            System.out.print("there aren't enemies anymore"); //
        }
    }

    private void spawnCurrentWave() {
        Wave wave = waves.get(currentWaveIndex);
        for (EnemyGroup group : wave.group()) {
            activeEnemies.addAll(enemyFactory.createEnemyGroup(
                    group.type(), group.count(), group.pattern()
            ));
        }
    }

    public List<Enemy> getActiveEnemies() { return activeEnemies; }
    public boolean isFinished() { return currentWaveIndex == waves.size(); }
}