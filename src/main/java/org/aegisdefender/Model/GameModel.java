package org.aegisdefender.Model;

import org.aegisdefender.Model.Entities.Enemies.Enemy;
import org.aegisdefender.Model.Entities.Enemies.EnemyFactory;
import org.aegisdefender.Model.Entities.Player;

import org.aegisdefender.Model.Projectiles.PlayerLaser;
import org.aegisdefender.Model.Projectiles.Projectile;

import java.util.ArrayList;
import java.util.List;

public class GameModel{
    private Player player;
    private Projectile projectile;

    private List<Projectile> projectiles;
    private List<GameObserver> observer;

    private List<Enemy>  kamikaze;
    private List<Enemy> posamine;
    private List<List<Enemy>> enemies;

    //------------//
    public GameModel(){
        player = new Player();
        projectiles  = new ArrayList<>();

        EnemyFactory enemyFactory = new EnemyFactory();
        kamikaze = enemyFactory.createEnemyGroup(EnemyFactory.EnemyType.KAMIKAZE,6,3);
        posamine = enemyFactory.createEnemyGroup(EnemyFactory.EnemyType.POSAMINE , 3 ,2); // le pattern ne sera pas utilisé

        //enemies list of list
        enemies = new ArrayList<>();
        enemies.add(kamikaze);
        enemies.add(posamine);

        observer = new ArrayList<>();
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
            ob.updateProjectiles(new ArrayList<>(projectiles));
        }
    }

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
       projectile = new PlayerLaser(player);
       projectiles.add(projectile);
    }

    // logic to move the playerlaser
    public void updateProjectiles() {
        for (int i = 0; i < projectiles.size(); i++) {
            projectile = projectiles.get(i);
            projectile.setLaserY(projectile.getLaserY() + projectile.getSpeed());
            if (projectile.getLaserY() < - 50) {
                projectiles.remove(i);
                i--;
            }
        }
        notifyObserver();
    }

    public List<Enemy> getKamikaze(){
        return new ArrayList<>(kamikaze);
    }
    public List<Enemy> getPosamine(){return  new ArrayList<>(posamine);}


    public void updateEnemy(){
        for(Enemy enemy :  posamine){
            enemy.update(player); //
        }
    }
}
