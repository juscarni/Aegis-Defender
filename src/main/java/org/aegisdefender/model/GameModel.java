package org.aegisdefender.model;

import org.aegisdefender.audio.AudioManager;

import org.aegisdefender.model.Core.CollisionDetector;
import org.aegisdefender.model.Core.HitResult;
import org.aegisdefender.model.Core.WaveManager;
import org.aegisdefender.model.Entities.Enemies.Enemy;
import org.aegisdefender.model.Entities.Enemies.EnemyFactory;
import org.aegisdefender.model.Entities.Player;
import org.aegisdefender.model.Entities.PlayerData;
import org.aegisdefender.model.Projectiles.Projectile;

import org.aegisdefender.playerRepository.PlayerRepository;

import java.awt.Point;
import java.awt.Rectangle;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameModel implements  AudioObseerver {

    private Player player;
    private PlayerData playerData;
    private WaveManager waveManager;
    private PlayerRepository playerRepository;

    private List<GameObserver> observer;
    private List<List<Projectile>>  enemyProjectiles;
    private List<Enemy> activeEnemies;

    private CollisionDetector collisionDetector;

    private List<Point> impactPointsOnPlayerAttackEnemyCurrentFrame;
    private List<Point> impactPointsOnEnemyAttackPlayerCurrentFrame;

    private AudioManager audioManager;

    public GameModel(){
        modelInit();
        playBackgroundMusic();
    }

    public void modelInit(){
        player = new Player();
        waveManager = new WaveManager();
        observer = new ArrayList<>();
        enemyProjectiles = new ArrayList<>();
        activeEnemies = new ArrayList<>();
        impactPointsOnPlayerAttackEnemyCurrentFrame = new ArrayList<>();
        impactPointsOnEnemyAttackPlayerCurrentFrame = new ArrayList<>();
        playerData = new PlayerData();
        playerRepository = new PlayerRepository();
        audioManager = new AudioManager();
        loadPlayerData();
    }

    public void addObserver(GameObserver ob){
        observer.add(ob);
    }

    public void notifyObserver(){
        for(GameObserver ob: observer){
            ob.updatePlayer(
                    player.getX(),
                    player.getY(),
                    player.getPlayerWidth(),
                    player.getPlayerHeight()
            );
            ob.updateProjectiles(player.playerProjectiles());
        }
    }

    /****************************************************************************************************************
     ********************************************** PLAYER DATA *****************************************************
     ****************************************************************************************************************/
    public void setPlayerX(int x){
        player.setX(x);
    }

    public void setPlayerY(int y){
        player.setY(y);
        notifyObserver();
    }

    public int getPlayerX(){return player.getX();}
    public int getPlayerY(){return player.getY();}
    public void init(){notifyObserver();}
    public void spawnPlayerProjectile(){
        player.shoot();
    }

    public void updateProjectiles() {
        player.updateProjectiles();
        notifyObserver();
    }

    public boolean isPlayerAlive(){
        return this.player.isAlive();
    }

    public int getPLAYER_MIN_X() {
        return player.getPLAYER_MIN_X();
    }

    public int getPLAYER_MAX_X() {
        return player.getPLAYER_MAX_X();
    }

    public int getPLAYER_MIN_Y() {
        return player.getPLAYER_MIN_Y();
    }

    public int getPLAYER_MAX_Y() {
        return player.getPLAYER_MAX_Y();
    }

    /***************************************PLAYER HEALTH BAR DATA AND HITBOX**************************************/

    public Rectangle playerHitBox(){
        return player.HitBox();
    }
    public Rectangle getHealthBar(){
        return player.gethealthBar();
    }
    public int getPlayerCurrentHealth(){
        return player.getCurrentHealth();
    }
    public boolean isInCoolDown(){return player.isInCooldown();}
    public void setShieldActivated(){
        player.setShieldActive(true);
    }

    /****************************************************************************************************************
     *********************************************** ENEMY DATA *****************************************************
     ****************************************************************************************************************/
    public void updateEnemy(){
        this.activeEnemies = waveManager.getActiveEnemies();
        for(Enemy enemy : this.activeEnemies){
            enemy.update(player);
        }
        long currentTime = System.currentTimeMillis();
        waveManager.update(currentTime);
    }

    public List<Enemy> getActiveEnemies(){
        return this.activeEnemies;
    }

    public void updateEnemyProjectiles(){
        enemyProjectiles.clear();

        for(Enemy enemy : waveManager.getActiveEnemies()){
            enemy.updateEnemyProjectiles();
            enemyProjectiles.add(enemy.getProjectiles());
        }
    }

    public List<List<Projectile>> getEnemyProjectiles(){
        return new ArrayList<>(this.enemyProjectiles); // copy snapshot
    }

    public void checkCollision(){
        collisionDetector = new CollisionDetector(
                this.waveManager.getActiveEnemies(),
                this.player,
                this.enemyProjectiles
        );
        /* *************************************************************************************************************
        * ********************* PROCESS PLAYER ATTACK AND ENEMIES ATTACK ***********************************************
        * **************************************************************************************************************
        * */
        handlePlayerProjectileHits();
        handleEnemyProjectileHits();
        this.impactPointsOnPlayerAttackEnemyCurrentFrame.addAll(this.impactPointsOnEnemyAttackPlayerCurrentFrame);
        // Collision On player and Enemy
        collsionOnPlayerEnemy();
    }

    public List<Point> impactPointOnPlayerAttackEnemy(){
        return new ArrayList<>(this.impactPointsOnPlayerAttackEnemyCurrentFrame);
    }

    public void handlePlayerProjectileHits(){
        // reset at every frame
        impactPointsOnPlayerAttackEnemyCurrentFrame.clear();

        List<HitResult> hits = collisionDetector.findAllPlayerProjectileHits();

        if (!hits.isEmpty()) {

            for (HitResult hit : hits) {
                impactPointsOnPlayerAttackEnemyCurrentFrame.add(hit.impactPoint());
                player.attack(hit.enemy());
            }

            Set<Projectile> toRemove = new HashSet<>();
            for (HitResult hit : hits) {
                toRemove.add(hit.projectile());
            }

            player.playerProjectiles().removeIf(toRemove::contains); //
        }
    }

    public void handleEnemyProjectileHits(){
        impactPointsOnEnemyAttackPlayerCurrentFrame.clear();
        List<HitResult> Hits = collisionDetector.findAllEnemyProjectileHits();

        if(!Hits.isEmpty()) {
            for(HitResult hit : Hits) {
                impactPointsOnEnemyAttackPlayerCurrentFrame.add(hit.impactPoint());
                System.out.println(hit.enemy().getType() + " has attacked player");
                //
                hit.enemy().attack(player);

                // ----
            }
            Set<Projectile> toRemove = new HashSet<>();

            for(HitResult hit :  Hits) {
                toRemove.add(hit.projectile()); // accumulate projectile to remove
            }

            for(List<Projectile> projectileList : this.enemyProjectiles){
                projectileList.removeIf(toRemove::contains);
            }
        }
    }

    public void collsionOnPlayerEnemy(){
        for(Enemy enemy : activeEnemies){
            if(enemy.getType().equals(EnemyFactory.EnemyType.KAMIKAZE.name()) && collisionDetector.collisiOnPlayerEnemy(enemy)) {

                enemy.attack(player);
            }
        }
    }


    public int getWaveIndex(){
        return waveManager.getCurrentWaveIndex() + 1; // + 1 because we know that the index starts by 0 ...
    }
    public int getKills(){
        return this.waveManager.getKills();
    }
    public int getScore(){
        return this.waveManager.getScore();
    }

    /* *******************************  LOAD AND SAVE PLAYER_DATA TO PLAYER.CSV FILE   ***********************************/
     /******************************************************************************************************************/
    public void setPlayerData(String username, int score, int kills, LocalDateTime localDateTime) {
        String dateTime = localDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy|HH:mm:ss"));
        this.playerData.updateData(username, score, kills, dateTime);
    }

    public void loadPlayerData(){
        List<String[]> playerDataFromCsv = playerRepository.readPlayerDataFromCsv();
        playerData.loadPlayerData(playerDataFromCsv);
    }

    public int getBestScore(){
        return this.playerData.getBestScore();
    }

    public void savePlayerData(){
        playerRepository.savePlayerData(playerData.getData());
    }

    public void playBackgroundMusic(){
        this.audioManager.playSound();
    }

    public List<String[]> getPlayerData(){
        return this.playerData.getData();
    }

    @Override
    public void setVolume(int volume) {
        audioManager.setVolume(volume);
    }
}