package org.aegisdefender.View;

import javax.swing.*;

public class GameFrame extends JFrame {
    private final GamePanel panel = new GamePanel();

    public GameFrame(){
        this.add(panel);
        this.pack();
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
    }
}
