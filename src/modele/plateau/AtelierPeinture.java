package modele.plateau;

import modele.item.ItemColor;
import modele.item.ItemShape;

public class AtelierPeinture extends Machine{
    @Override
    public void work() {
        // Le travail et l'envoi se font dans send() pour forcer l'attente des deux pièces.
    }

    @Override
    public void send() {
        if (current.size() >= 2) {
            ItemShape shape = null;
            ItemColor color = null;

            for (modele.item.Item item : current) {
                if (item instanceof ItemShape && shape == null) {
                    shape = (ItemShape) item;
                } else if (item instanceof ItemColor && color == null) {
                    color = (ItemColor) item;
                }
            }

            if (shape != null && color != null) {
                // Wait for both items to be ready for processing in the current tick
                if (shape.lastMovementTick == this.c.plateau.tickCount || color.lastMovementTick == this.c.plateau.tickCount) {
                    return; 
                }

                // Effectue la peinture
                shape.Color(color.getColor());
                current.remove(color); // Consomme la couleur
                
                // Envoie la forme peinte
                Machine m;
                Case nextCase = this.c.plateau.getCase(this.c, this.d);
                if (nextCase != null && (m = nextCase.getMachine()) != null) {
                    shape.lastMovementTick = this.c.plateau.tickCount;
                    m.current.add(shape);
                    current.remove(shape);
                }
            }
        }
    }
    @Override
    public boolean acceptsItem(modele.item.Item item) {
        if (current.size() >= 2) return false;
        if (item instanceof ItemShape) {
            for (modele.item.Item i : current) {
                if (i instanceof ItemShape) return false;
            }
        } else if (item instanceof ItemColor) {
            for (modele.item.Item i : current) {
                if (i instanceof ItemColor) return false;
            }
        }
        return true;
    }
}
