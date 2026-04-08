/*
 * Decompiled with CFR 0.152.
 */
package modele.plateau;

import modele.item.Item;
import modele.jeu.Jeu;
import modele.jeu.Objectif;
import modele.plateau.Machine;

public class ZoneLivraison
        extends Machine {
    private Jeu j;
    private int compteur;

    public ZoneLivraison(Jeu j2) {
        this.j = j2;
        this.compteur = 0;
    }

    @Override
    public void work() {
        Objectif tmp = this.j.get_Quel_objectif();
        System.out.println("ZoneLivraison: current=" + this.current.size() + " compteur=" + this.compteur + " objectif=" + tmp.getNb_formeAttendue());
        if (!this.current.isEmpty() && ((Item)this.current.getFirst()).equals(tmp.getItemShape())) {
            ++this.compteur;
            this.current.removeFirst();
            System.out.println("Forme accept\u00e9e ! compteur=" + this.compteur);
        }
        if (this.compteur == tmp.getNb_formeAttendue()) {
            this.compteur = 0;
            this.j.stopGame();
            System.out.println("Objectif suivant !");
            
            javax.swing.SwingUtilities.invokeLater(() -> {
                String[] options;
                if (this.j.currentLevel < 3) {
                    options = new String[]{"Niveau Suivant", "Menu Principal"};
                } else {
                    options = new String[]{"Menu Principal"};
                }
                
                int result = javax.swing.JOptionPane.showOptionDialog(
                    null,
                    "Félicitations ! Vous avez réussi l'objectif du Niveau " + this.j.currentLevel + ".",
                    "Niveau Terminé",
                    javax.swing.JOptionPane.DEFAULT_OPTION,
                    javax.swing.JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]
                );
                
                // Fermer les fenêtres du jeu
                for (java.awt.Window window : java.awt.Window.getWindows()) {
                    if (window instanceof vuecontroleur.VueControleur) {
                        window.dispose();
                    }
                }
                
                if (result == 0 && this.j.currentLevel < 3) {
                    // Niveau suivant
                    vuecontroleur.VueControleur vc = new vuecontroleur.VueControleur(new Jeu(this.j.currentLevel + 1));
                    vc.setVisible(true);
                } else {
                    // Menu Principal
                    new vuecontroleur.MainMenu().setVisible(true);
                }
            });
        }
    }

    @Override
    public void send() {
    }

    public int getCompteur() {
        return this.compteur;
    }
}
