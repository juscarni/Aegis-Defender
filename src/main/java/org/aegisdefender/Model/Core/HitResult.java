package org.aegisdefender.Model.Core;

import org.aegisdefender.Model.Entities.Enemies.Enemy;
import org.aegisdefender.Model.Projectiles.Projectile;

import java.awt.Point;
public record HitResult(Projectile projectile, Enemy enemy, Point impactPoint) {
}