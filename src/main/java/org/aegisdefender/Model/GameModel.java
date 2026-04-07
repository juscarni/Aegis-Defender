package org.aegisdefender.Model;

import org.aegisdefender.Model.Entities.Player;
import org.aegisdefender.Model.Projectiles.PlayerLaser;
import org.aegisdefender.Model.Projectiles.Projectile;

import java.util.ArrayList;
import java.util.List;

public class GameModel{

    private Player player;
    private Projectile projectile;
    private PlayerLaser laser;
    private List<Projectile> projectiles;

    private List<GameObserver> observer;

    //------------//
    public GameModel(){
        player = new Player();
        projectiles  = new ArrayList<>();
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
            ob.updatePlayerPosition(player.getX(),player.getY());
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

    public void setPlayerProjectile(int TILE , int offSetX, int offSetY){
       projectile = new PlayerLaser(player.getX() - offSetX,
                                    player.getY() - offSetY,
                                    TILE/4,TILE/2);
       projectiles.add(projectile);
    }

    // logic to move the playerlaser
    public void updateProjectiles() {
        for (int i = 0; i < projectiles.size(); i++) {
            projectile = projectiles.get(i);
            projectile.setPositionLaserY(projectile.getPositionLaserY() + projectile.getSpeed());
            if (projectile.getPositionLaserY() < - 50) {
                projectiles.remove(i);
                i--;
            }
        }
        notifyObserver();
    }
}
