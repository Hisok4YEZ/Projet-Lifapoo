package modele.plateau;

import modele.item.ItemColor;
import modele.item.ItemShape;

public class AtelierPeinture extends Machine{
    @Override
    public void work() {
        if (current.size()>=2){
            if(current.get(0) instanceof ItemShape && current.get(1) instanceof ItemColor){
                ((ItemShape) current.get(0)).Color(((ItemColor) current.get(1)).getColor());
                current.remove(current.get(1));
            }
        }
    }
    @Override
    public void send() {
        super.send();
    }
}
