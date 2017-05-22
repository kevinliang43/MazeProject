

/**
 * Created by KevinLiang on 5/17/17.
 */


//queue: first in, first out
public class Queue<T>  implements ICollection<T> {
  Deque<T> contents; // contests of the queue

  Queue() {
    this.contents = new Deque<T>();
  }

  // adds an item to the tail of the list
  public void add(T item) {
    contents.addAtTail(item);
  }

  // determines if the queue is empty
  public boolean isEmpty() {
    return contents.size() == 0;
  }

  // removes and returns the head of the list
  public T remove() {
    return contents.removeFromHead();
  }
}