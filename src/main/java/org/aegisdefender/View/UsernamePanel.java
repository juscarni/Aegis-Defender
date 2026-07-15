package org.aegisdefender.View;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;

public class UsernamePanel extends StarBackgroundPanel {

    private  JTextField inputField;

    public UsernamePanel() {
        setLayout(new GridBagLayout());
        add(buildCard());
    }

    private JPanel buildCard() {
        JPanel card = new JPanel();
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setMaximumSize(new Dimension(320, 400));

        //card.setBorder(BorderFactory.createLineBorder(Color.CYAN, 1));

        card.add(buildTitle());
        card.add(Box.createVerticalStrut(32));
        card.add(buildInput());
        card.add(Box.createVerticalStrut(24));
        card.add(buildConfirmButton());

        return card;
    }

    private JPanel buildTitle() {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        JLabel gameTitle = new JLabel("AEGIS DEFENDER");
        gameTitle.setFont(loadPressStart(30f));
        gameTitle.setForeground(new Color(255, 184, 0));
        gameTitle.setAlignmentX(CENTER_ALIGNMENT);

        JLabel welcome = new JLabel("WELCOME");
        welcome.setFont(loadPressStart(16f));
        welcome.setForeground(new Color(0, 200, 255, 153));
        welcome.setAlignmentX(CENTER_ALIGNMENT);

        p.add(gameTitle);
        p.add(Box.createVerticalStrut(12));
        p.add(welcome);
        return p;
    }

    private Font loadPressStart(float size) {
        try {
            InputStream is = getClass().getResourceAsStream("/Fonts/PressStart2P-Regular.ttf");
            return Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(Font.BOLD, size);
        } catch (Exception e) {
            return new Font("Arial", Font.BOLD, (int) size);
        }
    }

    private JPanel buildInput() {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        JLabel label = new JLabel("ENTER YOUR CALLSIGN", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.PLAIN, 10));
        label.setForeground(new Color(255, 255, 255, 89));
        label.setAlignmentX(CENTER_ALIGNMENT);

        inputField = new JTextField();
        inputField.setMaximumSize(new Dimension(280, 48));
        inputField.setPreferredSize(new Dimension(280, 48));
        inputField.setBackground(new Color(0, 0, 0, 128));
        inputField.setForeground(new Color(224, 247, 255));
        inputField.setCaretColor(new Color(0, 200, 255));
        inputField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 200, 255, 76), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        inputField.setFont(new Font("Monospaced", Font.BOLD, 18));
        inputField.setHorizontalAlignment(SwingConstants.CENTER);
        inputField.setAlignmentX(CENTER_ALIGNMENT);
        inputField.setDocument(new javax.swing.text.PlainDocument() {
            @Override
            public void insertString(int offs, String str, javax.swing.text.AttributeSet a)
                    throws javax.swing.text.BadLocationException {
                if (str == null) return;
                if ((getLength() + str.length()) <= 12)
                    super.insertString(offs, str, a);
            }
        });

        JLabel hint = new JLabel("Max 12 characters", SwingConstants.CENTER);
        hint.setFont(new Font("Arial", Font.PLAIN, 10));
        hint.setForeground(new Color(255, 255, 255, 51));
        hint.setAlignmentX(CENTER_ALIGNMENT);

        p.add(label);
        p.add(Box.createVerticalStrut(10));
        p.add(inputField);
        p.add(Box.createVerticalStrut(6));
        p.add(hint);
        return p;
    }

    private JPanel buildConfirmButton() {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        JLabel errorLabel = new JLabel("Please enter your callsign");
        errorLabel.setFont(loadPressStart(10f));
        errorLabel.setForeground(new Color(224, 75, 74));
        errorLabel.setAlignmentX(CENTER_ALIGNMENT);
        errorLabel.setVisible(false);

        ButtonGradient confirm = new ButtonGradient();
        confirm.setText("PLAY");
        confirm.setFont(new Font("Arial", Font.BOLD, 14));
        confirm.setAlignmentX(CENTER_ALIGNMENT);
        confirm.addActionListener(e -> {
            if (!inputField.getText().trim().isEmpty()){
                GameFrame.getInstance().getMainMenuPanel().showMainMenuPanel();
            }else{
                errorLabel.setVisible(true);
            }
        });

        p.add(errorLabel);
        p.add(Box.createVerticalStrut(10));
        p.add(confirm);
        return p;
    }

    public String getUsername(){ return inputField.getText().trim().toLowerCase(); }
}