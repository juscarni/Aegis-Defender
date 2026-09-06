
package org.aegisdefender.view;

import javax.swing.JFrame;

public class GameFrame extends JFrame {
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

    public static GameFrame getInstance() {
        return instance;
    }

    public MainMenuPanel getMainMenuPanel(){
        return this.mainMenuPanel;
    }
}
