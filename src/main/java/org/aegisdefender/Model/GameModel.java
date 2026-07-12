package org.aegisdefender.Model;

import org.aegisdefender.Model.Core.CollisionDetector;
import org.aegisdefender.Model.Core.HitResult;
import org.aegisdefender.Model.Core.WaveManager;
import org.aegisdefender.Model.Entities.Enemies.Enemy;
import org.aegisdefender.Model.Entities.Enemies.EnemyFactory;
import org.aegisdefender.Model.Entities.Player;
import org.aegisdefender.Model.Entities.PlayerData;
import org.aegisdefender.Model.Projectiles.Projectile;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameModel{
    private Player player;
    private PlayerData playerData;
    private WaveManager waveManager;

    private List<GameObserver> observer;
    private List<List<Projectile>>  enemyProjectiles;
    private List<Enemy> activeEnemies;

    private CollisionDetector collisionDetector;

    private List<Point> impactPointsOnPlayerAttackEnemyCurrentFrame;
    private List<Point> impactPointsOnEnemyAttackPlayerCurrentFrame;

    //------------//
    public GameModel(){
        player = new Player();
        waveManager = new WaveManager();
        // observer list
        observer = new ArrayList<>();

        enemyProjectiles = new ArrayList<>();
        activeEnemies = new ArrayList<>();

        // Impact points of player and enemy
        impactPointsOnPlayerAttackEnemyCurrentFrame = new ArrayList<>();
        impactPointsOnEnemyAttackPlayerCurrentFrame = new ArrayList<>();
        playerData = new PlayerData(); //----
    }

    public void addObserver(GameObserver ob){
        observer.add(ob);
    }
    public void removeObserver(GameObserver ob){
        observer.remove(ob);
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
    public void setPLayerX(int x){
        player.setX(x);
        notifyObserver();
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

    public void setPlayerData(String username, int score, int kills, LocalDateTime localDateTime) {
        this.playerData.updateData(username, score, kills, localDateTime);
    }

    public void getInfos(){
        this.playerData.getData().forEach((key , value) -> System.out.println(value));
    }
}