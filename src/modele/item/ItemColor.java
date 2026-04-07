package modele.item;

public class ItemColor extends Item {
    Color color;
    public Color getColor(){
        return color;
    }
    public void transform(Color add) { // faire varier la couleur suivant la couleur ajoutée
        color=add;
    }

}
