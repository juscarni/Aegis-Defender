
package org.aegisdefender.View;

import org.aegisdefender.Config.UIConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GameFrame extends JFrame {
    private GamePanel gamepanel;
    private MainMenuPanel mainMenuPanel;
    private static GameFrame instance;

    //-----
    public GameFrame(){
        this.setTitle("aegisdefender");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        initGame();
        this.pack();
        this.setLocationRelativeTo(null);
        this.requestFocus();
        this.requestFocusInWindow();
        //this.setCursor(setCursorInvisible());// get an invisible cursor
        this.setResizable(false);

        this.setVisible(true);
        instance = this;
    }

    public void initGame(){
        mainMenuPanel = new MainMenuPanel();
        this.add(mainMenuPanel.menuPanel());
    }


    public GamePanel getGamePanel(){
        return this.mainMenuPanel.getGamePanel();
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

    public static GameFrame getInstance() {
        return instance;
    }

    public MainMenuPanel getMainMenuPanel(){
        return this.mainMenuPanel;
    }
}
