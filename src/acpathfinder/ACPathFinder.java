
package acpathfinder;

import acpathfinder.graph.Graph;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ACPathFinder {
    
    // designation of agents in the nodes
    public static final int WHITE_ENTITY = 1; 
    public static final int BLACK_ENTITY = -1; 
    public static final int TEAM1_ID = 1;
    public static final int TEAM2_ID = 2;
    public static final int NO_TEAM_ID = 0;

    private static Properties prop;
    
    public static void main(String[] args) {
        // access to property file
        prop = new Properties();
        InputStream propIn;
        try {
            propIn = new FileInputStream("acpf.properties");
            prop.load(propIn);        
        } catch (IOException ex) {
            Logger.getLogger(ACPathFinder.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
       // XMLReader xmlReader = new XMLReader();
     //   LinkedGraph lg = xmlReader.createGraphFromXML();
        Graph graph = new Graph(12);
        graph.normalise(3,4);
        
        int[] obstacles = {4};
        graph.addObstacles(obstacles);
        SpatialGraph spatialGraph = new SpatialGraph(graph,5);
        
        int[] teamIDs = {1};
        int[] vertexIDs = {0};
        int[] team1Goals = {7};
        ArrayList<Entity> entities = spatialGraph.insertEntities(teamIDs,vertexIDs, team1Goals);
        //ArrayList<Entity> entities =   
        graph.insertEntities(teamIDs,vertexIDs, team1Goals);
        
        graph.addGoals(TEAM1_ID, team1Goals);
        spatialGraph.addGoals(TEAM1_ID, team1Goals);

        // simulation of 3rd dimension in search. The agents make a plan and all others
        // must take it into account.
        // TODO: perhaps it would be better store entity as a value,insted of just
        ArrayList<Path> pathes = new ArrayList<Path>();
        DijkstraSearch ds = new DijkstraSearch(spatialGraph);
        //DijkstraSearch ds = new DijkstraSearch(graph);
        int i = 0;
        for (Entity entity : entities) {
            ds.setFoundPathes(pathes);
            ds.setSource(entity.getActualNode());
            ds.setTarget(entity.getSingleTarget());
            i++;
            Path path = ds.search();
            if (path != null) {
                // rearange path for original graph
                // if it was already path in original graph, it will not be changed.
                // otherwise the path made in spatial graph will be adjusted to original.
                path = graph.rearrangePath(path); 
                pathes.add(path);
            }
        }
        
        XMLWriter xw = new XMLWriter();
        //xw.writeGraphToXML(spatialGraph,pathes, prop);
        xw.writeGraphToXML(graph,pathes, prop);
    }
    
   
    public void printResult(ArrayList<Path> pathes) {
//        for ()
//        System.out.println("Source ID: " + source.getId());
//        System.out.println("Target ID: " + target.getId());
//        for(LinkedGraphNode pn: path) {
//            System.out.println(pn.getId() + "->");
//        }
    }    

    

    
}
