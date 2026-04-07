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
    Direction inputDirection = Direction.South;

    public Machine() {
        this.current = new LinkedList();
    }

    public Machine(Item _item) {
        this();
        this.current.add(_item);
    }

    public Direction getDirection() {
        return this.d;
    }

    public void setDirection(Direction direction) {
        this.d = direction;
    }

    public Direction getInputDirection() {
        return this.inputDirection;
    }

    public void setInputDirection(Direction direction) {
        this.inputDirection = direction;
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
        if (this.current.isEmpty()) return;
        modele.item.Item item = this.current.getFirst();

        if (this.c != null && item.lastMovementTick == this.c.plateau.tickCount) {
            return; // Item newly arrived this tick, wait for next tick
        }

        // Auto-insertion latérale : Si ce n'est pas une machine spécialisée, le Tapis regarde à sa gauche/droite
        if (!(this instanceof AtelierPeinture) && (!(this instanceof Empileur)) && (!(this instanceof Decoupeur))) {
            Direction[] checkDirs = {Direction.East, Direction.West};
            for (Direction dir : checkDirs) {
                Case sideCase = this.c.plateau.getCase(this.c, dir);
                if (sideCase != null && sideCase.getMachine() != null) {
                    Machine neighbor = sideCase.getMachine();
                    // Si on passe à côté d'une machine d'assemblage, on injecte dedans SI elle en a besoin
                    if (neighbor instanceof AtelierPeinture || neighbor instanceof Empileur) {
                        if (neighbor.acceptsItem(item)) {
                            item.lastMovementTick = this.c.plateau.tickCount;
                            neighbor.current.add(item);
                            this.current.remove(item);
                            return; // Injecté avec succès
                        }
                    }
                }
            }
        }

        // Comportement standard : Tout droit via this.d (Nord par défaut)
        Machine m;
        Case nextCase = this.c.plateau.getCase(this.c, this.d);
        if (nextCase != null && (m = nextCase.getMachine()) != null) {
            if (m.acceptsItem(item)) {
                item.lastMovementTick = this.c.plateau.tickCount;
                m.current.add(item);
                this.current.remove(item);
            }
        }
    }

    public boolean acceptsItem(Item item) {
        // Par défaut, une machine accepte sans limite
        return true;
    }

    public void work() {
        // Le comportement par défaut ne fait rien.
    }

    @Override
    public void run() {
        this.work();
        this.send();
    }
}
