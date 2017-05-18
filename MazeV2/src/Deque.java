/**
 * Created by KevinLiang on 5/17/17.
 */

// represents class deque
public class Deque<T> {
  Sentinel<T> header;

  Deque() {
    this.header = new Sentinel<T>();
  }

  Deque(Sentinel<T> header) {
    this.header = header;
  }

  // returns the size of the deck not counting the header
  int size() {
    return header.countNodes(0);
  }

  // adds value T to the head of the Deque
  void addAtHead(T v) {
    header.addAtHead(v);
  }

  // adds value T to the tail of the Deque
  void addAtTail(T v) {
    header.addAtTail(v);
  }
  // removes the first node from this deque and throws runtime exception if empty
  T removeFromHead() {
    return header.removeFromHead();
  }

  // removes the last node from this deque and throws runtime exception if empty
  T removeFromTail() {
    return header.removeFromTail();
  }
  // find node
  ANode<T> find(IPred<T> pred) {
    return this.header.find(pred);
  }
  // remove node
  void removeNode(ANode<T> n) {
    this.find(new IsTargetNode<T>(n)).removeThis();
  }

  // determines if the deque contains T
  boolean contains(T item) {
    return this.header.contains(item);
  }
}