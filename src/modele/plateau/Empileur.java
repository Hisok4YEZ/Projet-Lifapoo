package modele.plateau;

import modele.item.ItemShape;

public class Empileur extends Machine {
    @Override
    public void work() {
        // Le travail se fait dans send()
    }

    @Override
    public void send() {
        if (current.size() >= 2) {
            ItemShape base = null;
            ItemShape top = null;

            for (modele.item.Item item : current) {
                if (item instanceof ItemShape) {
                    if (base == null) {
                        base = (ItemShape) item;
                    } else if (top == null) {
                        top = (ItemShape) item;
                    }
                }
            }

            if (base != null && top != null) {
                base.stack(top);
                current.remove(top);

                // Envoie la forme empilée
                Machine m;
                Case nextCase = this.c.plateau.getCase(this.c, this.d);
                if (nextCase != null && (m = nextCase.getMachine()) != null) {
                    m.current.add(base);
                    current.remove(base);
                }
            }
        }
    }
    @Override
    public boolean acceptsItem(modele.item.Item item) {
        if (!(item instanceof ItemShape)) return false;
        int count = 0;
        for (modele.item.Item i : current) {
            if (i instanceof ItemShape) count++;
        }
        return count < 2;
    }
}
