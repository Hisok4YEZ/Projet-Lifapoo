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
            this.j.objectif_suivant();
            this.compteur = 0;
            System.out.println("Objectif suivant !");
        }
    }

    @Override
    public void send() {
    }

    public int getCompteur() {
        return this.compteur;
    }
}
