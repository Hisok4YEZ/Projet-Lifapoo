/*
 * Decompiled with CFR 0.152.
 */
package vuecontroleur;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import modele.item.Item;
import modele.item.ItemColor;
import modele.item.ItemShape;
import modele.jeu.Objectif;
import modele.jeu.Jeu;
import modele.plateau.Case;
import modele.plateau.Machine;
import modele.plateau.Mine;
import modele.plateau.Plateau;
import modele.plateau.Poubelle;
import modele.plateau.Tapis;
import modele.plateau.ZoneLivraison;
import vuecontroleur.ImagePanel;

public class VueControleur
        extends JFrame
        implements Observer {
    private Plateau plateau;
    private Jeu jeu;
    private final int sizeX;
    private final int sizeY;
    private static final int pxCase = 82;
    private Image icoRouge;
    private Image icoTapisDroite;
    private Image icoPoubelle;
    private Image icoMine;
    private Image icoZoneLivraison;
    private Image icoEmpileur;
    private Image icoAtelierPeinture;
    private Image icoRotateur;
    private Image icoDecoupeur;
    private JComponent grilleIP;
    private boolean mousePressed = false;
    private int lastHoveredX = -1;
    private int lastHoveredY = -1;
    private ImagePanel[][] tabIP;
    private Timer animationTimer;
    private ImagePanel objectifPreview;
    private JLabel objectifTexte;
    private JLabel objectifProgression;

    public VueControleur(Jeu _jeu) {
        this.jeu = _jeu;
        this.plateau = this.jeu.getPlateau();
        this.sizeX = 16;
        this.sizeY = 16;
        this.chargerLesIcones();
        this.placerLesComposantsGraphiques();
        this.plateau.addObserver(this);
        this.mettreAJourAffichage();
        
        SwingUtilities.invokeLater(() -> {
            modele.jeu.Objectif obj = this.jeu.get_Quel_objectif();
            String msg = "Niveau " + this.jeu.currentLevel + " - Objectif : Produire et livrer " + 
                         obj.getNb_formeAttendue() + " fois la forme requise.\n" +
                         "Bonne chance !";
            javax.swing.JOptionPane.showMessageDialog(this, msg, "Objectif du niveau", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        });
    }

    private Image[] animForward = new Image[14];
    private Image[] animLeft = new Image[14];
    private Image[] animRight = new Image[14];
    public static int animationFrame = 0;

    private void chargerLesIcones() {
        this.icoRouge = new ImageIcon("./data/sprites/colors/blue.png").getImage();
        this.icoTapisDroite = new ImageIcon("./data/sprites/buildings/belt_top.png").getImage();
        this.icoPoubelle = new ImageIcon("./data/sprites/buildings/trash.png").getImage();
        this.icoMine = new ImageIcon("./data/sprites/buildings/miner.png").getImage();
        this.icoZoneLivraison = new ImageIcon("./data/sprites/buildings/goal_acceptor.png").getImage();
        this.icoEmpileur = new ImageIcon("./data/sprites/buildings/stacker.png").getImage();
        this.icoAtelierPeinture = new ImageIcon("./data/sprites/buildings/painter.png").getImage();
        this.icoRotateur = new ImageIcon("./data/sprites/buildings/rotater.png").getImage();
        this.icoDecoupeur = new ImageIcon("./data/sprites/buildings/cutter.png").getImage();
        
        for (int i = 0; i < 14; i++) {
            this.animForward[i] = new ImageIcon("./data/sprites/belt/built/forward_" + i + ".png").getImage();
            this.animLeft[i] = new ImageIcon("./data/sprites/belt/built/left_" + i + ".png").getImage();
            this.animRight[i] = new ImageIcon("./data/sprites/belt/built/right_" + i + ".png").getImage();
        }
        
        this.animationTimer = new Timer(70, e -> {
            animationFrame = (animationFrame + 1) % 14;
            if (grilleIP != null) {
                grilleIP.repaint();
            }
        });
        this.animationTimer.start();
    }

    private void placerLesComposantsGraphiques() {
        this.setTitle("ShapeCraft");
        this.setResizable(true);
        this.setSize(this.sizeX * 82, this.sizeX * 82);
        this.setDefaultCloseOperation(3);
        this.setLayout(new BorderLayout());
        this.add(this.creerPanneauObjectif(), "North");
        this.grilleIP = new JPanel(new GridLayout(this.sizeY, this.sizeX));
        JPanel panneauOutils = new JPanel();
        JButton boutonMine = new JButton("Mine");
        boutonMine.addActionListener(e -> this.jeu.setMachineActuelle(new Mine()));
        JButton boutonTapis = new JButton("Tapis");
        boutonTapis.addActionListener(e -> this.jeu.setMachineActuelle(new Tapis()));
        JButton boutonPoubelle = new JButton("Poubelle");
        boutonPoubelle.addActionListener(e -> this.jeu.setMachineActuelle(new Poubelle()));
        JButton boutonEmpileur = new JButton("Empileur");
        boutonEmpileur.addActionListener(e -> this.jeu.setMachineActuelle(new modele.plateau.Empileur()));
        JButton boutonPeinture = new JButton("Peinture");
        boutonPeinture.addActionListener(e -> this.jeu.setMachineActuelle(new modele.plateau.AtelierPeinture()));
        JButton boutonRotateur = new JButton("Rotateur");
        boutonRotateur.addActionListener(e -> this.jeu.setMachineActuelle(new modele.plateau.Rotateur()));
        JButton boutonDecoupeur = new JButton("Découpeur");
        boutonDecoupeur.addActionListener(e -> this.jeu.setMachineActuelle(new modele.plateau.Decoupeur()));
        panneauOutils.add(boutonMine);
        panneauOutils.add(boutonTapis);
        panneauOutils.add(boutonPoubelle);
        panneauOutils.add(boutonEmpileur);
        panneauOutils.add(boutonPeinture);
        panneauOutils.add(boutonRotateur);
        panneauOutils.add(boutonDecoupeur);
        this.add((Component)panneauOutils, "South");
        this.tabIP = new ImagePanel[this.sizeX][this.sizeY];
        for (int y = 0; y < this.sizeY; ++y) {
            int x = 0;
            while (x < this.sizeX) {
                ImagePanel iP;
                this.tabIP[x][y] = iP = new ImagePanel();
                final int xx = x++;
                final int yy = y;
                iP.addMouseListener(new MouseAdapter(){

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        VueControleur.this.lastHoveredX = xx;
                        VueControleur.this.lastHoveredY = yy;
                        if (SwingUtilities.isRightMouseButton(e)) {
                            VueControleur.this.jeu.removeMachine(xx, yy);
                        } else {
                            VueControleur.this.mousePressed = false;
                            VueControleur.this.jeu.press(xx, yy);
                        }
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        VueControleur.this.lastHoveredX = xx;
                        VueControleur.this.lastHoveredY = yy;
                        if (VueControleur.this.mousePressed) {
                            VueControleur.this.jeu.slide(xx, yy);
                        }
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        VueControleur.this.lastHoveredX = xx;
                        VueControleur.this.lastHoveredY = yy;
                        if (!SwingUtilities.isRightMouseButton(e)) {
                            VueControleur.this.mousePressed = true;
                            VueControleur.this.jeu.press(xx, yy);
                        }
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        VueControleur.this.mousePressed = false;
                    }
                });
                this.grilleIP.add(iP);
            }
        }
        this.add(this.grilleIP);

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_R) {
                    int x = VueControleur.this.lastHoveredX;
                    int y = VueControleur.this.lastHoveredY;
                    if (x >= 0 && y >= 0) {
                        VueControleur.this.jeu.rotateMachine(x, y);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private JPanel creerPanneauObjectif() {
        JPanel panneauObjectif = new JPanel(new BorderLayout(12, 0));
        panneauObjectif.setBackground(new Color(28, 32, 38));
        panneauObjectif.setBorder(new EmptyBorder(10, 12, 10, 12));

        this.objectifPreview = new ImagePanel();
        this.objectifPreview.setPreferredSize(new Dimension(56, 56));
        this.objectifPreview.setBackground((Image)null);

        JPanel texteObjectif = new JPanel(new GridLayout(2, 1));
        texteObjectif.setOpaque(false);

        this.objectifTexte = new JLabel();
        this.objectifTexte.setForeground(new Color(245, 245, 245));
        this.objectifTexte.setFont(new Font("Arial", Font.BOLD, 16));

        this.objectifProgression = new JLabel();
        this.objectifProgression.setForeground(new Color(140, 210, 255));
        this.objectifProgression.setFont(new Font("Arial", Font.PLAIN, 14));

        texteObjectif.add(this.objectifTexte);
        texteObjectif.add(this.objectifProgression);

        JPanel conteneurPreview = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        conteneurPreview.setOpaque(false);
        conteneurPreview.add(this.objectifPreview);

        JLabel labelControls = new JLabel("Clic droit : supprimer   |   R : tourner");
        labelControls.setForeground(new Color(160, 160, 160));
        labelControls.setFont(new Font("Arial", Font.PLAIN, 12));
        labelControls.setHorizontalAlignment(JLabel.RIGHT);

        panneauObjectif.add(conteneurPreview, "West");
        panneauObjectif.add(texteObjectif, "Center");
        panneauObjectif.add(labelControls, "East");
        return panneauObjectif;
    }

    private void mettreAJourAffichage() {
        for (int x = 0; x < this.sizeX; ++x) {
            for (int y = 0; y < this.sizeY; ++y) {
                this.tabIP[x][y].setBackground((Image)null);
                this.tabIP[x][y].setFront(null);
                this.tabIP[x][y].setShape(null);
                this.tabIP[x][y].setItemColor(null);
                this.tabIP[x][y].setDirection(null);
                
                Case c = this.plateau.getCases()[x][y];
                Machine m = c.getMachine();
                Item g = c.getGisement();
                if (m != null) {
                    this.tabIP[x][y].setDirection(m.getDirection());
                    if (m instanceof Tapis) {
                        modele.plateau.Direction inputDir = m.getInputDirection();
                        modele.plateau.Direction baseOrientation = modele.plateau.Direction.North;
                        if (inputDir == modele.plateau.Direction.South) baseOrientation = modele.plateau.Direction.North;
                        else if (inputDir == modele.plateau.Direction.North) baseOrientation = modele.plateau.Direction.South;
                        else if (inputDir == modele.plateau.Direction.East) baseOrientation = modele.plateau.Direction.West;
                        else if (inputDir == modele.plateau.Direction.West) baseOrientation = modele.plateau.Direction.East;
                        
                        this.tabIP[x][y].setDirection(baseOrientation);
                        
                        String shapeClass = ((Tapis)m).getShapeType();
                        if ("RIGHT".equals(shapeClass)) {
                            this.tabIP[x][y].setAnimBackground(animRight);
                        } else if ("LEFT".equals(shapeClass)) {
                            this.tabIP[x][y].setAnimBackground(animLeft);
                        } else {
                            this.tabIP[x][y].setAnimBackground(animForward);
                        }
                    } else if (m instanceof Poubelle) {
                        this.tabIP[x][y].setBackground(this.icoPoubelle);
                    } else if (m instanceof Mine) {
                        this.tabIP[x][y].setBackground(this.icoMine);
                    } else if (m instanceof ZoneLivraison) {
                        this.tabIP[x][y].setBackground(this.icoZoneLivraison);
                    } else if (m instanceof modele.plateau.Empileur) {
                        this.tabIP[x][y].setBackground(this.icoEmpileur);
                    } else if (m instanceof modele.plateau.AtelierPeinture) {
                        this.tabIP[x][y].setBackground(this.icoAtelierPeinture);
                    } else if (m instanceof modele.plateau.Rotateur) {
                        this.tabIP[x][y].setBackground(this.icoRotateur);
                    } else if (m instanceof modele.plateau.Decoupeur) {
                        this.tabIP[x][y].setBackground(this.icoDecoupeur);
                    }
                    Item current = m.getCurrent();
                    if (current instanceof ItemShape) {
                        this.tabIP[x][y].setShape((ItemShape)current);
                    } else if (current instanceof modele.item.ItemColor) {
                        this.tabIP[x][y].setItemColor((modele.item.ItemColor)current);
                    }
                }
                if (g != null) {
                    if (g instanceof ItemShape) {
                        this.tabIP[x][y].setShape((ItemShape)g);
                    } else if (g instanceof modele.item.ItemColor) {
                        this.tabIP[x][y].setItemColor((modele.item.ItemColor)g);
                    }
                }
            }
        }
        this.mettreAJourObjectif();
        this.grilleIP.repaint();
    }

    private void mettreAJourObjectif() {
        Objectif objectif = this.jeu.get_Quel_objectif();
        this.objectifPreview.setShape(objectif.getItemShape());
        this.objectifPreview.setItemColor(null);
        this.objectifPreview.setFront(null);
        this.objectifPreview.setDirection(null);

        int progression = 0;
        for (int x = 0; x < this.sizeX; ++x) {
            for (int y = 0; y < this.sizeY; ++y) {
                Machine machine = this.plateau.getCases()[x][y].getMachine();
                if (machine instanceof ZoneLivraison) {
                    progression = ((ZoneLivraison)machine).getCompteur();
                }
            }
        }

        this.objectifTexte.setText("Objectif du niveau " + this.jeu.currentLevel);
        this.objectifProgression.setText("Livrer " + progression + " / " + objectif.getNb_formeAttendue());
        this.objectifPreview.repaint();
    }

    @Override
    public void dispose() {
        if (this.animationTimer != null) {
            this.animationTimer.stop();
        }
        if (this.jeu != null) {
            this.jeu.stopGame();
        }
        super.dispose();
    }

    @Override
    public void update(Observable o, Object arg) {
        SwingUtilities.invokeLater(new Runnable(){

            @Override
            public void run() {
                VueControleur.this.mettreAJourAffichage();
            }
        });
    }
}
