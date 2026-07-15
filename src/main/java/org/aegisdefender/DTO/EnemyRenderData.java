package org.aegisdefender.DTO;

import java.awt.Rectangle;
    public record EnemyRenderData(int x , int y , int width, int height, Rectangle hitbox, String type, Rectangle healthBar, int currentHealth, boolean isExploding){ }
