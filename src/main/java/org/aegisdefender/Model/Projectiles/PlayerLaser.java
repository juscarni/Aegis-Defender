package org.aegisdefender.Model.Projectiles;

public class PlayerLaser  extends Projectile{

    public final int laserSpeedY = - 10;

    public PlayerLaser(int x, int y, int width,int height){
        super(x , y, width, height);
    }
    public final int getSpeedY(){
        return this.laserSpeedY;
    }
}
