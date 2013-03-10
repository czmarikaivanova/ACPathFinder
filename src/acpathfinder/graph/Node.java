
package acpathfinder.graph;

import acpathfinder.Entity;
import java.util.Set;
import java.util.TreeSet;


 /**
 * Graph Node
 * @author Marika Ivanova
 */
public class Node implements Comparable<Node> {
    // Comparable is for Set<Node>

    private int id;
    private Set<Node> successors;
    private Entity entity;
    private int goalTeamID;


    /**
     * Constructor
     * @param id  ID of the node
     */
    public Node(int id) {
        this.id = id;
        this.entity = null;
        this.successors = new TreeSet<Node>();
    }

    /**
     * @return the ID of the node
     */
    public int getId() {
        return id;
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
    
    /**
     * @param entity the entity to set
     */
    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public void setGoalTeamID(int goalTeamID) {
        this.goalTeamID = goalTeamID;
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

}
