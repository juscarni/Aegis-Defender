package org.aegisdefender.dto;

import java.awt.Rectangle;
    public record ProjectileRenderData(int x , int y , int width, int height, Rectangle hitbox, String type){}
