package org.insa.graphs.algorithm.utils;

import org.insa.graphs.algorithm.shortestpath.*;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.model.AccessRestrictions;
import java.util.EnumMap;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;

import org.insa.graphs.model.*;
import org.insa.graphs.model.RoadInformation.RoadType;
import org.junit.BeforeClass;
import org.junit.Test;
public class ShortestPathTest {

    // Small graph use for tests
    private static Graph graph;

    // List of nodes
    private static Node[] nodes;

    // List of arcs in the graph, a2b is the arc from node A (0) to B (1).
    @SuppressWarnings("unused")
    private static Arc a2b, a2c, b2c, b2f, c2d, c2g, d2g, d2f, e2h, f2g;

    // Some paths...
    private static ShortestPathData normalCarPathShortest, normalCarPathFastest, nonConnexePath, normalCyclePathShortest, normalCyclePathFastest, nullPath, cycleOnlyPath;

    @BeforeClass
    public static void initAll() throws IOException {

        // Map de tous les transports et leur restrictions
        EnumMap<AccessRestrictions.AccessMode, AccessRestrictions.AccessRestriction> restrictionsCycleOnly = new EnumMap<>(AccessRestrictions.AccessMode.class);
        EnumMap<AccessRestrictions.AccessMode, AccessRestrictions.AccessRestriction> restrictionsAllVehicules = new EnumMap<>(AccessRestrictions.AccessMode.class);
        for (AccessRestrictions.AccessMode mode : AccessRestrictions.AccessMode.ALL){ // Parcourir tous les véhicules
            restrictionsAllVehicules.put(mode, AccessRestrictions.AccessRestriction.ALLOWED); // Les autoriser tous
            restrictionsCycleOnly.put(mode, AccessRestrictions.AccessRestriction.FORBIDDEN);  // Les interdire tous
        }
        restrictionsCycleOnly.put(AccessRestrictions.AccessMode.BICYCLE, AccessRestrictions.AccessRestriction.ALLOWED); // Autoriser que les vélos

        // Vehicule (car = tous les véhicules, cycle = que les vélos) - vitesse (en km/h)
        RoadInformation carSpeed30 = new RoadInformation(RoadType.MOTORWAY,  new AccessRestrictions(restrictionsAllVehicules), false, 30, ""),
                        carSpeed50 = new RoadInformation(RoadType.MOTORWAY,  new AccessRestrictions(restrictionsAllVehicules), false, 50, ""),
                        carSpeed80 = new RoadInformation(RoadType.MOTORWAY,  new AccessRestrictions(restrictionsAllVehicules), false, 80, ""),
                        carSpeed90 = new RoadInformation(RoadType.MOTORWAY,  new AccessRestrictions(restrictionsAllVehicules), false, 90, ""), 
                        carSpeed110 = new RoadInformation(RoadType.MOTORWAY,  new AccessRestrictions(restrictionsAllVehicules), false, 110, ""),
                        cycleSpeed10 = new RoadInformation(RoadType.CYCLEWAY, new AccessRestrictions(restrictionsCycleOnly), false, 10, ""),
                        cycleSpeed20 = new RoadInformation(RoadType.CYCLEWAY, new AccessRestrictions(restrictionsCycleOnly), false, 20, ""),
                        cycleSpeed30 = new RoadInformation(RoadType.CYCLEWAY, new AccessRestrictions(restrictionsCycleOnly), false, 30, "");

        // Create nodes
        nodes = new Node[8];
        for (int i = 0; i < nodes.length; ++i) {
            nodes[i] = new Node(i, null);
        }

        // Add arcs...
        a2b = Node.linkNodes(nodes[0], nodes[1], 40, carSpeed80, null);
        a2c = Node.linkNodes(nodes[0], nodes[2], 100, carSpeed50, null);
        b2c = Node.linkNodes(nodes[1], nodes[2], 30, carSpeed30, null);
        b2f = Node.linkNodes(nodes[1], nodes[5], 45, carSpeed90, null);
        c2d = Node.linkNodes(nodes[2], nodes[3], 90, carSpeed30, null);
        c2g = Node.linkNodes(nodes[2], nodes[6], 50, cycleSpeed20, null);
        d2g = Node.linkNodes(nodes[3], nodes[6], 10, cycleSpeed10, null);
        d2f = Node.linkNodes(nodes[3], nodes[5], 220, carSpeed110, null);
        e2h = Node.linkNodes(nodes[4], nodes[7], 10, carSpeed30, null);
        f2g = Node.linkNodes(nodes[5], nodes[6], 45, cycleSpeed30, null);

        graph = new Graph("ID", "", Arrays.asList(nodes), null);
    
        normalCarPathShortest   = new ShortestPathData(graph, nodes[0], nodes[3], ArcInspectorFactory.getAllFilters().get(1)); // Chemin de a à d, shortest - only cars
        normalCarPathFastest    = new ShortestPathData(graph, nodes[0], nodes[3], ArcInspectorFactory.getAllFilters().get(3)); // Chemin de a à d, fastest  - only cars
        nonConnexePath          = new ShortestPathData(graph, nodes[2], nodes[4], ArcInspectorFactory.getAllFilters().get(0)); // Chemin de c à e, shortest - all roads
        normalCyclePathShortest = new ShortestPathData(graph, nodes[1], nodes[6], ArcInspectorFactory.getAllFilters().get(0)); // Chemin de b à g, shortest - all roads
        normalCyclePathFastest  = new ShortestPathData(graph, nodes[1], nodes[6], ArcInspectorFactory.getAllFilters().get(2)); // Chemin de b à g, fastest - all roads
        nullPath                = new ShortestPathData(graph, nodes[0], nodes[0], ArcInspectorFactory.getAllFilters().get(1)); // Chemin de a à a, shortest - all roads
        cycleOnlyPath           = new ShortestPathData(graph, nodes[3], nodes[6], ArcInspectorFactory.getAllFilters().get(1)); // Chemin de d à g, shortest - only cars

    }

