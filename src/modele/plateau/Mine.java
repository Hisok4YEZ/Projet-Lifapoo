/*
 * Decompiled with CFR 0.152.
 */
package modele.plateau;

import java.util.Random;
import modele.item.ItemShape;
import modele.plateau.Machine;

public class Mine
        extends Machine {
    @Override
    public void work() {
        if (this.c.gisement != null && new Random().nextInt(4) == 0 && this.current.isEmpty()) {
            if (this.c.gisement instanceof ItemShape) {
                this.current.add(((ItemShape)this.c.gisement).copy()); 
            } else if (this.c.gisement instanceof modele.item.ItemColor) {
                this.current.add(((modele.item.ItemColor)this.c.gisement).copy());
            }
        }
    }

    @Override
    public void send() {
        super.send();
    }
}
