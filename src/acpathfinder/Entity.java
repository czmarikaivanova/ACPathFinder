/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package acpathfinder;

import acpathfinder.graph.Node;

/**
 *
 * @author Marika Ivanova
 */
public class Entity {

    private int agentID;
    private int teamID;
    private Node actualNode;

    public Node getActualNode() {
        return actualNode;
    }

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

    public Entity(int agentID, int teamID, Node actualNode) {
        this.agentID = agentID;
        this.teamID = teamID;
        this.actualNode = actualNode;
    }
    
    @Override
    public String toString() {
        return "AgentID: " + agentID + "; TeamID: " +teamID + "; Position: " + actualNode.getId();
    }
}
