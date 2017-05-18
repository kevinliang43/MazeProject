/**
 * Created by KevinLiang on 5/17/17.
 */

// represents class node
public class DNode<T> extends ANode<T> {
  T data;


  DNode(T data) {
    this.data = data;
    this.next = null;
    this.prev = null;

  }

  DNode(T data, ANode<T> next, ANode<T> prev) {
    this.data = data;
    if (next == null || prev == null) {
      throw new IllegalArgumentException("You're not allowed to do this");
    }
    else {
      this.next = next;
      next.prev = this;
      this.prev = prev;
      prev.next = this;
    }
  }

  // findHelp returns a Anode
  ANode<T> findHelp(IPred<T> pred) {
    if (pred.apply(this.data)) {
      return this;
    }
    else {
      return this.next.findHelp(pred);
    }

  }

  // return this node as a node
  public DNode<T> asNode() {
    return this;
  }

  // Helper for contains
  boolean containsHelper(T item) {
    if (this.data.equals(item)) {
      return true;
    }
    else {
      return this.next.containsHelper(item);
    }
  }
}