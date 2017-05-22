/**
 * Created by KevinLiang on 5/17/17.
 */


/**
 * Collection is a grouping of items. Used to represent either a stack or queue
 *
 * @param <T> represents the type of item that the collection is generic over.
 */
public interface ICollection<T> {

  /**
   * Add an item to this Collection.
   *
   * @param item the item to be added.
   */
  void add(T item);

  /**
   * Removes an item from the collection.
   *
   * @return the item removed.
   */
  T remove();

  /**
   * Is this Collection empty?
   *
   * @return boolean representing whether or not this Collection is empty.
   */
  boolean isEmpty();
}