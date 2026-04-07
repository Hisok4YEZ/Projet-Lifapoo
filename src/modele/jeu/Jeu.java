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

    public Jeu() {
        this.plateau = new Plateau();
        this.plateau.setMachine(5, 10, new Mine());
        this.plateau.setMachine(5, 5, new Poubelle());
        this.objectifList = new ArrayList<Objectif>();
        Objectif Objectif1 = new Objectif(new ItemShape("CrCrCrCr"), 3);
        Objectif Objectif2 = new Objectif(new ItemShape("CbCb--Cb"), 2);
        Objectif Objectif3 = new Objectif(new ItemShape("CrCbCbCb"), 2);
        this.objectifList.add(Objectif1);
        this.objectifList.add(Objectif2);
        this.objectifList.add(Objectif3);
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

    public void press(int x, int y) {
        this.plateau.setMachine(x, y, this.machineActuelle);
    }

    public void slide(int x, int y) {
        this.plateau.setMachine(x, y, this.machineActuelle);
    }

    @Override
    public void run() {
        this.jouerPartie();
    }

    public void jouerPartie() {
        try {
            while (true) {
                this.plateau.run();
                Thread.sleep(1000L);
            }
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
