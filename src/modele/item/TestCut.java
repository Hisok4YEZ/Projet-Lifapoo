package modele.item;

public class TestCut {
    public static void main(String[] args) {
        ItemShape s = new ItemShape("CrCrCrCr");
        ItemShape right = s.Cut();
        
        System.out.println("Left part:");
        for(int i=0; i<4; i++) {
            System.out.println("SubShape: " + s.getSubShapes(ItemShape.Layer.one)[i]);
        }
        
        System.out.println("\nRight part:");
        for(int i=0; i<4; i++) {
            System.out.println("SubShape: " + right.getSubShapes(ItemShape.Layer.one)[i]);
        }
    }
}
