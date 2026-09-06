package org.aegisdefender.view;

import org.aegisdefender.config.UIConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainMenuPanel {

    private GamePanel gamePanel;
    private JPanel howToPlayPanel;
    private JPanel settingsPanel;
    private JPanel creditsPanel;
    private GameOverPanel gameOverPanel;
    private UsernamePanel usernamePanel;

    public ImageIcon[] icons;

    private JButton button;
    private CardLayout cardLayout;
    private JPanel menu;       // the main container (CardLayout)
    private JPanel menuButton; // the button panel (BoxLayout)
    private Font customFont;

    private Runnable onPlayCallback;
    private int volumeSlider;
    private List<String[]> playerData;


    public MainMenuPanel() {
        icons = new ImageIcon[5];
        cardLayout = new CardLayout();

        menu = new StarBackgroundPanel();
        menu.setLayout(cardLayout);

        menuButton = new JPanel();

        //loadIconsImages();
        this.playerData = new ArrayList<>();
    }

    public JPanel menuPanel() {
        menu.setPreferredSize(new Dimension(UIConfig.WINDOW_WIDTH, UIConfig.WINDOW_HEIGHT));
        menuButton.setOpaque(false);

        //configuration of button panel
        menuButton.setLayout(new BoxLayout(menuButton, BoxLayout.Y_AXIS));
        menuButton.setBackground(Color.black);

        menuButton.add(Box.createVerticalStrut(40));

        menuButton.add(createGameTitle()); // title
        menuButton.add(Box.createVerticalStrut(20)); // space
        menuButton.add(createTagline()); // subtitle

        menuButton.add(Box.createVerticalStrut(170)); //space between the title and buttons

        String[] menuItems = {"PLAY", "HOW TO PLAY", "HIGH SCORES", "SETTINGS", "CREDITS", "EXIT"};

        for (String menuItem : menuItems) {
            button = new ButtonGradient();
            button.setText(menuItem);
            //button.setIcon(icons[i]);
            button.setBackground(new Color(255, 174, 0));
            button.setForeground(new Color(255, 174, 0));
            button.setFont(customFont.deriveFont(Font.PLAIN, 15f));
            button.setFocusable(false);
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setMaximumSize(new Dimension(300, 80));
            button.addActionListener(this::showCardLayout);

            menuButton.add(button);
            menuButton.add(Box.createVerticalStrut(15));
        }

        menu.add(menuButton, "mainMenu");
        menu.add(playPanel(), "play");
        menu.add(howToPlayPanel(), "howToPlay");
        menu.add(settingsPanel(), "settings");
        menu.add(creditsPanel(), "credits");

        //System.out.println("test " +this.playerData); for debug
        gameOverPanel = new GameOverPanel();  // ---

        menu.add(gameOverPanel,"gameOverPanel");
        usernamePanel = new UsernamePanel();
        menu.add(usernamePanel,"usernamePanel");

        // display the UsernamePanel at first
        cardLayout.show(menu, "usernamePanel");

        return menu;
    }

    private void showCardLayout(ActionEvent actionEvent) {
        String item = actionEvent.getActionCommand();

        switch (item) {
            case "PLAY" -> {
                cardLayout.show(menu, "play");
                if(this.onPlayCallback != null) {
                    this.onPlayCallback.run();
                }
            }
            case "HOW TO PLAY" -> cardLayout.show(menu, "howToPlay");
            case "HIGH SCORES" -> cardLayout.show(menu, "highScore");//--
            case "SETTINGS" -> cardLayout.show(menu, "settings");
            case "CREDITS" -> cardLayout.show(menu, "credits");
            case "EXIT" -> System.exit(0);
            default -> System.out.println("Invalid item");
        }
    }

    public JPanel playPanel() {
        gamePanel = new GamePanel(); // -- it works as I want
        return gamePanel;
    }

    public JPanel howToPlayPanel() {
        howToPlayPanel = new StarBackgroundPanel();
        howToPlayPanel.setLayout(new BorderLayout());

        // ── Contenu central ─────────────────────────────────────────────
        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60)); //padding

        // Title
        JLabel title = new JLabel("HOW TO PLAY");
        title.setFont(customFont.deriveFont(Font.BOLD, 18f));
        title.setForeground(new Color(255, 184, 0));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(title);
        content.add(Box.createVerticalStrut(30));
        // ── CONTROLS ──────────────────────────────────────────────────────
        content.add(createLabel("— CONTROLS —", 10f, new Color(0, 200, 255)));
        content.add(Box.createVerticalStrut(10));
        content.add(createLabel("MOUSE MOVE    →   Move Aegis left / right", 10f, Color.WHITE));
        content.add(Box.createVerticalStrut(6));
        content.add(createLabel("LEFT CLICK      →   Attack : fire laser", 10f, Color.WHITE));
        content.add(Box.createVerticalStrut(6));
        content.add(createLabel("RIGHT CLICK   →   Defense : energy shield", 10f, Color.WHITE));
        content.add(Box.createVerticalStrut(24));

        // ── HEAT SYSTEM ───────────────────────────────────────────────────
        content.add(createLabel("— HEAT SYSTEM —", 10f, new Color(0, 200, 255)));
        content.add(Box.createVerticalStrut(10));
        content.add(createLabel("• Attack generates moderate heat", 10f, new Color(168, 180, 208)));
        content.add(Box.createVerticalStrut(6));
        content.add(createLabel("• Defense generates high heat", 10f, new Color(255, 140, 0)));
        content.add(Box.createVerticalStrut(6));
        content.add(createLabel("• At 100% heat  →  cooldown, no abilities", 10f, new Color(220, 60, 60)));
        content.add(Box.createVerticalStrut(24));

        // ── PARRY ─────────────────────────────────────────────────────────
        content.add(createLabel("— PARRY MECHANIC —", 10f, new Color(0, 200, 255)));
        content.add(Box.createVerticalStrut(10));
        content.add(createLabel("• Activate shield just before an enemy laser hits", 10f, new Color(168, 180, 208)));
        content.add(Box.createVerticalStrut(6));
        content.add(createLabel("• Destroys projectile + fires boosted laser back", 10f, new Color(60, 200, 120)));
        content.add(Box.createVerticalStrut(6));
        content.add(createLabel("• Boosted lasers destroy armored Berserkers", 10f, new Color(60, 200, 120)));
        content.add(Box.createVerticalStrut(24));

        // ── ENEMIES ───────────────────────────────────────────────────────
        content.add(createLabel("— ENEMIES —", 10f, new Color(0, 200, 255)));
        content.add(Box.createVerticalStrut(10));
        content.add(createLabel("KAMIKAZE    →   Crashes into you, use Attack", 10f, new Color(220, 60, 60)));
        content.add(Box.createVerticalStrut(6));
        content.add(createLabel("POSAMINE   →   Fires lasers, use Parry", 10f, new Color(255, 140, 0)));
        content.add(Box.createVerticalStrut(6));
        content.add(createLabel("MINELAYER  →   Drops mines, use Shield push", 10f, new Color(168, 180, 208)));
        content.add(Box.createVerticalStrut(6));
        content.add(createLabel("BERSERKER  →   Armored, only Parry can beat it", 10f, new Color(220, 60, 60)));
        content.add(Box.createVerticalStrut(30));

        // ── Bouton BACK ───────────────────────────────────────────────────
        ButtonGradient back = new ButtonGradient();
        back.setText("< BACK");
        back.setFont(customFont.deriveFont(Font.PLAIN, 10f));
        back.setMaximumSize(new Dimension(200, 50));
        back.setAlignmentX(Component.CENTER_ALIGNMENT);
        back.addActionListener(e -> cardLayout.show(menu, "mainMenu"));
        content.add(back);

        // Scroll
        JScrollPane scroll = new JScrollPane(content);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        SwingUtilities.invokeLater(() -> centerScrollPane(scroll)); //

        howToPlayPanel.add(scroll, BorderLayout.CENTER);
        return howToPlayPanel;
    }

    // Helper
    private JLabel createLabel(String text, float size, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(customFont.deriveFont(Font.PLAIN, size));
        label.setForeground(color);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    public JPanel highScorePanel(List<String[]> scores) {
        JPanel highScorePanel = new StarBackgroundPanel();
        highScorePanel.setLayout(new BorderLayout());

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(40, 40, 30, 40));
        content.setBorder(BorderFactory.createEmptyBorder(30, 80, 30, 80));

        // Titre
        JLabel title = new JLabel("HIGH SCORES");
        title.setFont(customFont.deriveFont(Font.BOLD, 18f));
        title.setForeground(new Color(255, 184, 0));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(title);
        content.add(Box.createVerticalStrut(30));

        // ── En-tête ───────────────────────────────────────────────────────
        content.add(createTableRow("#", "USERNAME", "SCORE", "KILLS", "DATE", true));
        content.add(Box.createVerticalStrut(8));

        // ── Lignes ────────────────────────────────────────────────────────
        if (scores == null || scores.isEmpty()) {
            content.add(Box.createVerticalStrut(20));
            content.add(createLabel("No scores yet. Be the first !", 10f, new Color(168, 180, 208)));
        } else {
            for (int i = 0; i < scores.size(); i++) {
                String[] row = scores.get(i);
                content.add(createTableRow(
                        String.valueOf(i + 1),
                        row[0], // username
                        row[1], // score
                        row[2], // kills
                        row[3], // date
                        false
                ));
                content.add(Box.createVerticalStrut(6));
            }
        }

        content.add(Box.createVerticalStrut(30));

        // ── Bouton BACK ───────────────────────────────────────────────────
        ButtonGradient back = new ButtonGradient();
        back.setText("< BACK");
        back.setFont(customFont.deriveFont(Font.PLAIN, 10f));
        back.setMaximumSize(new Dimension(200, 50));
        back.setAlignmentX(Component.CENTER_ALIGNMENT);
        back.addActionListener(e -> cardLayout.show(menu, "mainMenu"));
        content.add(back);

        JScrollPane scroll = new JScrollPane(content);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        SwingUtilities.invokeLater(() -> centerScrollPane(scroll)); //

        highScorePanel.add(scroll, BorderLayout.CENTER);
        return highScorePanel;
    }

    private JPanel createTableRow(String rank, String username, String score,
                                  String kills, String date, boolean isHeader) {
        JPanel row = new JPanel(new GridLayout(1, 5));
        row.setMaximumSize(new Dimension(500, isHeader ? 32 : 40));
        row.setAlignmentX(Component.CENTER_ALIGNMENT);

        if (isHeader) {
            row.setOpaque(true);
            row.setBackground(new Color(0, 200, 255, 30));
            row.setBorder(BorderFactory.createLineBorder(new Color(0, 200, 255, 100), 1));
        } else {
            row.setOpaque(true);
            row.setBackground(new Color(20, 25, 38, 180));
            row.setBorder(BorderFactory.createLineBorder(new Color(74, 86, 128, 80), 1));
        }

        Color fg = isHeader ? new Color(0, 200, 255) : new Color(168, 180, 208);

        // Top 3 colorés
        if (!isHeader) {
            int r = Integer.parseInt(rank);
            if      (r == 1) fg = new Color(255, 184, 0);    // or
            else if (r == 2) fg = new Color(168, 180, 208);  // argent
            else if (r == 3) fg = new Color(180, 100, 40);   // bronze
        }

        float fontSize = isHeader ? 8f : 9f;
        for (String cell : new String[]{rank, username, score, kills, date}) {
            // this will desplay date and time in the next line
                if(!cell.equalsIgnoreCase(date)) {
                    JLabel lbl = new JLabel(cell, SwingConstants.CENTER);
                    lbl.setFont(customFont.deriveFont(isHeader ? Font.BOLD : Font.PLAIN, fontSize));
                    lbl.setForeground(fg);
                    row.add(lbl);
                }
                else{
                    String str = "<html><div style='text-align:center;'>"
                            + cell.replace("|", "<br>")
                            + "</div></html>";

                    JLabel lbl = new JLabel(str, SwingConstants.CENTER);

                    lbl.setFont(customFont.deriveFont(
                            isHeader ? Font.BOLD : Font.PLAIN,
                            12f
                    ));

                    lbl.setForeground(fg);
                    row.add(lbl);
                }
            }
            return row;
        }

    public JPanel settingsPanel() {
        settingsPanel = new StarBackgroundPanel();
        settingsPanel.setLayout(new BorderLayout());

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(40, 80, 30, 80));

        // Title
        JLabel title = new JLabel("SETTINGS");
        title.setFont(customFont.deriveFont(Font.BOLD, 18f));
        title.setForeground(new Color(255, 184, 0));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(title);
        content.add(Box.createVerticalStrut(40));

        // ── DIFFICULTY ────────────────────────────────────────────────────
        content.add(createLabel("— DIFFICULTY —", 10f, new Color(0, 200, 255)));
        content.add(Box.createVerticalStrut(14));

        JPanel diffPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        diffPanel.setOpaque(false);
        String[] levels = {"EASY", "NORMAL", "HARD"};

        for (String level : levels) {
            ButtonGradient btn = new ButtonGradient();
            btn.setText(level);
            btn.setFont(customFont.deriveFont(Font.PLAIN, 9f));
            btn.setPreferredSize(new Dimension(120, 45));
            diffPanel.add(btn); //---
        }
        diffPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(diffPanel);
        content.add(Box.createVerticalStrut(34));

        // ── MUSIC VOLUME ──────────────────────────────────────────────────
        content.add(createLabel("— MUSIC VOLUME —", 10f, new Color(0, 200, 255)));
        content.add(Box.createVerticalStrut(14));
        content.add(createSlider(80));
        content.add(Box.createVerticalStrut(34));

        // ── SFX VOLUME ────────────────────────────────────────────────────
        content.add(createLabel("— SFX VOLUME —", 10f, new Color(0, 200, 255)));
        content.add(Box.createVerticalStrut(14));
        content.add(createSlider(100));
        content.add(Box.createVerticalStrut(44));

        // ── Bouton BACK ───────────────────────────────────────────────────
        ButtonGradient back = new ButtonGradient();
        back.setText("< BACK");
        back.setFont(customFont.deriveFont(Font.PLAIN, 10f));
        back.setMaximumSize(new Dimension(200, 50));
        back.setAlignmentX(Component.CENTER_ALIGNMENT);
        back.addActionListener(e -> cardLayout.show(menu, "mainMenu"));
        content.add(back);

        settingsPanel.add(content, BorderLayout.CENTER);
        return settingsPanel;
    }

    // Helper slider
    private JSlider createSlider(int defaultValue) {
        JSlider slider = new JSlider(0, 100, defaultValue);
        slider.setOpaque(false);
        slider.setForeground(new Color(0, 200, 255));
        slider.setMaximumSize(new Dimension(320, 40));
        slider.setAlignmentX(Component.CENTER_ALIGNMENT);
        slider.setMajorTickSpacing(20);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.addChangeListener(e -> setVolumeSlider(slider.getValue()));
        return slider;
    }

    public JPanel creditsPanel() {
        creditsPanel = new StarBackgroundPanel();
        creditsPanel.setLayout(new BorderLayout());

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(40, 60, 30, 60));

        // Title
        JLabel title = new JLabel("CREDITS");
        title.setFont(customFont.deriveFont(Font.BOLD, 18f));
        title.setForeground(new Color(255, 184, 0));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(title);
        content.add(Box.createVerticalStrut(40));

        // ── Project ────────────────────────────────────────────────────────
        content.add(createLabel("— PROJECT —", 10f, new Color(0, 200, 255)));
        content.add(Box.createVerticalStrut(10));
        content.add(createLabel("Aegis Defender  —  2D Arcade Game", 10f, Color.WHITE));
        content.add(Box.createVerticalStrut(6));
        content.add(createLabel("Tesina di Programmazione di Interfacce Grafiche", 10f, new Color(168, 180, 208)));
        content.add(Box.createVerticalStrut(2));
        content.add(createLabel("& Dispositivi Mobili", 10f, new Color(168, 180, 208)));
        content.add(Box.createVerticalStrut(30));

        // ── Developper ───────────────────────────────────────────────────
        content.add(createLabel("— DEVELOPED BY —", 10f, new Color(0, 200, 255)));
        content.add(Box.createVerticalStrut(10));
        content.add(createLabel("Nsayi Juscarni Geoffroy", 10f, new Color(255, 184, 0)));
        content.add(Box.createVerticalStrut(6));
        content.add(createLabel("& ", 10f, new Color(255, 184, 0)));
        content.add(Box.createVerticalStrut(6));
        content.add(createLabel("Eudes Mavah", 10f, new Color(255, 184, 0)));
        content.add(Box.createVerticalStrut(30));

        // ── Technologies ──────────────────────────────────────────────────
        content.add(createLabel("— BUILT WITH —", 10f, new Color(0, 200, 255)));
        content.add(Box.createVerticalStrut(10));
        content.add(createLabel("Java  &  Swing", 10f, Color.WHITE));
        content.add(Box.createVerticalStrut(6));
        content.add(createLabel("Font  :  Press Start 2P  —  Google Fonts", 10f, new Color(168, 180, 208)));
        content.add(Box.createVerticalStrut(6));
        content.add(createLabel("Pattern  :  MVC  (Model - View - Controller)", 10f, new Color(168, 180, 208)));
        content.add(Box.createVerticalStrut(40));

        // ── Last word ─────────────────────────────────────────────────────
        content.add(createLabel("— SPECIAL THANKS —", 10f, new Color(0, 200, 255)));
        content.add(Box.createVerticalStrut(10));
        content.add(createLabel("Space Invaders & Galaga  —  for the inspiration", 10f, new Color(168, 180, 208)));
        content.add(Box.createVerticalStrut(40));

        // ── Bouton BACK ───────────────────────────────────────────────────
        ButtonGradient back = new ButtonGradient();
        back.setText("< BACK");
        back.setFont(customFont.deriveFont(Font.PLAIN, 10f));
        back.setMaximumSize(new Dimension(200, 50));
        back.setAlignmentX(Component.CENTER_ALIGNMENT);
        back.addActionListener(e -> cardLayout.show(menu, "mainMenu"));
        content.add(back);

        JScrollPane scroll = new JScrollPane(content);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        SwingUtilities.invokeLater(() -> centerScrollPane(scroll)); //

        creditsPanel.add(scroll, BorderLayout.CENTER);
        return creditsPanel;
    }//end of CrediPanel

    public void loadIconsImages() {
        String[] iconNames = {"play.png", "howToPlay.png", "settings.png", "credits.png", "exitGame.png"};
        for (int i = 0; i < icons.length; i++) {
            //
            try {
                icons[i] = new ImageIcon(
                        new ImageIcon(getClass().getResource("/Icons/" + iconNames[i]))
                                .getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
            } catch (Exception e) {
                System.out.println("Error... : " + iconNames[i]);
            }
        }
    }

    public void setOnPlayCallBack(Runnable callBack) {
        this.onPlayCallback = callBack;
    }

    public GamePanel getGamePanel() {
        return this.gamePanel;
    }

    public JLabel createGameTitle() {
        JLabel gameTitle = new JLabel("AEGIS DEFENDER");

        try {
            // load the font
            InputStream is = getClass().getResourceAsStream("/Fonts/PressStart2P-Regular.ttf");

            Font customFont = Font.createFont(Font.TRUETYPE_FONT, is);
            Font arcadeFont = customFont.deriveFont(Font.BOLD, 38f);
            gameTitle.setFont(arcadeFont);

        } catch (Exception e) {
            System.out.println("the font file doesn't exist");
            gameTitle.setFont(new Font("Impact", Font.BOLD, 80));
        }
        gameTitle.setForeground(new Color(255, 184, 0));
        gameTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        return gameTitle;
    }

    private JLabel createTagline() {
        JLabel tagline = new JLabel("PROTECT  ·  PREVAIL  ·  SURVIVE");

        try {
            // load the font
            InputStream is = getClass().getResourceAsStream("/Fonts/PressStart2P-Regular.ttf");

            customFont = Font.createFont(Font.TRUETYPE_FONT, is);
            Font arcadeFont = customFont.deriveFont(Font.PLAIN, 12f);
            tagline.setFont(arcadeFont);
        } catch (Exception e) {
            //if the file doesn't exit we use arial font
            System.out.println("the font file doesn't exist");
            tagline.setFont(new Font("Arial", Font.BOLD, 20));
        }

        tagline.setForeground(new Color(0, 200, 255));
        tagline.setAlignmentX(Component.CENTER_ALIGNMENT);

        return tagline;
    }


    public void showGameOverPanel(){
        cardLayout.show(menu,"gameOverPanel");
    }
    public void showMainMenuPanel(){
        cardLayout.show(menu,"mainMenu");
    }

    public void restartGame(){
        cardLayout.show(menu, "play");
        if(this.onPlayCallback != null){
            this.onPlayCallback.run();
        }
    }
    // we need it to access a method to set the score ...
    public GameOverPanel getGameOverPanel(){
        return this.gameOverPanel;
    }
    public UsernamePanel getUsernamePanel() {
        return this.usernamePanel;
    }

    public void setVolumeSlider(int volume) {
        this.volumeSlider = volume;
    }

    public void setAllScore(List<String[]> playerData){
        this.playerData = playerData;
        menu.add(highScorePanel(this.playerData), "highScore"); //

        /*just for debug
        if(!this.playerData.isEmpty()){
            Arrays.stream(playerData.getFirst()).forEach(System.out::println);
        }*/
    }


    // this methode center the jscroolpane
    private void centerScrollPane(JScrollPane scrollPane) {
        JScrollBar horizontal = scrollPane.getHorizontalScrollBar();
        JScrollBar vertical = scrollPane.getVerticalScrollBar();

        int centerX =
                (horizontal.getMaximum() - horizontal.getVisibleAmount()) / 2;

        int centerY =
                (vertical.getMaximum() - vertical.getVisibleAmount()) / 2;

        horizontal.setValue(centerX);
        vertical.setValue(centerY);
    }

}