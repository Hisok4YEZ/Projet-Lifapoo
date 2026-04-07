/*
 * Decompiled with CFR 0.152.
 */
package modele.jeu;

import modele.item.ItemShape;

public class Objectif {
    private ItemShape formeAttendue;
    private int nb_formeAttendue;

    public Objectif(ItemShape forme, int nb_formes) {
        this.formeAttendue = forme;
        this.nb_formeAttendue = nb_formes;
    }

    public ItemShape getItemShape() {
        return this.formeAttendue;
    }

    public int getNb_formeAttendue() {
        return this.nb_formeAttendue;
    }
}
