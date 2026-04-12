package org.aegisdefender.Model;

import org.aegisdefender.Model.Projectiles.Projectile;

import java.util.List;

public interface GameObserver {
    void updatePlayer(int x , int y, int width, int height);
    void updateProjectiles(List<Projectile> projectiles);
}
