package org.aegisdefender.Model.Projectiles;

import java.awt.Rectangle;

public abstract class Projectile {
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected int speed;

    public int getLaserX(){
            return this.x;
    }
    public int getLaserY(){
        return this.y;
    }

    public int getLaserWidth(){
        return this.width;
    }
    public int getLaserHeight(){
        return this.height;
    }

    public void setLaserY(int y){
        this.y = y;
    }
    public int  getSpeed(){return this.speed;}

    public abstract Rectangle getHitBox();
}
