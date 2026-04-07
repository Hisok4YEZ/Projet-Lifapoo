package modele.plateau;

import modele.item.Item;
import modele.item.ItemShape;

public class Decoupeur extends Machine {
    @Override
    public void work() {
        super.work();
        if (current.size() ==1){
            ItemShape droite = ((ItemShape) current.get(0)).Cut();
            current.add(droite);
            System.out.println("Entré dans le découpeur");
        }
    }

    @Override
    public void send() {
        Case up = c.plateau.getCase(c, Direction.North);
        Case east = c.plateau.getCase(c, Direction.East);
        if (up != null && east != null) {
            Machine m1 = up.getMachine();
            Machine m2 = east.getMachine();
            if (m1 != null && m2 !=null && current.size()==2) {
                Item gauche = current.get(0);
                Item droite = current.get(1);
                m1.current.add(gauche);
                System.out.println(gauche);
                m2.current.add(droite);
                System.out.println(droite);
                current.remove(gauche);
                current.remove(droite);
            }
        }
    }
}

