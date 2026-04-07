package org.aegisdefender.View;

import java.awt.*;

public class EntityRenderData {

    public final int x;
    public final int y;
    public final int width;
    public final int height;
    public final Rectangle hitbox;

    public EntityRenderData(int x, int y, int width, int height, Rectangle hitbox) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.hitbox = hitbox;
    }
}