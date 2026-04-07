/*
 * Decompiled with CFR 0.152.
 */
package modele.plateau;

import java.util.LinkedList;
import modele.item.Item;
import modele.item.ItemShape;
import modele.plateau.Case;
import modele.plateau.Direction;

public abstract class Machine
        implements Runnable {
    LinkedList<Item> current;
    Case c;
    Direction d = Direction.North;

    public Machine() {
        this.current = new LinkedList();
    }

    public Machine(Item _item) {
        this();
        this.current.add(_item);
    }

    public void setCase(Case _c) {
        this.c = _c;
    }

    public Item getCurrent() {
        if (this.current.size() > 0) {
            return this.current.get(0);
        }
        return null;
    }

    public void send() {
        Machine m;
        Case up = this.c.plateau.getCase(this.c, Direction.North);
        if (up != null && (m = up.getMachine()) != null && !this.current.isEmpty()) {
            Item item = this.current.getFirst();
            m.current.add(item);
            this.current.remove(item);
        }
    }

    public void work() {
        if (this.current.size() > 0) {
            ((ItemShape)this.current.get(0)).rotate();
        }
    }

    @Override
    public void run() {
        this.work();
        this.send();
    }
}
