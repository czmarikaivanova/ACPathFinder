
package acpathfinder;

import acpathfinder.graph.Graph;
import acpathfinder.graph.Node;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author Marika Ivanova
 * Performs a Dijkstra search for given graph, source and target.
 * The search procedure is executed for every entity and the nodes included in
 * the found path are reserved. Thus the order has a significant role, since the 
 * solution can be lost, when the order of agents is wrongly selected.
 */
public class DijkstraSearch {

    // hashmap of distance from source to all vertices in the graph
    private HashMap<Node, Integer> distances;
    // hashmap of parent nodes for each vertex
    private HashMap<Node, Node> parents;
    // graph to be searched
    private Graph graph;    
    // unvisited nodes in dijkstra's algorithm
    ArrayList<Node> unvisited;  // todo: priority queue
    // source node ID
    private Node source;
    // target node ID
    private Node target;
    private int nodeCount;
    
    private static final int INFINITY = 9999999;
    private ArrayList<Path> foundPathes;
    
    public DijkstraSearch(Graph graph, Node source, Node target) {
        this.graph = graph;
        this.source = source;
        this.target = target;
        this.nodeCount = graph.getNodeCount();
        distances = new HashMap(nodeCount);
        parents = new HashMap(nodeCount);
    }

    public DijkstraSearch(Graph graph) {
        this.graph = graph;
        this.nodeCount = graph.getNodeCount();
        distances = new HashMap(nodeCount);
        parents = new HashMap(nodeCount);
    }
    
    public DijkstraSearch(Graph graph, Entity entity) {
        this.graph = graph;
        source = entity.getActualNode();
        target = entity.getSingleTarget();
        nodeCount = graph.getNodeCount();
        distances = new HashMap(nodeCount);
        parents = new HashMap(nodeCount);        
    }

    public Node getSource() {
        return source;
    }

    public void setSource(Node source) {
        this.source = source;
    }

    public Node getTarget() {
        return target;
    }

    public void setTarget(Node target) {
        this.target = target;
    }

    public void setFoundPathes(ArrayList<Path> foundPathes) {
        this.foundPathes = foundPathes;
    }
    
    
    public Path search() {
        // initialize variables (unvisited, distances, parents)
        unvisited = new ArrayList<Node>();
        for (Node node : graph) {
            if (!node.isReserved()) { // if the node is not a parto some path of other agent
                unvisited.add(node);
                distances.put(node, INFINITY); 
                parents.put(node, null);
            }
        }
        
        distances.put(source, 0);
        
        while(!unvisited.isEmpty()) {
            Node minNode = extractMin(unvisited);
            int distanceToMinNode = distances.get(minNode);
            if (distanceToMinNode == INFINITY) {
                // the rest of nodes (unvisited) are unaccessible
                // i.e. there is no path from source to target
                return null;
            }
            // if we've already reached the target node, we can stop searching
            if (graph.areTheSame(minNode, target)) {
                // IMPORTANT: target is now somewhere deeper in spatial graph - but indeed it represents the original target.
                target = minNode;
                return reconstructPath();
            }
            Set<Node> succesors = minNode.getSuccessors();
            for (Node s: succesors) {
                if (!isCrossing(s, minNode)) {
                    // alternative distance
                    int alt = distanceToMinNode + 1; // TODO: later we'll consider general edge values
                    if (alt < distances.get(s)) {
                        distances.put(s, alt);
                        parents.put(s, minNode);
                    }
                }
            }
        }
        return reconstructPath();
    }
    
    // extrat a node with minimum distance
    private Node extractMin(ArrayList<Node> nodeList) {
        if (nodeList.isEmpty()) {
            throw new RuntimeException("Cannot extractMin from empty nodeList");
        }
        // distance for the closest node
        int min = INFINITY; 
        Node closestNode = nodeList.get(0); // get first element
        // current distance between source and actual node
        for (Node node: nodeList) {
            int dist = distances.get(node);
            if (dist < min) {
                closestNode = node;
                min = dist;
            }
        }
        nodeList.remove(closestNode);
        return closestNode;
    }
    
    /**
     * private method to reconstruct found path
     * It's used when search algorithm needs to return a path
     * It also set occupy at time step for certain vertices on the path.
     * @return found path (class that extends LinkedList<LinkedGraphNode>)
     */
    private Path reconstructPath() {
        Path path = new Path(source.getId()); 
        Node node = target;
        source.reserve(path.getId());
        while (!node.equals(source)) {
            
            path.addFirst(node);
            node.reserve(path.getId());
            node = parents.get(node);
        }
        //add source node
        path.addFirst(node);        
        return path;
    }

    private boolean isCrossing(Node s, Node minNode) {
        int sid = s.getId();
        int mid = minNode.getId();
        int nodesInLayer = graph.getNodesInLayer();
        Node preS = graph.findNodeById(sid - nodesInLayer);
        Node postMin = graph.findNodeById(mid + nodesInLayer);
        if (!preS.isReserved() || !postMin.isReserved()) {
            return false;
        }
        // some other entity from s will come to minNode
        if (preS.getReservationId() == postMin.getReservationId()) {
            return true;
        }
        return false;
    }
 
}