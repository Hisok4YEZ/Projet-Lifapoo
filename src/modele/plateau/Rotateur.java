package modele.plateau;

import modele.item.ItemShape;

public class Rotateur extends Machine{
    @Override
    public void work() {
        super.work();
        if (current.size() > 0 && current.get(0) instanceof ItemShape) {
            ((ItemShape) current.get(0)).rotate();
        }
    }
}
