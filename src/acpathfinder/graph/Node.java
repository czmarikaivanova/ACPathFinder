
package acpathfinder.graph;

import acpathfinder.Entity;
import java.util.Set;
import java.util.TreeSet;


 /**
 * Graph Node
 * @author Marika Ivanova
 */
public class Node implements Comparable<Node> {
    private int NO_TEAM_GOAL = -1;
    
    private boolean reserved; // TODO: do we need it?
    
    // Comparable is for Set<Node>
    private int id;
    private Set<Node> successors;    
    private Entity entity;
    private int goalTeamID;

    // makespan size depends on the numer of nodes in the graph. 
    // so this value is just temporary, for testing.

    /**
     * Constructor
     * @param id  ID of the node
     */
    public Node(int id) {
        this.id = id;
        entity = null;
        successors = new TreeSet<Node>();
    }

    /**
     * @return the ID of the node
     */
    public int getId() {
        return id;
    }
    
    // set a new node ID.
    void setId(int i) {
        this.id = i;
    }
    /**
     * @return the successors
     */
    public Set<Node> getSuccessors() {
        return successors;
    }


    /**
     * @return the entity
     */
    public Entity getEntity() {
        return entity;
    }

    public int getGoalTeamID() {
        return goalTeamID;
    }    
    
    public boolean isReserved() {
        return reserved;
    }
    
    /**
     * @param entity the entity to set
     */
    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public void setGoalTeamID(int goalTeamID) {
        this.goalTeamID = goalTeamID;
    }
    
    public void reserve() {
        reserved = true;
    }
    
    public void release() {
        reserved = false;
    }
    
    @Override
    public String toString() {
        return Integer.toString(getId());
        //return "GraphNode >> id: " + getId() + ", Value: " + (value != null ? value.toString() : "null");
    }

    @Override
    public int compareTo(Node t) {
        return (id - t.id); // for Comparable interface
    }
    
    public boolean isGoal() {
        return goalTeamID != NO_TEAM_GOAL;
    }



}
