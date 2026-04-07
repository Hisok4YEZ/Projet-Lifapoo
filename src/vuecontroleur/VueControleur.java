/*
 * Decompiled with CFR 0.152.
 */
package vuecontroleur;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import modele.item.Item;
import modele.item.ItemColor;
import modele.item.ItemShape;
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
    private JComponent grilleIP;
    private boolean mousePressed = false;
    private ImagePanel[][] tabIP;

    public VueControleur(Jeu _jeu) {
        this.jeu = _jeu;
        this.plateau = this.jeu.getPlateau();
        this.sizeX = 16;
        this.sizeY = 16;
        this.chargerLesIcones();
        this.placerLesComposantsGraphiques();
        this.plateau.addObserver(this);
        this.mettreAJourAffichage();
    }

    private void chargerLesIcones() {
        this.icoRouge = new ImageIcon("./data/sprites/colors/blue.png").getImage();
        this.icoTapisDroite = new ImageIcon("./data/sprites/buildings/belt_top.png").getImage();
        this.icoPoubelle = new ImageIcon("./data/sprites/buildings/trash.png").getImage();
        this.icoMine = new ImageIcon("./data/sprites/buildings/miner.png").getImage();
        this.icoZoneLivraison = new ImageIcon("./data/sprites/buildings/goal_acceptor.png").getImage();
    }

    private void placerLesComposantsGraphiques() {
        this.setTitle("ShapeCraft");
        this.setResizable(true);
        this.setSize(this.sizeX * 82, this.sizeX * 82);
        this.setDefaultCloseOperation(3);
        this.grilleIP = new JPanel(new GridLayout(this.sizeY, this.sizeX));
        JPanel panneauOutils = new JPanel();
        JButton boutonMine = new JButton("Mine");
        boutonMine.addActionListener(e -> this.jeu.setMachineActuelle(new Mine()));
        JButton boutonTapis = new JButton("Tapis");
        boutonTapis.addActionListener(e -> this.jeu.setMachineActuelle(new Tapis()));
        JButton boutonPoubelle = new JButton("Poubelle");
        boutonPoubelle.addActionListener(e -> this.jeu.setMachineActuelle(new Poubelle()));
        panneauOutils.add(boutonMine);
        panneauOutils.add(boutonTapis);
        panneauOutils.add(boutonPoubelle);
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
                        VueControleur.this.mousePressed = false;
                        VueControleur.this.jeu.press(xx, yy);
                        System.out.println(xx + "-" + yy);
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        if (VueControleur.this.mousePressed) {
                            VueControleur.this.jeu.slide(xx, yy);
                        }
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        VueControleur.this.mousePressed = true;
                        VueControleur.this.jeu.press(xx, yy);
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
    }

    private void mettreAJourAffichage() {
        for (int x = 0; x < this.sizeX; ++x) {
            for (int y = 0; y < this.sizeY; ++y) {
                this.tabIP[x][y].setBackground((Image)null);
                this.tabIP[x][y].setFront(null);
                Case c = this.plateau.getCases()[x][y];
                Machine m = c.getMachine();
                Item g = c.getGisement();
                if (m != null) {
                    if (m instanceof Tapis) {
                        this.tabIP[x][y].setBackground(this.icoTapisDroite);
                    } else if (m instanceof Poubelle) {
                        this.tabIP[x][y].setBackground(this.icoPoubelle);
                    } else if (m instanceof Mine) {
                        this.tabIP[x][y].setBackground(this.icoMine);
                    } else if (m instanceof ZoneLivraison) {
                        this.tabIP[x][y].setBackground(this.icoZoneLivraison);
                    }
                    Item current = m.getCurrent();
                    if (current instanceof ItemShape) {
                        this.tabIP[x][y].setShape((ItemShape)current);
                    }
                    if (current instanceof ItemColor) {
                        // empty if block
                    }
                }
                if (g == null) continue;
                this.tabIP[x][y].setShape((ItemShape)g);
            }
        }
        this.grilleIP.repaint();
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
