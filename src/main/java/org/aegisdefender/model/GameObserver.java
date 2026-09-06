package org.aegisdefender.model;

import org.aegisdefender.model.Projectiles.Projectile;

import java.util.List;

public interface GameObserver {
    void updateProjectiles(List<Projectile> projectiles);
    void updatePlayer(int x, int y, int width, int height);
}
