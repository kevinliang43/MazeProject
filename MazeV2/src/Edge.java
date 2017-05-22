/**
 * Created by KevinLiang on 5/17/17.
 */

/**
 * Edge class. Edge is created by two nodes.
 */
public class Edge implements Comparable<Edge> {
  // Nodes that constitute this edge.
  Node a;
  Node b;
  // weight of the edge. To be used for sorting.
  int weight;

  /**
   * Constructor for this Edge.
   * @param a Represents 1 node that constitute this edge.
   * @param b Represents the other node that constitute this edge.
   * @param weight the weight given to this edge.
   */
  Edge(Node a, Node b, int weight) {
    this.a = a;
    this.b = b;
    this.weight = weight;
  }

  /**
   * function to return a comparator for edge weights.
   * @param e other edge to be compared to
   * @return an integer representing whether this edge is less, the same, or greater than the given
   * edge, in terms of weight.
   */
  public int compareTo(Edge e) {
    return this.weight - e.weight;
  }
}
