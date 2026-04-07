package org.aegisdefender.Model.Projectiles;

import java.awt.Rectangle;

public abstract class Projectile {

    private int positionLaserX;
    private int positionLaserY;
    private int laserWidth;
    private int laserHeight;
    private int speed;

    public Projectile(int x , int y, int width, int height){
        this.positionLaserX = x;
        this.positionLaserY = y;
        this.laserWidth = width;
        this.laserHeight = height;
    }
    public int getPositionLaserX(){
        return this.positionLaserX;
    }
    public int getPositionLaserY(){
        return this.positionLaserY;
    }
    public int getLaserWidth(){
        return this.laserWidth;
    }
    public int getLaserHeight(){
        return this.laserHeight;
    }
    public void setPositionLaserY(int y){
        this.positionLaserY = y;
    }
    public abstract int  getSpeed();
    public abstract Rectangle getHitBox();
}
