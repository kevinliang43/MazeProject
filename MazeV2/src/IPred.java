/**
 * Created by KevinLiang on 5/17/17.
 */

/**
 * Predicate Interface.
 *
 * @param <T> What this predicate is generic over.
 */
public interface IPred<T> {

  /**
   * Applies this predicate to a given item.
   * Checks to see whether or not the given item passes the conditions set by this predicate.
   *
   * @param t The Item to be checked.
   * @return A boolean representing whether or not the given item passed the conditions set by this
   * Predicate.
   */
  boolean apply(T t);
}
