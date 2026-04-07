package vuecontroleur;

import modele.jeu.Jeu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JFrame {

    public MainMenu() {
        setTitle("ShapeCraft - Menu Principal");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Configuration du style sombre et pro
        Color bgColor = new Color(30, 30, 30);
        Color fgColor = new Color(240, 240, 240);
        Color btnColor = new Color(60, 60, 60);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(bgColor);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(100, 50, 100, 50));

        JLabel titleLabel = new JLabel("SHAPECRAFT");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setForeground(fgColor);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("L'Industrie de Formes");
        subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 18));
        subtitleLabel.setForeground(new Color(150, 150, 150));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(subtitleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 80)));

        mainPanel.add(createStyledButton("Niveau 1", btnColor, fgColor, 1));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(createStyledButton("Niveau 2", btnColor, fgColor, 2));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(createStyledButton("Niveau 3", btnColor, fgColor, 3));

        add(mainPanel);
    }

    private JButton createStyledButton(String text, Color bg, Color fg, int levelId) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 20));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 2));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(250, 50));
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(90, 90, 90));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(bg);
            }
        });

        btn.addActionListener(e -> launchLevel(levelId));
        return btn;
    }

    private void launchLevel(int levelId) {
        dispose(); // Fermer le menu
        
        SwingUtilities.invokeLater(() -> {
            Jeu jeu = new Jeu(levelId);
            VueControleur vc = new VueControleur(jeu);
            jeu.getPlateau().addObserver(vc);
            vc.setVisible(true);
        });
    }
}
