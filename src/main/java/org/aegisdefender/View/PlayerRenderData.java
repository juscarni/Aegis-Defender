package org.aegisdefender.View;

import java.awt.*;

public record PlayerRenderData(int x, int y, int width, int height, Rectangle hitbox, Rectangle healthBar, int currentHealth, boolean isInCoolDown, boolean isPlayerAlive){};
