
package org.aegisdefender.View;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GameFrame extends JFrame {

    // Dimensions de l'image tampon (dummy image) pour le curseur
    private static final int CURSOR_IMG_SIZE = 16;
    // Point d'ancrage du curseur (0,0 = coin supérieur gauche)
    private static final int HOTSPOT_X = 0;
    private static final int HOTSPOT_Y = 0;

    private static final String CURSOR_NAME = "blank cursor";

    private GamePanel gamepanel = null;

    public GameFrame(){
        gamepanel = new GamePanel();
        this.add(gamepanel);
        this.pack();
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.requestFocus();
        this.requestFocusInWindow();
        this.setCursor(setCursorInvisible());// get an invisible cursor
    }


    public GamePanel gamePanelInstance(){
        return gamepanel;
    }

    public Cursor setCursorInvisible(){
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        BufferedImage image = new BufferedImage(CURSOR_IMG_SIZE, CURSOR_IMG_SIZE, BufferedImage.TYPE_INT_ARGB);
        return toolkit.createCustomCursor(
                image,
                new Point(HOTSPOT_X,HOTSPOT_Y),
                CURSOR_NAME
        );
    }

}
