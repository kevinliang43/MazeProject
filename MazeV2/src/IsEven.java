/**
 * Created by KevinLiang on 5/17/17.
 */

/**
 * Predicate class for checking for Even integers.
 */
public class IsEven implements IPred<Integer> {
  /**
   * Is the given Integer even?
   *
   * @param n Integer to be checked.
   * @return boolean representing whether or not the given integer is even.
   */
  public boolean apply(Integer n) {
    return n % 2 == 0;
  }
}