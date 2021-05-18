package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Node;
import org.insa.graphs.model.Point;

public class LabelStar extends Label{

    double volOiseau ;

    Node destination ;

    LabelStar(Node sommet_courant, Node destination){
        super(sommet_courant);
        this.destination = destination ;
        this.volOiseau = Point.distance(this.sommet_courant.getPoint(), this.destination.getPoint()) ;
    }

    LabelStar(Node sommet_courant, Node destination, double volOiseau){
        super(sommet_courant);
        this.destination = destination ;
        this.volOiseau = volOiseau;
    }

    LabelStar(Node sommet_courant, boolean marque, double cout, Node pere, Node destination) {
        super(sommet_courant, marque, cout, pere) ;
        this.destination = destination ;
        this.volOiseau = Point.distance(this.sommet_courant.getPoint(), this.destination.getPoint()) ;
    }

    //Getter Destination
    Node getDestination() {
        return this.destination ;
    }

    //Setter Destination
    void setDestination(Node destination) {
        this.destination = destination ;
    }

    //Getter Total Cout
    double getTotalCost(){
        return this.getCost() + this.volOiseau ;
    }

    //Getter Total Cout
    double getVolOiseau(){
        return this.volOiseau ;
    }

    //Setter vol Oiseau
    void setvolOiseau(double volOiseau) {
        this.volOiseau = volOiseau ;
    }

    @Override
    public int compareTo(Label other) {
        return Double.compare(this.getTotalCost(), ((LabelStar)other).getTotalCost());
    }
    
}
