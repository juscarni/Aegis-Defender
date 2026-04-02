package org.aegisdefender.Config;

public class UIConfig {

    public final int TILES = 40;
    public final int LASER_OFFSET_PLAYERX = 7;
    public final int LASER_OFFSET_PLAYERY = TILES - 20;
    public final  int FPS = 30;

    public final int ROWS  = 18;
    public final int COLS = 14;

    public final int WINDOW_WIDTH =  COLS * TILES;// 560px
    public final int WINDOW_HEIGHT = ROWS * TILES;; // 720px

    public final int DRAW_OFFSET_X = 45;
    public final int DRAW_OFFSET_Y = 65;


    public static final int CURSOR_IMG_SIZE = 16;
    // Point d'ancrage du curseur (0,0 = coin supérieur gauche)
    public static final int HOTSPOT_X = 0;
    public static final int HOTSPOT_Y = 0;
    public static final String CURSOR_NAME = "blank cursor";
}
