
package acpathfinder.graph;

import acpathfinder.Entity;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Directed graph (uniting representation)
 * Allows parallel edges and loops
 * @author Marika Ivanova
 */
public class Graph implements Iterable<Node> {

    private Map<Integer, Node> nodes; // <nodeID, node>

    /**
     *
     * @return
     */
    @Override
    public Iterator<Node> iterator() {
        return nodes.values().iterator();
    }
    
    /**
     * Graph Constructor. Creates graph with given number of nodes
     * @param nodeCount Number of vertices
     */
    public Graph(int nodeCount) {
        nodes = new TreeMap<Integer, Node>();
        for (int i = 0; i < nodeCount; i++) {
            nodes.put(i, new Node(i));
        }
    }

    /**
     * Adds and edge to the graph
     * @param from vertex, the start of the edge
     * @param to vertex, the end of the edge
     */
    public void addUnorientedEdge(Node from, Node to) {
        from.getSuccessors().add(to);
        to.getSuccessors().add(from);
    }

    public void addUnorientedEdgeById(Integer fromId, Integer toId) {
        Node from = findNodeById(fromId);
        Node to = findNodeById(toId);
        
        if (fromId == null || toId == null) {
            throw new RuntimeException("addUnorientedEdgeById -- nonexisting nodeId");
        }
        
        addUnorientedEdge(from, to);
    }

    /**
     * Removes edge. If multiple edges removes the first occurrence.
     * @param from vertex, the start of the edge
     * @param to vertex, the end of the edge
     */
    public void removeUnorientedEdge(Node from, Node to) {
        from.getSuccessors().remove(to);
        to.getSuccessors().remove(from);
    }
    
    public void removeNode(Integer nodeId) {
        Node node = nodes.get(nodeId);
        for (Node target : node.getSuccessors()) {
            target.getSuccessors().remove(node);
        }
        nodes.remove(nodeId);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < nodes.size(); i++) {
            builder.append(i).append(" => ");
            for (Node target : nodes.get(i).getSuccessors()) {
                builder.append(target.toString());
                builder.append(" || ");
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    /**
     * Returns number of vertices fo the graph
     * @return number of vertices
     */
    public int getVertexCount() {
        return nodes.size();
    }

    /**
     * Returns number of edges in the graph
     * @return number of edges
     */
    public int getEdgeCount() {
        int count = 0;
        for (Node n : nodes.values()) {
            count += n.getSuccessors().size();
        }
        return count;
    }
    
    //----------------------------- ADDED COMPONENTS -------------------------
    
    
    /**
     * Graph Constructor. Creates a normalized graph ( = grid ) with given
     * width and height
     * @param width width of the grid graph
     * @param height height of the grid graph
     * @return new graph
     */
    public void normalise(int width, int height) {
        // check if the parameters are consistent with vertex count of the graph
        int nodeCount = width * height;
        if (nodeCount != getVertexCount()) {
            throw new IllegalArgumentException("Parameters don't match vertex count " +
                       "\n vertex count: " + nodeCount + 
                       "\n width: " + width + 
                       "\n height: " + height);
        }
        
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                // vertex id according to w and h
                int idx = i * width + j;
                
                if (j < width - 1) {
                    addUnorientedEdge(findNodeById(idx), findNodeById(idx + 1));
                }
                if (i < height - 1) {
                    addUnorientedEdge(findNodeById(idx), findNodeById(idx + width));
                }
            }
        }

    }    
    
    /**
     * 
     * @param entities 
     */
    public void addEntities(ArrayList<Entity> entities) {
       for (Entity entity: entities) {
            Node entityNode = entity.getActualNode();
            entityNode.setEntity(entity);
        } 
    }
    
    /**
     * create entities (agents) in graph. Entities belongs to the desired teams
     * @param teamIDs IDs of team 
     * @param vertexIDs starting positions
     * @return 
     */
    public ArrayList<Entity> insertEntities(int[] teamIDs, int[] vertexIDs) {
        if (teamIDs.length != vertexIDs.length) {
            throw new IllegalArgumentException("Array sizes aren't equal!");
        }
        ArrayList<Entity> entityList = new ArrayList<Entity>();
        for(int i = 0; i < vertexIDs.length; i++) {
            Node entityNode = findNodeById(vertexIDs[i]);
            Entity newEntity = new Entity(i, teamIDs[i], entityNode);
            entityNode.setEntity(newEntity);
            entityList.add(newEntity);
        }
        return entityList;
    }
    
    public Node findNodeById(int nodeID) {
        return nodes.get(nodeID);
    }
    
    public void removeNodesById(int[] nodeIDs) {
        for (int nodeID : nodeIDs) {
            removeNode(nodeID);
        }
    }
    
    /**
     * add obstacles to existing graph = remove all edges from and to give vertices
     * @param index array of vertices to remove
     */
    public void addObstacles(int[] obstacleNodeIDs) {
        removeNodesById(obstacleNodeIDs);
    }
    
    /**
     * Add goals to vertices
     * @param teamID ID of team
     * @param indices indices of nodes, where the goals will be set
     */
    public void addGoals(int teamID, int[] indices) {
        for (int nodeID : indices) {
            Node node = findNodeById(nodeID);
            node.setGoalTeamID(teamID);
        }
    }
    
    /**
     * checks whether the desired ID is a valid vertex ID
     * @param nodeId desired vertex ID
     * @return true iff there is a vertex with desired ID
     */
    public boolean isNodeIdValid(Integer nodeId) {
        return nodes.containsKey(nodeId);
    }
   
}