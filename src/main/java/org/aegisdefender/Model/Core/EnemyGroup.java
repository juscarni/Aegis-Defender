package org.aegisdefender.Model.Core;

import org.aegisdefender.Model.Entities.Enemies.EnemyFactory;
public record EnemyGroup(EnemyFactory.EnemyType type, int count, int pattern, double spawnDelay){}
