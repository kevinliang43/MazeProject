/**
 * Created by KevinLiang on 5/17/17.
 */

/**
 * Represents the Actual node representing a point in the maze.
 *
 * @param <T> What this node is generic over.
 */
public class DNode<T> extends ANode<T> {
  T data; // data that this node holds.


  /**
   * Basic Constructor
   *
   * @param data representing the data that this node will hold.
   */
  DNode(T data) {
    this.data = data;
    this.next = null;
    this.prev = null;

  }

  /**
   * Convenience Constructor. Constructs a node, and mutates the next and prev nodes such that
   * the nodes are all linked.
   *
   * @param data Represents the data that this node will hold.
   * @param next Represents the next node in sequence.
   * @param prev Represents the previous node in sequence.
   */
  DNode(T data, ANode<T> next, ANode<T> prev) {
    this.data = data;
    if (next == null || prev == null) {
      throw new IllegalArgumentException("You're not allowed to do this");
    } else {
      this.next = next;
      next.prev = this;
      this.prev = prev;
      prev.next = this;
    }
  }

  /**
   * Helper function for the find method.
   * If this node does not match the conditions, it will pass on the predicate to the next node.
   *
   * @param pred the predicate determining whether or not this is Node has correct values
   * @return A node that is being searched for.
   */
  ANode<T> findHelp(IPred<T> pred) {
    if (pred.apply(this.data)) {
      return this;
    } else {
      return this.next.findHelp(pred);
    }

  }

  /**
   * Returns this as a node.
   *
   * @return this as a node.
   */
  public DNode<T> asNode() {
    return this;
  }

  /**
   * Helper function for the contains method.
   * If this node does not contain the given data, it will pass the call onto the next Node.
   *
   * @param item item to compare to this.
   * @return boolean representing whether or not a Node with the given data is found.
   */
  boolean containsHelper(T item) {
    if (this.data.equals(item)) {
      return true;
    } else {
      return this.next.containsHelper(item);
    }
  }
}