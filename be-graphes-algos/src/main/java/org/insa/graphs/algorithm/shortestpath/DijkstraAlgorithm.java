package org.insa.graphs.algorithm.shortestpath;

import java.util.ArrayList;
import java.util.List;

import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.model.*;
import org.insa.graphs.model.Node;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    private final double INFINI = 10000000.0;//Double.POSITIVE_INFINITY;

    @Override
    protected ShortestPathSolution doRun() {

        final ShortestPathData data = getInputData();

        //on crée la liste qui va regrouper tous nos labels
        List<Label> labelList = new ArrayList<Label>();

        BinaryHeap<Label> nodeHeap = new BinaryHeap<Label>();

        Node sommet_initial = data.getOrigin();
        
        //Initialisation
        for (Node node : data.getGraph().getNodes()) {
            labelList.add(node.getId(), new Label(node));
        }

        //Je mets le cout du sommet initial à 0 et je l'ajoute dans le tas
        labelList.get(sommet_initial.getId()).setCost(0);
        nodeHeap.insert(labelList.get(sommet_initial.getId()));

        Label minTas;
        while (!nodeHeap.isEmpty()) {
            //On récupère le sommet et on le marque
            minTas = nodeHeap.deleteMin();
            minTas.setMarque(true);
            //On récupère tous les successeurs
            for (Arc arc : minTas.getNode().getSuccessors()) {
                if (data.isAllowed(arc)) {
                    Label successor = labelList.get(arc.getDestination().getId());
                    //Si le successeur n'est pas marque alors je l'étudie
                    if (!successor.getMarque()) {
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
                }
            }

        }

        /*Il faut finir d'implémenter la solution
        Pour cela deux solutions :
            - Soit on remplace pere (qui est node) par l'arc
            - Soit on fait notre liste de Node en redescedant depuis GetDestination vers GetOrigin à l'aide de l'attribut pere et on utilise ensuite la methode CreateShortestPath implémentée au début 
        */

        List<Node> nodesSolution = new ArrayList<Node>() ;
        Node node = data.getDestination() ;
        System.out.println("destination : " + node.getId()) ;
        nodesSolution.add(node);
        node = labelList.get(node.getId()).getPere() ;

        while (node != sommet_initial) {
            System.out.println("node : " + node.getId()) ;
            nodesSolution.add(0, node);
            node = labelList.get(node.getId()).getPere() ;
        }
        System.out.println("sommet initial : " + sommet_initial.getId()) ;
        nodesSolution.add(0, sommet_initial);

        for (Node point : nodesSolution) {
            System.out.println("point : " + point.getId()) ;
        }

        Path pathFinal = Path.createShortestPathFromNodes(data.getGraph(), nodesSolution) ;
        
        ShortestPathSolution solution = new ShortestPathSolution(data, ShortestPathSolution.Status.OPTIMAL, pathFinal);
        return solution;
    }
}
