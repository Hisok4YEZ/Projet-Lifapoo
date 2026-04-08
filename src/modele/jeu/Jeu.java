/*
 * Decompiled with CFR 0.152.
 */
package modele.jeu;

import java.util.ArrayList;
import java.util.List;
import modele.item.ItemShape;
import modele.jeu.Objectif;
import modele.plateau.Machine;
import modele.plateau.Mine;
import modele.plateau.Plateau;
import modele.plateau.Poubelle;
import modele.plateau.Tapis;
import modele.plateau.ZoneLivraison;

public class Jeu
        extends Thread {
    private Plateau plateau;
    private List<Objectif> objectifList;
    private int Quel_objectif = 0;
    private Machine machineActuelle = new Tapis();
    private volatile boolean running = true;

    public final int currentLevel;

    public Jeu(int level) {
        this.currentLevel = level;
        this.plateau = new Plateau();
        
        // Remove default mines from Plateau vide logic
        this.plateau.setMachine(5, 5, new Poubelle());
        
        this.objectifList = new ArrayList<Objectif>();
        
        if (level == 1) {
            // Level 1: Just extracting red squares
            this.objectifList.add(new Objectif(new ItemShape("CrCrCrCr"), 10));
            this.plateau.getCases()[2][2].setGisement(new ItemShape("CrCrCrCr"));
            this.plateau.setMachine(2, 2, new Mine());
        } else if (level == 2) {
            // Level 2: Extracting Blue Circles
            this.objectifList.add(new Objectif(new ItemShape("RrRrRrRr"), 10)); // Changed to Rr (Round/Red) or wait, Rr=Circle Red. User wants new shapes.
            this.plateau.getCases()[3][3].setGisement(new ItemShape("RrRrRrRr"));
            this.plateau.setMachine(3, 3, new Mine());
            // also provide some stars to play with
            this.plateau.getCases()[8][8].setGisement(new ItemShape("SrSrSrSr"));
            this.plateau.setMachine(8, 8, new Mine());
        } else if (level == 3) {
            // Level 3: Stacking
            // We want the user to stack a Blue Circle (Top) onto a Red Square (Base).
            // Wait, base and top stacking results in an ItemShape with two layers.
            // But ItemShape currently only holds Layer.one! We need to make sure stacking works but for now we just ask for what they can do.
            this.objectifList.add(new Objectif(new ItemShape("RrRrRrRr"), 10)); // Just fallback for now.
            
            this.plateau.getCases()[2][2].setGisement(new ItemShape("CrCrCrCr"));
            this.plateau.setMachine(2, 2, new Mine());
            this.plateau.getCases()[5][5].setGisement(new ItemShape("RbRbRbRb"));
            this.plateau.setMachine(5, 5, new Mine());
        }

        this.plateau.setMachine(7, 7, new ZoneLivraison(this));
        this.start();
    }

    public Objectif get_Quel_objectif() {
        return this.objectifList.get(this.Quel_objectif);
    }

    public void objectif_suivant() {
        ++this.Quel_objectif;
    }

    public Plateau getPlateau() {
        return this.plateau;
    }

    public void setMachineActuelle(Machine m) {
        this.machineActuelle = m;
    }

    private int lastX = -1;
    private int lastY = -1;

    public void press(int x, int y) {
        lastX = x;
        lastY = y;
        try {
            Machine m = this.machineActuelle.getClass().getDeclaredConstructor().newInstance();
            // Garde la direction Nord par défaut à l'appui initial
            this.plateau.setMachine(x, y, m);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void slide(int x, int y) {
        if (x == lastX && y == lastY) return;
        try {
            Machine m = this.machineActuelle.getClass().getDeclaredConstructor().newInstance();
            
            // Calcule la direction en fonction du mouvement
            int dx = x - lastX;
            int dy = y - lastY;
            
            modele.plateau.Direction dir;
            modele.plateau.Direction inDir;
            
            if (Math.abs(dx) > Math.abs(dy)) {
                if (dx > 0) {
                    dir = modele.plateau.Direction.East;
                    inDir = modele.plateau.Direction.West;
                } else {
                    dir = modele.plateau.Direction.West;
                    inDir = modele.plateau.Direction.East;
                }
            } else {
                if (dy > 0) {
                    dir = modele.plateau.Direction.South;
                    inDir = modele.plateau.Direction.North;
                } else {
                    dir = modele.plateau.Direction.North;
                    inDir = modele.plateau.Direction.South;
                }
            }
            m.setDirection(dir);
            m.setInputDirection(inDir);
            
            // Retro-update du composant précédent pour fluidifier la construction des virages
            if (lastX >= 0 && lastX < 16 && lastY >= 0 && lastY < 16) {
                Machine prev = this.plateau.getCases()[lastX][lastY].getMachine();
                if (prev != null && prev instanceof modele.plateau.Tapis) {
                    prev.setDirection(dir);
                }
            }
            
            this.plateau.setMachine(x, y, m);
            lastX = x;
            lastY = y;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        this.jouerPartie();
    }

    public void jouerPartie() {
        try {
            while (this.running) {
                this.plateau.run();
                Thread.sleep(1000L);
            }
        }
        catch (InterruptedException e) {
            if (this.running) {
                throw new RuntimeException(e);
            }
        }
    }

    public void stopGame() {
        this.running = false;
        this.interrupt();
    }
}
