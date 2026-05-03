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
        group1.add(new EnemyGroup(EnemyFactory.EnemyType.KAMIKAZE,6,4,2.0));
        waves.add(new Wave(1,group1));

        // wave 2
        List<EnemyGroup> group2 = new ArrayList<>();
        group2.add(new EnemyGroup(EnemyFactory.EnemyType.KAMIKAZE,5,1,2.0));
        group2.add(new EnemyGroup(EnemyFactory.EnemyType.POSAMINE,3,0,1.0));
        waves.add(new Wave(2, group2));

        //third group....
    }

    public void update(long deltaTime) {
        if (isFinished()) {
            // boss case will be put here ...
            return;
        }

        // 1. Nettoyage automatique des morts
        activeEnemies.removeIf(e -> !e.isAlive());

        // 2. Logique de spawn
        if (!waveInProgress) {
            spawnCurrentWave();
            waveInProgress = true;
            timer = 0;
        }

        // 3. Passage à la suite
        if (activeEnemies.isEmpty()) {
            waveInProgress = false;
            currentWaveIndex++;
            timer = 0;
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
    public boolean isFinished() { return currentWaveIndex >= waves.size(); }
}
