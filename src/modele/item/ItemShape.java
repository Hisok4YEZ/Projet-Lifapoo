/*
 * Decompiled with CFR 0.152.
 */
package modele.item;

import modele.item.Color;
import modele.item.Item;
import modele.item.SubShape;

public class ItemShape
        extends Item {
    private SubShape[] tabSubShapes;
    private Color[] tabColors;

    public SubShape[] getSubShapes(Layer l) {
        switch (l.ordinal()) {
            case 0: {
                return new SubShape[]{this.tabSubShapes[0], this.tabSubShapes[1], this.tabSubShapes[2], this.tabSubShapes[3]};
            }
        }
        throw new IllegalStateException("Unexpected value: " + String.valueOf((Object)l));
    }

    public Color[] getColors(Layer l) {
        switch (l.ordinal()) {
            case 0: {
                return new Color[]{this.tabColors[0], this.tabColors[1], this.tabColors[2], this.tabColors[3]};
            }
        }
        throw new IllegalStateException("Unexpected value: " + String.valueOf((Object)l));
    }

    public ItemShape(String str) {
        this.tabSubShapes = new SubShape[str.length() / 2];
        this.tabColors = new Color[str.length() / 2];
        block9: for (int i = 0; i < 4; ++i) {
            switch (str.charAt(i * 2)) {
                case 'C': {
                    this.tabSubShapes[i] = SubShape.Carre;
                    break;
                }
                case '-': {
                    this.tabSubShapes[i] = SubShape.None;
                    break;
                }
                default: {
                    throw new IllegalStateException("Unexpected value: " + str.charAt(i));
                }
            }
            switch (str.charAt(i * 2 + 1)) {
                case 'r': {
                    this.tabColors[i] = Color.Red;
                    continue block9;
                }
                case 'b': {
                    this.tabColors[i] = Color.White;
                    continue block9;
                }
                case '-': {
                    this.tabColors[i] = null;
                    continue block9;
                }
                default: {
                    throw new IllegalStateException("Unexpected value: " + str.charAt((i + 1) * 2));
                }
            }
        }
    }

    public void rotate() {
        SubShape[] bufferSubShapes = new SubShape[]{this.tabSubShapes[3], this.tabSubShapes[0], this.tabSubShapes[1], this.tabSubShapes[2]};
        Color[] bufferColors = new Color[]{this.tabColors[3], this.tabColors[0], this.tabColors[1], this.tabColors[2]};
        this.tabSubShapes = bufferSubShapes;
        this.tabColors = bufferColors;
    }

    public void stack(ItemShape ShapeSup) { // ShapeSup est empilé sur this
        for (int i=0; i<4; i++){
            if(tabSubShapes[i]==SubShape.None && ShapeSup.tabSubShapes[i]!=SubShape.None){
                tabSubShapes[i]=ShapeSup.tabSubShapes[i];
                tabColors[i]=ShapeSup.tabColors[i];
            }
        }

    }

    public ItemShape Cut() { // this et l'objet retourné correpondent au deux sorties
        SubShape[] SubShapeDroite = new SubShape[4];
        SubShape[] SubShapeGauche = new SubShape[4];
        Color[] ColorDroite = new Color[4];
        Color[] ColorGauche = new Color [4];
        for (int i=0; i<=3; i++){
            if(i>1){
                SubShapeDroite[i]=SubShape.None;
                SubShapeGauche[i]=tabSubShapes[i];
                ColorDroite[i]=null;
                ColorGauche[i]=tabColors[i];
            }
            else{
                SubShapeDroite[i]=tabSubShapes[i];
                SubShapeGauche[i]=SubShape.None;
                ColorDroite[i]=tabColors[i];
                ColorGauche[i]=null;
            }
        }
        tabSubShapes=SubShapeGauche;
        tabColors=ColorGauche;
        return new ItemShape(SubShapeDroite,ColorDroite);
    }

    public void Color(Color c) {
        for (int i=0; i<4; i++){
            if(tabSubShapes[i]!=SubShape.None) {
                tabColors[i] = c;
            }
        }
    }

    public boolean equals(Object obj) {
        ItemShape other = (ItemShape)obj;
        for (int i = 0; i < 4; ++i) {
            if (this.tabSubShapes[i] != other.tabSubShapes[i]) {
                return false;
            }
            if (this.tabColors[i] == other.tabColors[i]) continue;
            return false;
        }
        return true;
    }

    public static enum Layer {
        one,
        two,
        three;

    }
}
