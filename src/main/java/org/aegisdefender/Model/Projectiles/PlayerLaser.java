package org.aegisdefender.Model.Projectiles;

import org.aegisdefender.Config.GameConfig;
import org.aegisdefender.Config.UIConfig;
import org.aegisdefender.Model.Entities.Player;

import java.awt.Rectangle;

public class PlayerLaser  extends Projectile{

    public PlayerLaser(Player player){
        this.x = player.getX() + GameConfig.LASER_OFFSET_PLAYERX;
        this.y = player.getY() +  GameConfig.LASER_OFFSET_PLAYERY;
        this.width = UIConfig.TILES/4;
        this.height = UIConfig.TILES/2;
        this.speed = - 25;
    }

    @Override
    public Rectangle getHitBox() {
        return new Rectangle(
                getLaserX() + 3,
                getLaserY() + 2,
                getLaserWidth() - 6,
                getLaserHeight() - 2);
    }
}
