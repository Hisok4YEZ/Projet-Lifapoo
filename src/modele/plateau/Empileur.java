package modele.plateau;

import modele.item.ItemShape;

public class Empileur extends Machine {
    @Override
    public void work() {
        if(current.size()>=2){
            ((ItemShape)current.get(0)).stack(((ItemShape) current.get(1)));
            current.remove(current.get(1));
        }
    }
    @Override
    public void send() {
        super.send();
    }
}
