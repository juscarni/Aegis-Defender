package org.aegisdefender.View;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class ButtonGradient extends JButton {

    // ── Palette PRIMARY (PLAY) ────────────────────────────────────────
    private static final Color P_BG     = new Color(15, 37, 64);
    private static final Color P_BORDER = new Color(0, 200, 255);
    private static final Color P_TEXT   = new Color(0, 200, 255);

    // ── Palette SECONDARY (HOW TO PLAY, SETTINGS, CREDITS) ───────────
    private static final Color S_BG     = new Color(20, 25, 38);
    private static final Color S_BORDER = new Color(74, 86, 128);
    private static final Color S_TEXT   = new Color(168, 180, 208);

    // ── Palette DANGER (EXIT) ─────────────────────────────────────────
    private static final Color D_BG     = new Color(20, 10, 10);
    private static final Color D_BORDER = new Color(100, 30, 30);
    private static final Color D_TEXT   = new Color(160, 70, 70);

    // ── État ──────────────────────────────────────────────────────────
    private float   hoverAlpha = 0f;
    private float   pressAlpha = 0f;
    private float   pressSize  = 0f;
    private Point   pressPoint = new Point();
    private boolean mouseOver  = false;
    private boolean pressing   = false;

    private Timer hoverTimer;
    private Timer rippleTimer;

    public ButtonGradient() {
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setBorder(new EmptyBorder(13, 28, 13, 28));

        // Timer hover — classe anonyme pour éviter la référence avant assignation
        hoverTimer = new Timer(16, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hoverAlpha += mouseOver ? 0.08f : -0.06f;
                if (hoverAlpha < 0f) hoverAlpha = 0f;
                if (hoverAlpha > 1f) hoverAlpha = 1f;
                repaint();
                if (hoverAlpha == 0f || hoverAlpha == 1f) {
                    hoverTimer.stop();
                }
            }
        });

        // Timer ripple — idem
        rippleTimer = new Timer(12, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pressSize += 7f;
                pressAlpha -= 0.035f;
                if (pressAlpha < 0f) pressAlpha = 0f;
                repaint();
                if (pressAlpha <= 0f) {
                    pressing = false;
                    rippleTimer.stop();
                }
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                mouseOver = true;
                hoverTimer.start();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                mouseOver = false;
                hoverTimer.start();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                pressPoint = e.getPoint();
                pressSize  = 0f;
                pressAlpha = 0.4f;
                pressing   = true;
                rippleTimer.restart();
            }
        });
    }

    // ── Détection automatique du style selon le texte du bouton ──────
    private Color[] resolveColors() {
        String t = getText() == null ? "" : getText().toUpperCase();
        if (t.equals("PLAY")) return new Color[]{P_BG, P_BORDER, P_TEXT};
        if (t.equals("EXIT")) return new Color[]{D_BG, D_BORDER, D_TEXT};
        return new Color[]{S_BG, S_BORDER, S_TEXT};
    }

    // ── Rendu ─────────────────────────────────────────────────────────
    @Override
    protected void paintComponent(Graphics g) {
        int W = getWidth();
        int H = getHeight();

        // Sécurité : évite un BufferedImage de taille 0
        if (W <= 0 || H <= 0) return;

        Color[] colors = resolveColors();
        Color bg     = colors[0];
        Color border = colors[1];
        Color text   = colors[2];
        int   arc    = H; // forme pill

        BufferedImage img = new BufferedImage(W, H, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,      RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // 1. Fond de base
        g2.setColor(bg);
        g2.fillRoundRect(0, 0, W, H, arc, arc);

        // 2. Voile blanc au hover
        if (hoverAlpha > 0f) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, hoverAlpha * 0.09f));
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 0, W, H, arc, arc);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
        }

        // 3. Brillance en haut du bouton
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.06f));
        g2.setPaint(new GradientPaint(0, 0, Color.WHITE, 0, H / 2f, new Color(255, 255, 255, 0)));
        g2.fillRoundRect(2, 2, W - 4, H / 2, arc, arc);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));

        // 4. Ripple au clic
        if (pressing && pressAlpha > 0f) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, pressAlpha));
            g2.setColor(Color.WHITE);
            int r = (int) pressSize;
            g2.fillOval(pressPoint.x - r, pressPoint.y - r, r * 2, r * 2);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
        }

        // 5. Lueur de bordure au hover
        if (hoverAlpha > 0f) {
            g2.setColor(new Color(border.getRed(), border.getGreen(), border.getBlue(), (int)(70 * hoverAlpha)));
            g2.setStroke(new BasicStroke(4f));
            g2.drawRoundRect(2, 2, W - 4, H - 4, arc, arc);
        }

        // 6. Bordure nette
        g2.setColor(border);
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(1, 1, W - 2, H - 2, arc, arc);

        // 7. Texte centré avec letter-spacing
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        g2.setFont(getFont());
        g2.setColor(text);
        drawSpacedText(g2, getText(), W, H);

        g2.dispose();
        ((Graphics2D) g).drawImage(img, 0, 0, null);
    }

    private void drawSpacedText(Graphics2D g2, String label, int W, int H) {
        if (label == null || label.isEmpty()) return;
        FontMetrics fm  = g2.getFontMetrics();
        int spacing     = 2;
        int totalW      = fm.stringWidth(label) + spacing * (label.length() - 1);
        int x           = (W - totalW) / 2;
        int y           = (H - fm.getHeight()) / 2 + fm.getAscent();
        for (char ch : label.toCharArray()) {
            g2.drawString(String.valueOf(ch), x, y);
            x += fm.charWidth(ch) + spacing;
        }
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();
        return new Dimension(Math.max(d.width, 260), Math.max(d.height, 50));
    }
}