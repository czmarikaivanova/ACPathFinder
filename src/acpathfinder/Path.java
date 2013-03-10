// Path.java

package acpathfinder;

import acpathfinder.graph.Node;
import java.util.LinkedList;

/**
 *
 * @author Marika Ivanova
 * A path for one entity from start to destination node.
 * Instead whole nodes we store only it's IDs
 */
public class Path extends LinkedList<Node> {

    // id of entity
    private int id;

    public Path(int id) {
        this.id = id;
    }

    /**
     *  get ID of the entity which the path belongs to
     */
    public int getId() {
        return id;
    }

    
}
