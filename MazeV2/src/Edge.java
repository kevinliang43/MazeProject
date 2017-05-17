/**
 * Created by KevinLiang on 5/17/17.
 */

/**
 * Edge class. Edge is created by two nodes.
 */
public class Edge implements Comparable<Edge> {
  // represent nodes that constitute an edge.
  Node a;
  Node b;
  // weight of the edge
  int weight;

  // constructor for the edge class.
  Edge(Node a, Node b, int weight) {
    this.a = a;
    this.b = b;
    this.weight = weight;
  }

  // comparator for Collections.sort()

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
