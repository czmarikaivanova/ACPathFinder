
package acpathfinder.graph;

import acpathfinder.Entity;
import acpathfinder.Path;
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

    
    protected Map<Integer, Node> nodes; // <nodeID, node>
    protected int layerCount;
    protected int nodesInLayer;
    protected ArrayList<Entity> entities;

    /**
     * Graph Constructor. Creates graph with given number of nodes
     * @param nodeCount Number of vertices
     */
    public Graph(int nodeCount) {
        nodes = new TreeMap<Integer, Node>();
        layerCount = 1;
        entities = new ArrayList<Entity>();
        nodesInLayer = nodeCount;
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
    public int getNodeCount() {
        return nodes.size();
    }

    public int getLayerCount() {
        return layerCount;
    }
    
    public int getNodesInLayer() {
        return nodesInLayer;
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
        int nodeCnt = width * height;
        if (nodeCnt != getNodeCount()) {
            throw new IllegalArgumentException("Parameters don't match vertex count " +
                       "\n vertex count: " + nodeCnt + 
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
//    public void addEntities(ArrayList<Entity> entities) {
//       for (Entity entity: entities) {
//            Node entityNode = entity.getActualNode();
//            entityNode.setEntity(entity);
//        } 
//       this.entities = entities;
//    }
    
    public Entity getEntity(int nodeId) {
        return findNodeById(nodeId).getEntity();
    }

    /**
     * create entities (agents) in graph. Entities belongs to the desired teams
     * @param teamIDs IDs of team 
     * @param vertexIDs starting positions
     * @return 
     */
    public ArrayList<Entity> insertEntities(int[] teamIDs, int[] vertexIDs, int[] goalNodeIds) {
        if (teamIDs.length != vertexIDs.length || teamIDs.length != goalNodeIds.length) {
            throw new IllegalArgumentException("Array sizes aren't equal!");
        }
        ArrayList<Entity> entityList = new ArrayList<Entity>();
        for(int i = 0; i < vertexIDs.length; i++) {
            Node entityNode = findNodeById(vertexIDs[i]);
            Node targetNode = findNodeById(goalNodeIds[i]);
            Entity newEntity = new Entity(i, teamIDs[i], entityNode, targetNode);
            entityNode.setEntity(newEntity);
            entityList.add(newEntity);
        }
        return entityList;
    }
    
    public Node findNodeById(int nodeID) {
        return nodes.get(nodeID);
    }
    
    /**
     * remove nodes by id.
     * rename (change ID) remaining nodes, so their IDs are 0..number of nodes - 1
     * reconstruct a new TreeMap of nodes
     * @param nodeIDs array of node IDs
     */
    public void removeNodesById(int[] nodeIDs) {
        for (int nodeID : nodeIDs) {
            removeNode(nodeID);
        }
        Iterator iterator = iterator();
        int i = 0; // new node ID
        TreeMap<Integer, Node> newNodes = new TreeMap<Integer, Node>();
        while (iterator.hasNext()) {
            Node node = (Node) iterator.next();
            node.setId(i);
            newNodes.put(node.getId(), node);
            i++;
        }
        nodes = newNodes;
    }
    
    /**
     * add obstacles to existing graph = remove all edges from and to give vertices
     * @param index array of vertices to remove
     */
    public void addObstacles(int[] obstacleNodeIDs) {
        removeNodesById(obstacleNodeIDs);
    }
    
    /**
     * Add goals to vertices. There is just information about team, not about
     * the entity. Entity itself has exact node as goal (set of nodes)
     * @param teamID ID of team
     * @param indices indices of nodes, where the goals will be set
     * 
     * not more then one so far
     */
    public void addGoals(int teamID, int[] indices) {
        for (int nodeID : indices) {
            findNodeById(nodeID).setGoalTeamID(teamID);
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
    
        // two nodes in spatial graph represents the same node, if they are in 
    // one line (by edges)
    public boolean areTheSame(Node n1, Node n2) {
        return ((n1.getId() % nodesInLayer) == (n2.getId() % nodesInLayer));
    }
    
    @Override
    public Iterator<Node> iterator() {
        return nodes.values().iterator();
    }

    public Path rearrangePath(Path path) {
        Path rearrangedPath = new Path(path.getId());
        for (Node node: path) {
            int nodeId = node.getId();
            Node nodeInOriginalGraph = findNodeById(nodeId % nodesInLayer);
            rearrangedPath.addLast(nodeInOriginalGraph);
        }
        return rearrangedPath;
    }
    
}
