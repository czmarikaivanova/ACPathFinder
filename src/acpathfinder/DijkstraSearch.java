
package acpathfinder;

import acpathfinder.graph.Graph;
import acpathfinder.graph.Node;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author Marika Ivanova
 */
public class DijkstraSearch implements SearchIface {

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
    private int vertexCount;
    
    private static final int INFINITY = 9999999;

    
    private ArrayList<Path> foundPathes;
    
    public DijkstraSearch(Graph graph, Node source, Node target) {
        this.graph = graph;
        this.source = source;
        this.target = target;
        this.vertexCount = graph.getVertexCount();
        distances = new HashMap(vertexCount);
        parents = new HashMap(vertexCount);
    }

    public DijkstraSearch(Graph graph) {
        this.graph = graph;
        this.vertexCount = graph.getVertexCount();
        distances = new HashMap(vertexCount);
        parents = new HashMap(vertexCount);
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
    
    
    @Override
    public Path search() {
        // initialize variables (unvisited, distances, parents)
        unvisited = new ArrayList<Node>();
        for (Node node : graph) {
            unvisited.add(node);
            distances.put(node, INFINITY); 
            parents.put(node, null);
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
            // if we've already reached the target node, we can stop search
            if (minNode.equals(target)) {
                return reconstructPath();
            }
            Set<Node> succesors = minNode.getSuccessors();
            for (Node s: succesors) {
                // alternative distance
                int alt = distanceToMinNode + 1; // TODO: later we'll consider general edge values
                if (alt < distances.get(s) && !isOccupiedAtStep(alt, minNode)) {
                    distances.put(s, alt);
                    parents.put(s, minNode);
                }
            }
        }
        return reconstructPath();
    }
    
    private Node extractMin(ArrayList<Node> nodeList) {
        if (nodeList.isEmpty()) {
            throw new RuntimeException("Cannot extractMin from empty nodeList");
        }
        // distance for the closest node
        int min = INFINITY; 
        Node closestNode = nodeList.get(0); // get first element
        // current distance between source and actual noe
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
     * private method to reconstruct finded path
     * It's used when search algorithm needs to return a path
     * @return finded path (class that extends LinkedList<LinkedGraphNode>)
     */
    private Path reconstructPath() {
        Path path = new Path(source.getId()); 
        Node node = target;
        while (!node.equals(source)) {
            path.addFirst(node);
            node = parents.get(node);
        }
        //add source node
        path.addFirst(node);
        
        return path;
    }

    private boolean isOccupiedAtStep(int step, Node node) {
        if (foundPathes == null || foundPathes.isEmpty()) return false;
        for (Path p: foundPathes) {
            if (step >= p.size()) continue;
            if (p.get(step).equals(node)) return true;
        }
        return false;
    }
    

 
    
}