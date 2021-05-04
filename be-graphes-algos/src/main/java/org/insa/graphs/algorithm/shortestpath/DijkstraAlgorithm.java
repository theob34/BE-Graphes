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

    private final double INFINI = 10000000.0;

    //on créer la liste qui va regrouper tous nos labels
    List<Label> labelList = new ArrayList<Label>();

    BinaryHeap<Node> nodeHeap = new BinaryHeap<Node>();

    @Override
    protected ShortestPathSolution doRun() {

        final ShortestPathData data = getInputData();

        Node sommet_initial = data.getOrigin();
        
        //Initialisation
        for (Node node : data.getGraph().getNodes()) {
            labelList.add(node.getId(), new Label(node));
        }

        //Je met le cout du sommet initial à 0 et je l'ajoute dans le tas
        labelList.get(sommet_initial.getId()).setCost(0);
        nodeHeap.insert(sommet_initial);

        Node minTas;
        while (!nodeHeap.isEmpty()) {
            //On récupère le sommet et on le marque
            minTas = nodeHeap.deleteMin();
            labelList.get(minTas.getId()).setMarque(true);
            //On récupère tous les successeurs
            for (Arc arc : minTas.getSuccessors()) {
                Label successor = labelList.get(arc.getDestination().getId());
                //Si le successeur n'est pas marque alors je l'étudie
                if (!successor.getMarque()) {
                    //Si le cout est plus faible en passant par x alors je le met à jour
                    if (successor.getCost() > labelList.get(minTas.getId()).getCost() + arc.getLength()) {
                        //Si le successeur est déjà dans le tas, je le supprime (permet la mise à jour)
                        if ((!successor.getMarque()) && (successor.getCost() != INFINI)) {
                            nodeHeap.remove(successor.sommet_courant);
                        }
                        //Je mets à jour le coût
                        successor.setCost(labelList.get(minTas.getId()).getCost() + arc.getLength());
                        //Je rajoute le successeur et définit son nouveau père
                        nodeHeap.insert(successor.sommet_courant);
                        successor.setPere(minTas);
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
        nodesSolution.add(node);
        node = labelList.get(node.getId()).getPere() ;

        while (node != sommet_initial) {
            nodesSolution.add(1, node);
            node = labelList.get(node.getId()).getPere() ;
        }
        nodesSolution.add(0, sommet_initial);

        Path pathFinal = new Path(data.getGraph());

        pathFinal = Path.createShortestPathFromNodes(data.getGraph(), nodesSolution) ;

        
        ShortestPathSolution solution = new ShortestPathSolution(data, ShortestPathSolution.Status.OPTIMAL, pathFinal);
        return solution;
    }


}
