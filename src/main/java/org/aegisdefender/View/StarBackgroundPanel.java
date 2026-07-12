package org.aegisdefender.View;

import org.aegisdefender.Config.UIConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class StarBackgroundPanel extends JPanel implements ActionListener {

    private final int NUM_STARS = 100; // Nombre d'étoiles à l'écran
    private int[] starX = new int[NUM_STARS];
    private int[] starY = new int[NUM_STARS];
    private int[] starSpeed = new int[NUM_STARS];
    private int[] starSize = new int[NUM_STARS];
    private Timer timer;

    public StarBackgroundPanel() {
        setBackground(new Color(8, 12, 24));
        Random rand = new Random();

        // Initialisation aléatoire des étoiles
        for (int i = 0; i < NUM_STARS; i++) {
            starX[i] = rand.nextInt(UIConfig.WINDOW_WIDTH);
            starY[i] = rand.nextInt(UIConfig.WINDOW_HEIGHT);
            starSpeed[i] = rand.nextInt(1) + 1; // Vitesse de chute (1 à 3)
            starSize[i] = rand.nextInt(2) + 1;  // Taille de l'étoile (1 à 3 pixels)
        }

        // Le Timer va rafraîchir l'écran toutes les 30 millisecondes (environ 30 FPS)
        timer = new Timer(30, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Dessine le fond noir

        // Dessine toutes les étoiles
        g.setColor(Color.WHITE);
        for (int i = 0; i < NUM_STARS; i++) {
            // J'utilise fillRect pour un effet "Pixel Art" carré, tu peux utiliser fillOval pour des ronds
            g.fillRect(starX[i], starY[i], starSize[i], starSize[i]);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Met à jour la position des étoiles
        for (int i = 0; i < NUM_STARS; i++) {
            starY[i] += starSpeed[i]; // L'étoile descend

            // Si l'étoile sort de l'écran en bas, on la replace tout en haut
            if (starY[i] > getHeight()) {
                starY[i] = 0;
                starX[i] = new Random().nextInt(getWidth()); // A une nouvelle position X
            }
        }
        repaint(); // Demande à Swing de redessiner le panneau
    }
}