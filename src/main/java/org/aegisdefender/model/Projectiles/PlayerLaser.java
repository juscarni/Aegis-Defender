package org.aegisdefender.model.Projectiles;

import org.aegisdefender.config.GameConfig;
import org.aegisdefender.config.UIConfig;
import org.aegisdefender.model.Entities.Player;

import java.awt.Rectangle;

public class PlayerLaser  extends Projectile{

    public PlayerLaser(Player player){
        this.projectileX = player.getX() + GameConfig.LASER_OFFSET_PLAYERX;
        this.projectileY = player.getY() +  GameConfig.LASER_OFFSET_PLAYERY;
        this.projectileWidth = UIConfig.TILES/4;
        this.projectileHeight = UIConfig.TILES/2;
        this.projectileSpeed = - 25;
    }

    @Override
    public Rectangle getProjectileHitBox() {
        return new Rectangle(
                getLaserX() + 3,
                getLaserY() + 2,
                getLaserWidth() - 6,
                getLaserHeight() - 2);
    }
}
