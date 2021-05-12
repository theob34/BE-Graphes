package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Node;

public class Label implements Comparable<Label> {

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

    Node getNode() {
        return this.sommet_courant ;
    }
    
    //Getter cout
    double getCost() {
        return this.cout;
    }

    //Setter cout
    void setCost(double cout) {
        this.cout = cout;
    }

    //Getter marque
    boolean getMarque () {
        return this.marque;
    }

    //Setter marque
    void setMarque(boolean marque) {
        this.marque = marque;
    }

    //Getter pere
    Node getPere() {
        return this.pere;
    }

    //Setter pere
    void setPere(Node pere) {
        this.pere = pere;
    }

    /*Retourne vrai si le label est déjà marqué*/
    boolean isMarked(){
        return this.marque;
    }

    @Override
    public int compareTo(Label other) {
        return Double.compare(this.cout, other.getCost());
    }

}
