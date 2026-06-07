package org.aegisdefender.Model;

import org.aegisdefender.Model.Core.WaveManager;
import org.aegisdefender.Model.Entities.Enemies.Enemy;
import org.aegisdefender.Model.Entities.Player;
import org.aegisdefender.Model.Projectiles.Projectile;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class GameModel{
    private Player player;
    private WaveManager waveManager;
    private List<GameObserver> observer;
    private List<List<Projectile>>  enemyProjectiles;

    //------------//
    public GameModel(){
        player = new Player();
        waveManager = new WaveManager();
        // observer list
        observer = new ArrayList<>();
        enemyProjectiles = new ArrayList<>();
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

    /****************************************************************************************************************
     *********************************************** ENEMY DATA *****************************************************
     ****************************************************************************************************************/
    public void updateEnemy(){
        for(Enemy enemy : waveManager.getActiveEnemies()){
            enemy.update(player);
        }
        long currentTime = System.currentTimeMillis();
        waveManager.update(currentTime);
    }

    public List<Enemy> getActiveEnemies(){
        return List.copyOf(waveManager.getActiveEnemies()); // just send a copy
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
}
