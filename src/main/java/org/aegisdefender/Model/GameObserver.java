package org.aegisdefender.Model;

import org.aegisdefender.Model.Projectiles.Projectile;

import java.util.List;

public interface GameObserver {
    void updatePlayerPosition(int x , int y);
    void updateProjectiles(List<Projectile> projectiles);
}
