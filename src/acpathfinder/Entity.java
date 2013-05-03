// Entity.java

package acpathfinder;

import acpathfinder.graph.Node;
import java.util.ArrayList;

/**
 *
 * @author Marika Ivanova
 */
public class Entity {

    private int agentID;
    private int teamID;
    private Node actualNode;
    private ArrayList<Node> targets;    
    
    public Entity(int agentID, int teamID, Node actualNode, Node targetNode) {
        this.agentID = agentID;
        this.teamID = teamID;
        this.actualNode = actualNode;
        targets = new ArrayList<Node>();
        targets.add(targetNode);
    }
    
    public Node getActualNode() {
        return actualNode;
    }

    // A move. It is not used in search. But it will be used in simulations.
    public void setActualNodeID(Node actualNode) {
        this.actualNode = actualNode;
    }

    public int getAgentID() {
        return agentID;
    }

    public void setAgentID(int agentID) {
        this.agentID = agentID;
    }

    public int getTeamID() {
        return teamID;
    }

    public void setTeamID(int teamID) {
        this.teamID = teamID;
    }


    
    // set a new goal - one node
    public void setTarget(Node targetNode) {
        targets.add(targetNode);
    }
    
    public Node getSingleTarget() {
        if ((targets != null) && (targets.size() > 0)) {
            return targets.get(0);
        }
        return null;
    }
    
    @Override
    public String toString() {
        return "AgentID: " + agentID + "\n TeamID: " +teamID + "\n Position: " + actualNode.getId() + "\n targetNode: " + getSingleTarget();
    }
}
