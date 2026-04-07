package modele.plateau;

import modele.item.Item;
import modele.item.ItemShape;

public class Decoupeur extends Machine {
    @Override
    public void work() {
        // La coupe se fait dans send() pour éviter de bloquer la queue si un tapis apporte un item pendant que la taille n'est pas "exactement" 1.
    }

    @Override
    public void send() {
        if (current.size() > 0) {
            Item item = current.get(0);
            Machine m1 = null;
            Machine m2 = null;
            
            Case up = c.plateau.getCase(c, Direction.North);
            Case east = c.plateau.getCase(c, Direction.East);
            
            if (up != null) m1 = up.getMachine();
            if (east != null) m2 = east.getMachine();

            // S'il y a un débouchoir d'au moins un côté, on travaille la forme.
            if (m1 != null || m2 != null) {
                if (item instanceof ItemShape) {
                    ItemShape droite = ((ItemShape) item).Cut();
                    
                    if (m1 != null) m1.current.add(item);
                    if (m2 != null) m2.current.add(droite);
                    
                    current.remove(0);
                    System.out.println("Découpé et envoyé !");
                } else {
                    // Couleur : orientée au nord par défaut.
                    if (m1 != null) m1.current.add(item);
                    current.remove(0);
                }
            }
        }
    }
}

