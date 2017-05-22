/**
 * Created by KevinLiang on 5/17/17.
 */

/**
 * Represents an Abstract Node Class
 *
 * @param <T> what the class is generic over.
 */
public abstract class ANode<T> {
  ANode<T> next;
  ANode<T> prev;


  /**
   * count the number of nodes in this Deque, excluding the Sentinel
   *
   * @param acc accumulator to keep track of the number of nodes.
   * @return integer representing the number of nodes in the Deque.
   */
  public int countNodes(int acc) {
    if (this.next.isSentinel()) {
      return acc;
    } else {
      return this.next.countNodes(1 + acc);
    }
  }

  /**
   * Helper method for the find function
   *
   * @param pred the predicate determining whether or not this is Node has correct values
   * @return Default, returns this node.
   */
  ANode<T> findHelp(IPred<T> pred) {
    return this;
  }

  /**
   * is this Node a Sentinel?
   *
   * @return Boolean. Deafult, this node is not a sentinel.
   */
  public boolean isSentinel() {
    return false;
  }

  /**
   * Changes this node's next, to a given node.
   *
   * @param that represents the new 'next' node.
   */
  void changeNext(ANode<T> that) {
    this.next = that;
  }

  /**
   * Changes this node's previous node to a given node.
   *
   * @param that represents this node's new Previous node.
   */
  void changePrev(ANode<T> that) {
    this.prev = that;
  }

  /**
   * Removes this node from the Deque.
   */
  void removeThis() {
    this.next.changePrev(this.prev);
    this.prev.changeNext(this.next);
  }

  /**
   * Return this as a Node.
   *
   * @return Defaults as "this isn't a node"
   */
  DNode<T> asNode() {
    throw new RuntimeException("This isn't a Node");
  }

  /**
   * Returns this Node, if given conditions are met.
   *
   * @param item item to compare to this.
   * @return boolean representing whether or not conditions are met.
   */
  abstract boolean containsHelper(T item);
}
