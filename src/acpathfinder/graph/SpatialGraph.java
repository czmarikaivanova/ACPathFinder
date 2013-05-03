
package acpathfinder;

import acpathfinder.graph.Graph;
import acpathfinder.graph.Node;

/**
 *
 * @author marion
 */
public class SpatialGraph extends Graph {
    // int nodeCnt -- inherited 

    private Graph originalGraph;
    
    public SpatialGraph(Graph graph, int layerCount) {
        super(graph.getNodeCount()); // already creates nodes and put them into inherited treemap nodes
        originalGraph = graph;
        this.layerCount = layerCount;
        nodesInLayer = graph.getNodeCount(); // now there are only nodes from original node
        for (int i = 1; i < layerCount; i++) { // 1st layer is already present
            for (int j = 0; j < nodesInLayer; j++) {
                Node node = new Node(i * nodesInLayer + j); 
                nodes.put(node.getId(), node);
                addOrientedEdge((i-1) * nodesInLayer + j, i * nodesInLayer + j);
            }
            for (Node originalNode: graph) { // add edges
                for (Node succesor: originalNode.getSuccessors()) {
                    addOrientedEdge((i-1) * nodesInLayer + originalNode.getId(),i * nodesInLayer + succesor.getId());
                }
            }
        }
    }
    

    
    @Override
    public void addGoals(int teamID, int[] indices) {
        
        for (int i = 0; i < layerCount; i++) {
            for (int j = 0; j < indices.length; j++) {
                int idx = i * nodesInLayer + indices[j];
                try {
                    findNodeById(idx).setGoalTeamID(teamID);
                } catch (NullPointerException e) {
                    System.err.println("Null pointer on index: " + idx);
                }
            }
        }
    }
    
    //---------private methods --------------
    
        
    private void addOrientedEdge(Node from, Node to) {
           from.getSuccessors().add(to);     
    }

    private void addOrientedEdge(Integer fromId, Integer toId) {
        Node from = findNodeById(fromId);
        Node to = findNodeById(toId);
        
        if (fromId == null || toId == null) {
            throw new RuntimeException("addUnorientedEdgeById -- nonexisting nodeId");
        }
        addOrientedEdge(from, to);  
    }
}
