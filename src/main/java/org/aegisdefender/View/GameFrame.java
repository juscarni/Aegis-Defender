
package org.aegisdefender.View;

import org.aegisdefender.Config.UIConfig;

import javax.swing.JFrame;

import java.awt.Toolkit;
import java.awt.Cursor;
import java.awt.image.BufferedImage;
import java.awt.Point;

public class GameFrame extends JFrame {
    private GamePanel gamepanel;

    //-----
    public GameFrame(){
        gamepanel = new GamePanel();
        this.add(gamepanel);
        this.pack();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.requestFocus();
        this.requestFocusInWindow();
        this.setCursor(setCursorInvisible());// get an invisible cursor
    }


    public GamePanel getGamePanel(){
        return gamepanel;
    }

    public Cursor setCursorInvisible(){
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        BufferedImage image = new BufferedImage(UIConfig.CURSOR_IMG_SIZE, UIConfig.CURSOR_IMG_SIZE, BufferedImage.TYPE_INT_ARGB);
        return toolkit.createCustomCursor(
                image,
                new Point(UIConfig.HOTSPOT_X,UIConfig.HOTSPOT_Y),
                UIConfig.CURSOR_NAME
        );
    }

}
