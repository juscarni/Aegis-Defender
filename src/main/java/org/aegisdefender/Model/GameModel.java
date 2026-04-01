package org.aegisdefender.Model;

import org.aegisdefender.Model.Entities.Player;
import org.aegisdefender.Model.Projectiles.PlayerLaser;
import org.aegisdefender.Model.Projectiles.Projectile;

import java.util.ArrayList;
import java.util.List;

public class GameModel{

    private Player player;
    private Projectile projectile = null;
    private List<PlayerLaser> playerProjectile;

    public GameModel(){
        player = new Player();
        playerProjectile = new ArrayList<PlayerLaser>();
    }

    public void setPLayerX(int x){
        player.setX(x);
    }

    public void setPlayerY(int y){
        player.setY(y);
    }

    public int getPlayerX(){
        return player.getX();
    }

    public int getPlayerY(){
        return player.getY();
    }
    // create player projectiles
    public void setPlayerProjectile(int TILE , int offSetX, int offSetY){
       projectile = new PlayerLaser(getPlayerX() - offSetX,
                                    getPlayerY() - offSetY,
                                    TILE/8,TILE/3);
       playerProjectile.add((PlayerLaser)projectile);
    }
    public List<PlayerLaser> getPlayerProjectile(){
        return playerProjectile;
    }

    // logic to move the playerlaser
    public void updateProjectiles() {
        System.out.println(playerProjectile.size()); //test

        for (int i = 0; i < playerProjectile.size(); i++) {
            PlayerLaser laser = playerProjectile.get(i);
            laser.setPositionLaserY(laser.getPositionLaserY() + laser.getSpeedY());
            if (laser.getPositionLaserY() < - 50) {
                playerProjectile.remove(i);
                i--;
            }
        }
    }
}
