package org.insa.graphs.algorithm.shortestpath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.model.*;
import org.insa.graphs.model.Node;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    private final double INFINI = 10000000.0;

    protected List<Label> initLabelListShort() {
        //on crée la liste qui va regrouper tous nos labels
        List<Label> labelList = new ArrayList<Label>();

        //Initialisation
        for (Node node : data.getGraph().getNodes()) {
            labelList.add(node.getId(), new Label(node));
        }

        return labelList ;
    }

    protected List<Label> initLabelListFast() {
        return initLabelListShort() ;
    }

    @Override
    protected ShortestPathSolution doRun() { 

        final ShortestPathData data = getInputData();

        //Si le départ et l'arrivée sont le même node, alors je retourne un chemin avec seulement 1 node
        if (data.getOrigin() == data.getDestination()) {
            return new ShortestPathSolution(data, ShortestPathSolution.Status.OPTIMAL, Path.createShortestPathFromNodes(data.getGraph(), Arrays.asList(data.getOrigin())));
        }

        BinaryHeap<Label> nodeHeap = new BinaryHeap<Label>();

        Node sommet_initial = data.getOrigin();

        List<Label> labelList ;

        if (data.getMode() == ShortestPathData.Mode.LENGTH) {
            labelList = initLabelListShort() ;
        }
        else {
            labelList = initLabelListFast() ;
        }

        //Je mets le cout du sommet initial à 0 et je l'ajoute dans le tas
        labelList.get(sommet_initial.getId()).setCost(0);
        nodeHeap.insert(labelList.get(sommet_initial.getId()));
        notifyOriginProcessed(sommet_initial);

        Label minTas;
        while ((!labelList.get(data.getDestination().getId()).marque) && (!nodeHeap.isEmpty())) {
            //On récupère le sommet et on le marque
            minTas = nodeHeap.deleteMin();
            minTas.setMarque(true);
            notifyNodeMarked(minTas.getNode());
            //On récupère tous les successeurs
            for (Arc arc : minTas.getNode().getSuccessors()) {
                if (data.isAllowed(arc)) {
                    Label successor = labelList.get(arc.getDestination().getId());
                    notifyNodeReached(successor.getNode());
                    //Si le successeur n'est pas marque alors je l'étudie
                    if (!successor.getMarque()) {
                        //Si je cherche le plus court chemin, je compare les tailles
                        if (data.getMode() == ShortestPathData.Mode.LENGTH) {
                            //Si le cout est plus faible en passant par x alors je le met à jour
                            if (successor.getCost() > (minTas.getCost() + arc.getLength())) {
                                //Si le successeur est déjà dans le tas, je le supprime (permet la mise à jour)
                                if ((!successor.getMarque()) && (successor.getCost() != INFINI)) {
                                    nodeHeap.remove(successor);
                                }
                                //Je mets à jour le coût
                                successor.setCost(minTas.getCost() + arc.getLength());
                                //Je rajoute le successeur et définit son nouveau père
                                nodeHeap.insert(successor);
                                successor.setPere(minTas.getNode());
                            }
                        }
                        else {
                            //Si le cout est plus faible en passant par x alors je le met à jour
                            if (successor.getCost() > (minTas.getCost() + arc.getMinimumTravelTime())) {
                                //Si le successeur est déjà dans le tas, je le supprime (permet la mise à jour)
                                if ((!successor.getMarque()) && (successor.getCost() != INFINI)) {
                                    nodeHeap.remove(successor);
                                }
                                //Je mets à jour le coût
                                successor.setCost(minTas.getCost() + arc.getMinimumTravelTime());
                                //Je rajoute le successeur et définit son nouveau père
                                nodeHeap.insert(successor);
                                successor.setPere(minTas.getNode());
                            }
                        }                        
                    }
                }
            }

        }
        notifyDestinationReached(data.getDestination());

        ShortestPathSolution solution = null ;

        if ((!labelList.get(data.getDestination().getId()).marque)) {
            solution = new ShortestPathSolution(data, ShortestPathSolution.Status.INFEASIBLE);
        } 
        else {
            List<Node> nodesSolution = new ArrayList<Node>() ;
            Node node = data.getDestination() ;
            nodesSolution.add(node);
            node = labelList.get(node.getId()).getPere() ;
            while (node != sommet_initial) {
                nodesSolution.add(0, node);
                node = labelList.get(node.getId()).getPere() ;
            }
            nodesSolution.add(0, sommet_initial);

            Path pathFinal ;

            if (data.getMode() == ShortestPathData.Mode.LENGTH) {
                pathFinal = Path.createShortestPathFromNodes(data.getGraph(), nodesSolution) ;
            }
            else {
                pathFinal = Path.createFastestPathFromNodes(data.getGraph(), nodesSolution) ;
            }
            //Je regarde si je dispose d'un chemin, ou seulement d'un Node auquel cas, mon algrithme n'a pas trouvé de chemin
            if (!(pathFinal.size() < 1)) {
                solution = new ShortestPathSolution(data, ShortestPathSolution.Status.OPTIMAL, pathFinal);
            }
        }
        return solution;
    }

}
