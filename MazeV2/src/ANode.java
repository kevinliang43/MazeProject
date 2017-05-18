/**
 * Created by KevinLiang on 5/17/17.
 */

// represents abstract class ANode
public abstract class ANode<T> {
  ANode<T> next;
  ANode<T> prev;


  // count the number of nodes in this Deque without Sentinel
  public int countNodes(int acc) {
    if (this.next.isSentinel()) {
      return acc;
    }
    else {
      return this.next.countNodes(1 + acc);
    }
  }

  // Helper for find method
  ANode<T> findHelp(IPred<T> pred) {
    return this;
  }

  // is this node a sentinel?
  public boolean isSentinel() {
    return false;
  }

  //EFFECT:
  //change this nodes next to the given ANode
  void changeNext(ANode<T> that) {
    this.next = that;
  }

  //EFFECT:
  //change this nodes next to the given ANode
  void changePrev(ANode<T> that) {
    this.prev = that;
  }

  //EFFECT:
  //remove this ANode from the list
  void removeThis() {
    this.next.changePrev(this.prev);
    this.prev.changeNext(this.next);
  }

  // return this as a Node
  DNode<T> asNode() {
    throw new RuntimeException("This isn't a Node");
  }

  // return this if it matches the given conditions, otherwise recurse
  abstract boolean containsHelper(T item);
}
