package org.aegisdefender.Model.Projectiles;

import java.awt.Rectangle;

public abstract class Projectile {
    protected int projectileX;
    protected int projectileY;
    protected int projectileWidth;
    protected int projectileHeight;
    protected int projectileSpeed;
    protected String type;

    public int getLaserX(){
            return this.projectileX;
    }
    public int getLaserY(){
        return this.projectileY;
    }
    public int getLaserWidth(){
        return this.projectileWidth;
    }
    public int getLaserHeight(){return this.projectileHeight;}

    public void setLaserY(int y){
        this.projectileY = y;
    }
    public int  getSpeed(){return this.projectileSpeed;}

    public Rectangle getProjectileHitBox() {
        return new Rectangle(
                getLaserX()+ 3,
                getLaserY()+ 2,
                getLaserWidth()- 2,
                getLaserHeight()-2
        );
    }
    public String getType(){
        return type;
    }
}
