/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package acpathfinder.graph;

/**
 *
 * @author Marika Ivanova
 */
public class Edge {
    
    private Node target;
    private double weight;
    public Edge(Node target, double weight) { 
        this.target = target; 
        this.weight = weight; 
    }    
}