    DijkstraAlgorithm DijAlgo;
    Path cheminAttendu, cheminTrouve;
    String testName;

    @Test
    public void testNormalCarPath() {
        
        // Shortest path
        testName = "Chemin le plus court en voiture";
        DijAlgo = new DijkstraAlgorithm(normalCarPathShortest);
        cheminAttendu = Path.createShortestPathFromNodes(graph, Arrays.asList(new Node[] { nodes[0], nodes[1], nodes[2], nodes[3] })); // a -> b -> c -> d        
        cheminTrouve = DijAlgo.run().getPath();
        System.out.println("Test n° 1 : " + testName + " | chemin Attendu : " + cheminAttendu + "| chemin Trouvé : " + cheminTrouve);
        assertTrue(("Test n° 1 : " + testName + " | chemin Attendu : " + cheminAttendu.toString() + "| chemin Trouvé : " + cheminTrouve), (cheminAttendu.toString().compareTo(cheminTrouve.toString()))==0);
        
        // Fastest path
        testName = "Chemin le plus rapide en voiture";
        DijAlgo = new DijkstraAlgorithm(normalCarPathFastest);
        cheminAttendu = Path.createShortestPathFromNodes(graph, Arrays.asList(new Node[] { nodes[0], nodes[1], nodes[5], nodes[3] })); // a -> b -> f -> d        
        cheminTrouve = DijAlgo.run().getPath();
        assertTrue(("Test n° 2 : " + testName + " | chemin Attendu : " + cheminAttendu + "| chemin Trouvé : " + cheminTrouve), cheminAttendu.toString().compareTo(cheminTrouve.toString())==0);
    }

