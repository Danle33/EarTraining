package components;

import utils.Colors;
import javax.swing.*;
import java.awt.Dimension;
import java.awt.Toolkit;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("Ear Trainer");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        // Calculate relative 9:16 frame size
        applyRelative9by16Size(0.8);
        setResizable(false);
        setLocationRelativeTo(null);

        // Load Home Panel initially
        showScreen(new HomePanel(this));
    }

    /** Replaces current panel with new panel view */
    public void showScreen(JPanel newPanel) {
        setContentPane(newPanel);
        revalidate();
        repaint();
    }

    private void applyRelative9by16Size(double screenHeightPercentage) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        int targetHeight = (int) Math.round(screenHeight * screenHeightPercentage);
        int targetWidth = (int) Math.round(targetHeight * (9.0 / 16.0));

        if (targetWidth > screenWidth) {
            targetWidth = (int) (screenWidth * 0.9);
            targetHeight = (int) (targetWidth * (16.0 / 9.0));
        }

        setSize(targetWidth, targetHeight);
    }
}