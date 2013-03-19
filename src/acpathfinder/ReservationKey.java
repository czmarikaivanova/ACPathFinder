/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package acpathfinder;

import acpathfinder.graph.Node;

/**
 *
 * @author marion
 */
public class ReservationKey {
    Node node;
    int t;

    public ReservationKey(Node node, int t) {
            this.node = node;
            this.t = t;
        }    
}