    @Test
    public void testNormalCyclePath() {
        // Shortest path
        testName = "Chemin le plus court en vélo";
        DijAlgo = new DijkstraAlgorithm(normalCyclePathShortest);
        cheminAttendu = Path.createShortestPathFromNodes(graph, Arrays.asList(new Node[] {nodes[1], nodes[2], nodes[6]})); // b -> c -> g        
        cheminTrouve = DijAlgo.run().getPath();
        assertTrue(("Test n° 3 : " + testName + " | chemin Attendu : " + cheminAttendu + "| chemin Trouvé : " + cheminTrouve), cheminAttendu.toString().compareTo(cheminTrouve.toString())==0);

        // Fastest path
        testName = "Chemin le plus rapide en vélo";
        DijAlgo = new DijkstraAlgorithm(normalCyclePathFastest);
        cheminAttendu = Path.createShortestPathFromNodes(graph, Arrays.asList(new Node[] { nodes[1], nodes[5], nodes[6]})); // b -> f -> g        
        cheminTrouve = DijAlgo.run().getPath();
        assertTrue(("Test n° 4 : " + testName + " | chemin Attendu : " + cheminAttendu + "| chemin Trouvé : " + cheminTrouve), cheminAttendu.toString().compareTo(cheminTrouve.toString())==0);
    }


    @Test
    public void testImpossiblePath() {
        // Non connexe
        testName = "Chemin inexistant";
        DijAlgo = new DijkstraAlgorithm(nonConnexePath);
        cheminAttendu = null;
        cheminTrouve = DijAlgo.run().getPath();
        assertTrue(("Test n° 5 : " + testName + " | chemin Attendu : " + cheminAttendu + "| chemin Trouvé : " + cheminTrouve), cheminAttendu == cheminTrouve);

        // Chemin nul
        testName = "Chemin nul (point de départ = point d'arrivé)";
        DijAlgo = new DijkstraAlgorithm(nullPath);
        cheminAttendu = Path.createShortestPathFromNodes(graph, Arrays.asList(new Node[] {nodes[0]}));;
        cheminTrouve = DijAlgo.run().getPath();
        assertTrue(("Test n° 6 : " + testName + " | chemin Attendu : " + cheminAttendu + "| chemin Trouvé : " + cheminTrouve), cheminAttendu.toString().compareTo(cheminTrouve.toString())==0);
    
        // Route non-empruntable
        testName = "Chemin nul (point de départ = point d'arrivé)";
        DijAlgo = new DijkstraAlgorithm(cycleOnlyPath);
        cheminAttendu = null;
        cheminTrouve = DijAlgo.run().getPath();
        assertTrue(("Test n° 7 : " + testName + " | chemin Attendu : " + cheminAttendu + "| chemin Trouvé : " + cheminTrouve), cheminAttendu == cheminTrouve);
    }

    @Test
    public void testBellmanFordDijkstra() {

        // Shortest
        testName = "Comparaison Bellman Ford & Dijkstra : chemin normal voiture shortest";
        BellmanFordAlgorithm BellAlgo = new BellmanFordAlgorithm(normalCarPathShortest);
        DijAlgo = new DijkstraAlgorithm(normalCarPathShortest);
        cheminAttendu = BellAlgo.run().getPath();
        cheminTrouve = DijAlgo.run().getPath();
        assertTrue(("Test n° 8 : " + testName + " | chemin Bellman Ford : " + cheminAttendu + "| chemin Dijkstra : " + cheminTrouve), cheminAttendu.toString().compareTo(cheminTrouve.toString())==0);

        // Fastest
        testName = "Comparaison Bellman Ford & Dijkstra : chemin normal voiture fastest";
        BellAlgo = new BellmanFordAlgorithm(normalCarPathFastest);
        DijAlgo = new DijkstraAlgorithm(normalCarPathFastest);
        cheminAttendu = BellAlgo.run().getPath();
        cheminTrouve = DijAlgo.run().getPath();
        assertTrue(("Test n° 9 : " + testName + " | chemin Bellman Ford : " + cheminAttendu + "| chemin Dijkstra : " + cheminTrouve), cheminAttendu.toString().compareTo(cheminTrouve.toString())==0);

    }

}
