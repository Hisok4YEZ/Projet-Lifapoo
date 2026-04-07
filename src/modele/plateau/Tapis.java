package modele.plateau;

import modele.item.ItemShape;

public class Tapis extends Machine{

    public String getShapeType() {
        int inX = -this.inputDirection.dx;
        int inY = -this.inputDirection.dy;
        
        int outX = this.d.dx;
        int outY = this.d.dy;
        
        int crossProduits = inX * outY - inY * outX;
        
        if (crossProduits > 0) {
            return "RIGHT";
        } else if (crossProduits < 0) {
            return "LEFT";
        } else {
            return "FORWARD";
        }
    }
}
