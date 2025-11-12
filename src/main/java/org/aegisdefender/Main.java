package org.aegisdefender;

import org.aegisdefender.Controller.AppController;

import javax.swing.SwingUtilities;

public class Main{
    public static void main(String[] args) {
        SwingUtilities.invokeLater(AppController::new);
    }
}