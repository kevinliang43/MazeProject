/**
 * Created by KevinLiang on 5/17/17.
 */

/**
 * Class to represent a Deque data structure.
 *
 * @param <T> What this Deque is generic over.
 */
public class Deque<T> {
  Sentinel<T> header; // serves as the "beginning" of the deque.

  // Constructor
  Deque() {
    this.header = new Sentinel<T>();
  }

  // Convinience Constructor.
  Deque(Sentinel<T> header) {
    this.header = header;
  }

  /**
   * Returns the size of this deque.
   *
   * @return integer representing the number of items in this deque.
   */
  int size() {
    return header.countNodes(0);
  }

  /**
   * Add an item to the head of the deque.
   *
   * @param v item to be added
   */
  void addAtHead(T v) {
    header.addAtHead(v);
  }

  /**
   * Add an item to the tail of the deque.
   *
   * @param v item to be added.
   */
  void addAtTail(T v) {
    header.addAtTail(v);
  }

  /**
   * Removes the item at the head of the deque.
   *
   * @return the item removed.
   */
  T removeFromHead() {
    return header.removeFromHead();
  }

  /**
   * Removes the item at the tail of the deque.
   *
   * @return the item removed.
   */
  T removeFromTail() {
    return header.removeFromTail();
  }

  /**
   * Finds a node that matches the conditions of a given Predicate.
   *
   * @param pred represents the conditions that a Node must meet to be "found"
   * @return the Node found.
   */
  ANode<T> find(IPred<T> pred) {
    return this.header.find(pred);
  }

  /**
   * Given a node, searches the deque, and removes the node if found.
   *
   * @param n node to be removed.
   */
  void removeNode(ANode<T> n) {
    this.find(new IsTargetNode<T>(n)).removeThis();
  }

  /**
   * Checks to see if this deque contains a given item.
   *
   * @param item item to be found.
   * @return boolean representing whether or not the item has been found.
   */
  boolean contains(T item) {
    return this.header.contains(item);
  }
}