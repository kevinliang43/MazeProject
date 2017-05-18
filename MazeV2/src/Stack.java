/**
 * Created by KevinLiang on 5/17/17.
 */




/**
 * My implementation of a Stack data structure.
 * @param <T> type of item which the stack is generic over.
 */
public class Stack<T> implements ICollection<T> {
  Deque<T> contents;  //contents of the stack

  Stack() {
    this.contents = new Deque<T>();
  }

  // adds an item to the head of the list
  public void add(T item) {
    contents.addAtHead(item);
  }

  // determines if the stack is empty
  public boolean isEmpty() {
    return contents.size() == 0;
  }

  // removes and returns the head of the list
  public T remove() {
    return contents.removeFromHead();
  }
}
