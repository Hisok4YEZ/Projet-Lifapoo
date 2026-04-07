/*
 * Decompiled with CFR 0.152.
 */
package modele.plateau;

import java.util.HashMap;
import java.util.Observable;
import java.util.Random;
import modele.item.ItemShape;
import modele.plateau.Case;
import modele.plateau.Direction;
import modele.plateau.Machine;
import modele.plateau.Point;

public class Plateau
        extends Observable
        implements Runnable {
    public static final int SIZE_X = 16;
    public static final int SIZE_Y = 16;
    private HashMap<Case, Point> map = new HashMap();
    private Case[][] grilleCases = new Case[16][16];

    public Plateau() {
        this.initPlateauVide();
    }

    public Case[][] getCases() {
        return this.grilleCases;
    }

    public Case getCase(Case source, Direction d) {
        Point p = this.map.get(source);
        return this.caseALaPosition(new Point(p.x + d.dx, p.y + d.dy));
    }

    private void initPlateauVide() {
        for (int x = 0; x < 16; ++x) {
            for (int y = 0; y < 16; ++y) {
                this.grilleCases[x][y] = new Case(this);
                this.map.put(this.grilleCases[x][y], new Point(x, y));
            }
        }
    }

    public void setMachine(int x, int y, Machine m) {
        this.grilleCases[x][y].setMachine(m);
        this.setChanged();
        this.notifyObservers();
    }

    private boolean contenuDansGrille(Point p) {
        return p.x >= 0 && p.x < 16 && p.y >= 0 && p.y < 16;
    }

    private Case caseALaPosition(Point p) {
        Case retour = null;
        if (this.contenuDansGrille(p)) {
            retour = this.grilleCases[p.x][p.y];
        }
        return retour;
    }

    public long tickCount = 0;

    @Override
    public void run() {
        this.tickCount++;
        for (int x = 0; x < 16; ++x) {
            for (int y = 0; y < 16; ++y) {
                Case c = this.grilleCases[x][y];
                if (c.getMachine() == null) continue;
                c.getMachine().run();
            }
        }
        this.setChanged();
        this.notifyObservers();
    }
}
