
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
        Graph lg1 = new Graph(12);
        lg1.normalise(4,3);
        int[] obstacles = {1, 4};
        lg1.addObstacles(obstacles);
        int[] obstacles2 = {};
        int[] teamIDs = {1};
        int[] vertexIDs = {0};
        int[] team1Goals = {6};
        ArrayList<Entity> entities = lg1.insertEntities(teamIDs,vertexIDs);
        lg1.addGoals(TEAM1_ID, team1Goals);
        lg1.addObstacles(obstacles2);
        //System.out.println(lg1.toString());

        ArrayList<Path> pathes = new ArrayList<Path>();
        int i = 0;
        for (Entity entity : entities) {
            DijkstraSearch ds = new DijkstraSearch(lg1);
            ds.setFoundPathes(pathes);
            ds.setSource(entity.getActualNode());
            ds.setTarget(lg1.findNodeById(team1Goals[i]));
            i++;
            Path path = ds.search();
            if (path != null) {
                pathes.add(ds.search());
            }
        }
        
        XMLWriter xw = new XMLWriter();
        xw.writeGraphToXML(lg1,pathes, prop);
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
