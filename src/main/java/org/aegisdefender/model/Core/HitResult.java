package org.aegisdefender.model.Core;

import org.aegisdefender.model.Entities.Enemies.Enemy;
import org.aegisdefender.model.Projectiles.Projectile;

import java.awt.Point;
public record HitResult(Projectile projectile, Enemy enemy, Point impactPoint) {
}