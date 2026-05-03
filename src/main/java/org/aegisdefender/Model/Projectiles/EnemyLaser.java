package org.aegisdefender.Model.Projectiles;

import java.awt.*;

public class EnemyLaser extends Projectile{

    public EnemyLaser(int x, int y, int width, int height,String type){
        this.projectileX = x;
        this.projectileY = y;
        this.projectileWidth = width;
        this.projectileHeight = height;
        this.type = type;
    }

    @Override
    public Rectangle getProjectileHitBox() {
        return new Rectangle(
                getLaserX()+3,
                getLaserY()+2,
                getLaserWidth()-2,
                getLaserHeight()-2
        );
    }
}
