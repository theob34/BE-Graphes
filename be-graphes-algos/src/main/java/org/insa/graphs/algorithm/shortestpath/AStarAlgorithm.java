package org.insa.graphs.algorithm.shortestpath;

import java.util.ArrayList;
import java.util.List;

import org.insa.graphs.model.Node;
import org.insa.graphs.model.Point;

public class AStarAlgorithm extends DijkstraAlgorithm {

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    protected List<Label> initLabelListShort() {
        //on crée la liste qui va regrouper tous nos labels
        List<Label> labelList = new ArrayList<Label>();

        Node destination = getInputData().getDestination() ;

        //Initialisation
        for (Node node : data.getGraph().getNodes()) {
            labelList.add(node.getId(), new LabelStar(node, destination));
        }

        return labelList ;
    }

    @Override
    protected List<Label> initLabelListFast() {
        //on crée la liste qui va regrouper tous nos labels
        List<Label> labelList = new ArrayList<Label>();

        Node destination = getInputData().getDestination() ;
        double dureeVolOiseau ;
        
        //Je récupère la vitesse moyenne soit précisée dans les données soit celle associée au graphe 
        //pour calculer la durée approximative de mon trajet à vol d'oiseau
        int vitesse = getInputData().getMaximumSpeed() ;
        if (vitesse == -1) {
            if (getInputData().getGraph().getGraphInformation().hasMaximumSpeed()) {
                vitesse = getInputData().getGraph().getGraphInformation().getMaximumSpeed() ;
            }
            else {
                vitesse = 1 ; //par défaut si aucune vitesse n'est précisée, je la mets à 1
            }
        }

        //Initialisation
        for (Node node : data.getGraph().getNodes()) {
            dureeVolOiseau = Point.distance(node.getPoint(), destination.getPoint()) / vitesse ;
            labelList.add(node.getId(), new LabelStar(node, destination, dureeVolOiseau));
        }

        return labelList ;
    }

    

}
