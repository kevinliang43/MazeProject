/**
 * Created by KevinLiang on 5/17/17.
 */


/**
 * Collection is a grouping of items. Used to represent either a stack or queue
 * @param <T> represents the type of item that the collection is generic over.
 */
public interface ICollection<T> {
  void add(T item);
  T remove();
  boolean isEmpty();
}