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

  /**
   * Add an item to the top of this Stack.
   * @param item the item to be added.
   */
  public void add(T item) {
    contents.addAtHead(item);
  }

  /**
   * Is this Stack Empty or not?
   * @return a boolean representing whether this stack is empty.
   */
  public boolean isEmpty() {
    return contents.size() == 0;
  }

  /**
   * removes the item from the top of the stack.
   * @return the item removed.
   */
  public T remove() {
    return contents.removeFromHead();
  }
}
