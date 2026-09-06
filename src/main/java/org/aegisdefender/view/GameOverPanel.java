package org.aegisdefender.view;

import org.aegisdefender.config.UIConfig;

import javax.swing.*;
import java.awt.*;

public class GameOverPanel extends JPanel {

    private final JLabel scoreValue  = new JLabel("0",  SwingConstants.CENTER);
    private final JLabel waveValue   = new JLabel("0",  SwingConstants.CENTER);
    private final JLabel killsValue  = new JLabel("0",  SwingConstants.CENTER);
    private final JLabel bestValue   = new JLabel("0",  SwingConstants.CENTER);

    public GameOverPanel() {
        setOpaque(false);
        setPreferredSize(new Dimension(UIConfig.WINDOW_WIDTH, UIConfig.WINDOW_HEIGHT));
        setLayout(new GridBagLayout());
        add(buildCard());
    }

    private JPanel buildCard() {
        JPanel card = new JPanel();
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setMaximumSize(new Dimension(360, 500));

        card.add(buildTitle());
        card.add(Box.createVerticalStrut(24));
        card.add(buildStats());
        card.add(Box.createVerticalStrut(12));
        card.add(buildBestScore());
        card.add(Box.createVerticalStrut(28));
        card.add(buildButtons());

        return card;
    }

    private JPanel buildTitle() {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        JLabel sub = new JLabel("MISSION FAILED", SwingConstants.CENTER);
        sub.setFont(new Font("Arial", Font.BOLD, 11));
        sub.setForeground(new Color(162, 45, 45));
        sub.setAlignmentX(CENTER_ALIGNMENT);

        JLabel title = new JLabel("GAME OVER", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 46));
        title.setForeground(new Color(240, 149, 149));
        title.setAlignmentX(CENTER_ALIGNMENT);

        p.add(sub);
        p.add(title);
        return p;
    }

    private JPanel buildStats() {
        JPanel p = new JPanel(new GridLayout(1, 3, 10, 0));
        p.setOpaque(false);
        p.add(statCard("SCORE",  scoreValue));
        p.add(statCard("WAVE",   waveValue));
        p.add(statCard("KILLS",  killsValue));
        return p;
    }

    private JPanel statCard(String label, JLabel valueLabel) {
        JPanel card = new JPanel();
        card.setOpaque(true);
        card.setBackground(new Color(255, 255, 255, 13));
        card.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 25), 1));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel lbl = new JLabel(label, SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.PLAIN, 10));
        lbl.setForeground(new Color(136, 135, 128));
        lbl.setAlignmentX(CENTER_ALIGNMENT);

        valueLabel.setFont(new Font("Arial", Font.BOLD, 22));
        valueLabel.setForeground(new Color(241, 239, 232));
        valueLabel.setAlignmentX(CENTER_ALIGNMENT);

        card.add(Box.createVerticalStrut(10));
        card.add(lbl);
        card.add(Box.createVerticalStrut(4));
        card.add(valueLabel);
        card.add(Box.createVerticalStrut(10));
        return card;
    }

    private JPanel buildBestScore() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(true);
        p.setBackground(new Color(255, 255, 255, 10));
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 20), 1),
                BorderFactory.createEmptyBorder(10, 16, 10, 16)
        ));

        JLabel lbl = new JLabel("Best score");
        lbl.setFont(new Font("Arial", Font.PLAIN, 12));
        lbl.setForeground(new Color(136, 135, 128));

        bestValue.setFont(new Font("Arial", Font.BOLD, 14));
        bestValue.setForeground(new Color(250, 199, 117));

        p.add(lbl, BorderLayout.WEST);
        p.add(bestValue, BorderLayout.EAST);
        return p;
    }

    private JPanel buildButtons() {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        ButtonGradient replay = new ButtonGradient();
        replay.setText("PLAY");
        replay.setFont(new Font("Arial", Font.BOLD, 14));
        replay.setAlignmentX(CENTER_ALIGNMENT);
        replay.addActionListener(e -> GameFrame.getInstance().getMainMenuPanel().restartGame());

        ButtonGradient menu = new ButtonGradient();
        menu.setText("MAIN MENU");
        menu.setFont(new Font("Arial", Font.BOLD, 14));
        menu.setAlignmentX(CENTER_ALIGNMENT);
        menu.addActionListener(e -> GameFrame.getInstance().getMainMenuPanel().showMainMenuPanel());

        p.add(replay);
        p.add(Box.createVerticalStrut(10));
        p.add(menu);
        return p;
    }

    // ── Setters appelés par le contrôleur ────────────────────────────

    public void setStats(int score, int wave, int kills, int best) {
        scoreValue.setText(String.valueOf(score));
        waveValue.setText(String.valueOf(wave));
        killsValue.setText(String.valueOf(kills));
        bestValue.setText(String.valueOf(best));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(new Color(8, 12, 24, 210));
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();
        super.paintComponent(g);
    }
}