/*
 * Decompiled with CFR 0.152.
 */
package modele.plateau;

import modele.item.Item;
import modele.plateau.Machine;
import modele.plateau.Plateau;

public class Case {
    protected Plateau plateau;
    protected Machine machine;
    protected Item gisement;

    public void setMachine(Machine m) {
        this.machine = m;
        m.setCase(this);
    }

    public Machine getMachine() {
        return this.machine;
    }

    public Item getGisement() {
        return this.gisement;
    }

    public void setGisement(Item g) {
        this.gisement = g;
    }

    public Case(Plateau _plateau) {
        this.plateau = _plateau;
    }
}
