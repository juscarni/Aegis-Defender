package org.aegisdefender.Model.Entities.Enemies;

import org.aegisdefender.Config.UIConfig;
import org.aegisdefender.Model.Entities.Player;

import java.awt.*;
import java.util.Random;

public class Kamikaze extends Enemy{

    private int attackPower = 30;
    private static final Random rand = new Random();

    // === Variables pour les patterns ===
    private int patternType;        // 0 à 4
    private double angle = 0.0;
    private double amplitude = 0.0;
    private int startX = 0;
    private double curveStrength = 1.0;
    private int homingStrength = 3; // force du suivi du joueur (plus c'est bas, plus c'est précis)


    public Kamikaze(){
        this.x = 100;
        this.y = -10;
        this.speed = 6;
        this.health = 100;
        this.width = UIConfig.TILES*3;
        this.height = UIConfig.TILES*3;

        // Par défaut on donne un pattern aléatoire (tu pourras le changer depuis le WaveManager)
        this.patternType = rand.nextInt(5);
        this.amplitude = 60 + rand.nextInt(60); // entre 60 et 120 pixels d'oscillation
    }
    // Méthode simple pour choisir le pattern
    public void setPattern(int pattern) {
        this.patternType = Math.clamp(pattern, 0, 4);
        this.startX = this.x;
        this.angle = 0.0;
        this.homingStrength = (pattern == 0 || pattern == 3) ? 5 : 3; // plus agressif sur certains patterns
    }

    @Override
    public void attack(Player player) {
        player.takeDamaged(attackPower);
    }

    @Override
    public void move(Player player) {
        // Descente de base toujours présente (effet kamikaze)
        this.y += speed;

        switch (patternType) {
            case 0: // 1. Plongeon Direct + Homing doux (classique)
                homingTowardPlayer(player, homingStrength);
                break;

            case 1: // 2. Mouvement en Vague (Sinus) - très beau visuellement
                angle += 0.085;
                this.x = startX + (int) (amplitude * Math.sin(angle));
                homingTowardPlayer(player, 2); // léger ajustement vers le joueur
                break;

            case 2: // 3. Plongeon en Arc / Courbe (inspiré Galaga/Falcon Squad)
                angle += 0.055;
                this.x = startX + (int) (amplitude * Math.sin(angle * 1.8));
                // Accélération progressive quand il descend (plus dangereux)
                if (this.y > 180) {
                    this.speed = (int)Math.min(11, this.speed + 0.12);//
                }
                homingTowardPlayer(player, 2);
                break;

            case 3: // 4. Zigzag Agressif (changements brusques)
                angle += 0.18;
                this.x += (int) (Math.sin(angle * 5.5));
                // Quand il est proche du joueur en X → il fonce droit sur lui
                if (player != null && Math.abs(this.x - player.getX()) < 45) {
                    homingTowardPlayer(player, 2);
                }
                break;

            case 4: // 5. Attaque en Spirale légère + Homing fort (spectaculaire)
                angle += 0.08;
                this.x = startX + (int) (amplitude * 0.20 * Math.cos(angle * 2.5));
                // Spirale qui se resserre progressivement
                amplitude = Math.max(30, amplitude - 0.25);
                homingTowardPlayer(player, 3);
                break;
        }

        // Sécurité : on garde l'ennemi dans les limites de l'écran en X
        if (this.x < 20) this.x = 20;
        if (this.x > UIConfig.WINDOW_WIDTH - this.width - 20) {
            this.x = UIConfig.WINDOW_WIDTH - this.width - 20;
        }
    }

    private void homingTowardPlayer(Player player, int maxAdjust) {
        if (player == null) return;
        int targetX = player.getX();

        if (this.x < targetX) {
            this.x += Math.min(maxAdjust, targetX - this.x);
        } else if (this.x > targetX) {
            this.x -= Math.min(maxAdjust, this.x - targetX);
        }
    }

    @Override
    public void enemyBehavior(Player player) {

    }

    @Override
    public Rectangle getHitBox() {
        return new Rectangle(
                this.x + 2 ,
                this.y + 2,
                this.width - 4,
                this.height - 4);
    }

    @Override
    public String getType(){
        return EnemyFactory.EnemyType.KAMIKAZE.name();
    }
}
