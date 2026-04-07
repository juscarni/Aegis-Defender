package org.aegisdefender.Model.Projectiles;

import java.awt.*;

public class PlayerLaser  extends Projectile{

    public final int laserSpeedY = - 5;

    public PlayerLaser(int x, int y, int width,int height){
        super(x , y, width, height);
    }

    @Override
    public int getSpeed() {
        return this.laserSpeedY;
    }

    @Override
    public Rectangle getHitBox() {
        return new Rectangle(
                getPositionLaserX() + 3,
                getPositionLaserY() + 2,
                getLaserWidth() - 6,
                getLaserHeight() - 2);
    }
}
