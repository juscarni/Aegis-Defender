package org.aegisdefender.Model;

import org.aegisdefender.Model.Core.WaveManager;
import org.aegisdefender.Model.Entities.Enemies.Enemy;
import org.aegisdefender.Model.Entities.Enemies.Posamine;
import org.aegisdefender.Model.Entities.Player;
import org.aegisdefender.Model.Projectiles.Projectile;

import java.util.ArrayList;
import java.util.List;

public class GameModel{
    private Player player;
    private WaveManager waveManager;
    private List<GameObserver> observer;
    private List<List<Projectile>>  enemyProjectiles;

    private Posamine posamine;

    //------------//
    public GameModel(){
        player = new Player();
        waveManager = new WaveManager();
        // observer list
        observer = new ArrayList<>();
        enemyProjectiles = new ArrayList<>();

        //Enemies
        posamine = new Posamine();
    }

    public void addObserver(GameObserver ob){
        observer.add(ob);
    }
    public void removeObserver(GameObserver ob){
        observer.remove(ob);
    }

    public void notifyObserver(){
        for(GameObserver ob: observer){
            ob.updatePlayer(player.getX(),player.getY(),player.getPlayerWidth(),player.getPlayerHeight());
            ob.updateProjectiles(new ArrayList<>(player.playerProjectiles()));
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
        return waveManager.getActiveEnemies();
    }

    public void updateEnemyProjectiles(){
        posamine.updateEnemyProjectiles();
        enemyProjectiles.add(posamine.getProjectiles());
    }

    public List<List<Projectile>> getEnemyProjectiles(){
        return this.enemyProjectiles;
    }
}
