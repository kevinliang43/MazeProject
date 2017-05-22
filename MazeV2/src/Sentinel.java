/**
 * Created by KevinLiang on 5/17/17.
 */

//represensts class sentinel
public class Sentinel<T> extends ANode<T> {

  Sentinel() {
    this.next = this;
    this.prev = this;
  }


  // is this node a sentinel?
  public boolean isSentinel() {
    return true;
  }

  // adds value T to the head of the Deque
  void addAtHead(T v) {

    DNode<T> n = new DNode<T>(v, this.next, this);
    n.next.changePrev(n);
    this.changeNext(n);
  }

  // adds value T to the tail of the Deque
  void addAtTail(T v) {
    DNode<T> n = new DNode<T>(v, this, this.prev);
    n.prev.changeNext(n);
    this.changePrev(n);

  }


  // find the first node that satisfies the given IPred
  ANode<T> find(IPred<T> pred) {
    if (this.next.isSentinel()) {
      return this;
    }
    else {
      return this.next.findHelp(pred);
    }
  }

  // Helper for find
  ANode<T> findHelp(IPred<T> pred) {
    return this;
  }

  // remove this from the list
  void removeThis() {
    //sentinels cannot be removed
  }

  // removes the first node from this deck and throws runtime exception if empty
  T removeFromHead() {
    if (this.next.isSentinel()) {
      throw new RuntimeException("Empty");
    }
    else {
      T rem = this.next.asNode().data;
      this.next.removeThis();
      return rem;

    }
  }
  //removes the first node from this deck and throws runtime exception if empty
  T removeFromTail() {
    if (this.prev.isSentinel()) {
      throw new RuntimeException("Empty");
    }
    else {
      T rem = this.prev.asNode().data;
      this.prev.removeThis();
      return rem;
    }
  }

  // determines if the first node is the item
  boolean contains(T item) {
    return this.next.containsHelper(item);
  }

  // helper for contains
  boolean containsHelper(T item) {
    return false;
  }
}