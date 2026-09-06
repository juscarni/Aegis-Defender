package org.aegisdefender.model.Core;

import org.aegisdefender.model.Entities.Enemies.EnemyFactory;
public record EnemyGroup(EnemyFactory.EnemyType type, int count, int pattern, double spawnDelay){}
