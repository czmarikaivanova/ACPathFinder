package acpathfinder;

/**
 *
 * @author Marika Ivanova
 */
import acpathfinder.graph.Graph;
import acpathfinder.graph.Node;
import java.io.File;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
 
public class XMLWriter {

    public void writeGraphToXML(Graph linkedGraph, ArrayList<Path> pathes, Properties prop) {       
      try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            //--------------------------------------------------------------
            //      First level elements
            //--------------------------------------------------------------

            // root element - graphrec
            
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("graphrec");
            rootElement.setAttribute("version", "1.0");
            doc.appendChild(rootElement);

            //--------------------------------------------------------------
            //      Second level elements
            //--------------------------------------------------------------
            
            // solution element
            Element solution = doc.createElement("solution");
            solution.setAttribute("id", "1");
            solution.setIdAttribute("id", true);
            rootElement.appendChild(solution);
            
            //--------------------------------------------------------------
            //      Third level elements
            //--------------------------------------------------------------

            // scene elements
            Element scene = doc.createElement("scene");
            scene.setAttribute("bg", "#ffffff");
            solution.appendChild(scene);

            // graph elements
            Element graph = doc.createElement("graph");
            solution.appendChild(graph);

            // scenario elements
            Element scenario = doc.createElement("scenario");
            scenario.setAttribute("validator", "Multirobot");
            solution.appendChild(scenario);

            //--------------------------------------------------------------
            //      Forth level elements
            //--------------------------------------------------------------
            
            // viewport element
            Element viewport = doc.createElement("viewport");
            viewport.setAttribute("x", "-1.18921");
            viewport.setAttribute("y", "-29.7302");
            scene.appendChild(viewport);

            // maxtrix element
            Element matrix = doc.createElement("matrix");
            matrix.setAttribute("m11", "1.68179");
            matrix.setAttribute("m12", "0");
            matrix.setAttribute("m21", "0");
            matrix.setAttribute("m22", "1.68179");
            matrix.setAttribute("dx", "0");
            matrix.setAttribute("dy", "0");
            scene.appendChild(matrix);
            
            
            //----------------
            // create node and entity and  elements
            
            for (Node lgNode : linkedGraph) {
                // new node elements
                Element node = doc.createElement("node");
                // set attribute to node element
                node.setAttribute("id", Integer.toString(lgNode.getId()));
                node.setIdAttribute("id", true);
                int goalTeamID = lgNode.getGoalTeamID();
                if (goalTeamID == ACPathFinder.NO_TEAM_ID) {
                        node.setAttribute("bg",prop.getProperty("nodeBg"));
                }
                else if (goalTeamID == ACPathFinder.TEAM1_ID) {
                        node.setAttribute("bg",prop.getProperty("team1_goal_bgcolor"));
                }
                else {
                        node.setAttribute("bg", prop.getProperty("team2_goal_bgcolor"));
                }
                node.setAttribute("fg", prop.getProperty("nodeFg"));
                node.setAttribute("bnd", "#000000");
                node.setAttribute("x", "0");
                node.setAttribute("y", "0");
                Entity entity = lgNode.getEntity();
                // there is an agent in the node
                if (entity != null) {
                    Element entityNode = doc.createElement("entity");
                    String entityIdString = Integer.toString(entity.getTeamID());
                    entityNode.setAttribute("id", entityIdString);
                    if (entity.getTeamID() == ACPathFinder.TEAM1_ID) {
                        entityNode.setAttribute("bg", prop.getProperty("team1_entity_bgcolor"));
                    }
                    else if (entity.getTeamID() == ACPathFinder.TEAM2_ID){
                        entityNode.setAttribute("bg", prop.getProperty("team2_entity_bgcolor"));
                    }
                    entityNode.setAttribute("bgf", "#005500");
                    entityNode.setAttribute("fg", "#ffffff");
                    entityNode.setAttribute("fgf", "#ffffff");
                    node.setAttribute("ent", entityIdString);
                    graph.appendChild(entityNode);
                }
                graph.appendChild(node);
            }
            
            // create edge elements
            for (Node lgNode : linkedGraph) {
                Set<Node> succesors = lgNode.getSuccessors();
                for (Node s: succesors) {

                    // new edge elements
                    Element edge = doc.createElement("edge");

                    // define 'from' and 'to' node
                    edge.setAttribute("n1", Integer.toString(lgNode.getId()));
                    edge.setAttribute("n2", Integer.toString(s.getId()));
                    edge.setAttribute("ln", "#000000");
                    edge.setAttribute("hgl", "#00ffff");
                    
                    graph.appendChild(edge);
                }
            }
            
            // create move elements
            // cycle over all pathes
            for (Path p: pathes) {
                for (int i = 0; i < p.size() - 1; i++) {
                    Element move = doc.createElement("move");
                    Node srcNode = p.get(i);
                    Node dstNode = p.get(i + 1);
                    move.setAttribute("tms", Integer.toString(i));
                    move.setAttribute("src", Integer.toString(srcNode.getId()));
                    move.setAttribute("dst", Integer.toString(dstNode.getId()));
                    scenario.appendChild(move);
                }
                
            }
            
            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("out.xml"));

            // Output to console for testing
             StreamResult result1 = new StreamResult(System.out);

            transformer.transform(source, result);

            System.out.println("File saved!");

      } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
      } catch (TransformerException tfe) {
            tfe.printStackTrace();
      }
    }
}