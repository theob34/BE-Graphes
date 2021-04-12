package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Node;

public class Label {

    private final double INFINI = 10000000.0;

    /*sommet associé à ce label (sommet ou numéro de sommet).*/
    Node sommet_courant ;

    /*booléen, vrai lorsque le coût min de ce sommet est définitivement connu par l'algorithme.*/
    boolean marque;

    /*valeur courante du plus court chemin depuis l'origine vers le sommet.*/
    double cout;

    /*correspond au sommet précédent sur le chemin correspondant au plus court chemin courant.*/
    Node pere;

    /*Constructeur de la classe valeur par défaut*/
    Label(Node sommet_courant) {
        this.sommet_courant = sommet_courant;
        this.marque = false;
        this.cout = INFINI;
        this.pere = null;
    }

    /*Constructeur de la classe avec spécification de toutes les valeurs*/
    Label(Node sommet_courant, boolean marque, double cout, Node pere) {
        this.sommet_courant = sommet_courant;
        this.marque = marque;
        this.cout = cout;
        this.pere = pere;
    }
    
    /*Retourne le cout du label*/
    double getCost() {
        return this.cout;
    }

    /*Affecte la valeur cout*/
    void setCost(double cout) {
        this.cout = cout;
    }

    boolean getMarque () {
        return this.marque;
    }

    /*Met la marque à marque*/
    void setMarque(boolean marque) {
        this.marque = marque;
    }

    void setPere(Node pere) {
        this.pere = pere;
    }

    /*Retourne vrai si le label est déjà marqué*/
    boolean isMarked(){
        return this.marque;
    }

}
