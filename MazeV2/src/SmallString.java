/**
 * Created by KevinLiang on 5/17/17.
 */

/**
 * Predicate to test if a String is less than length 5.
 */

public class SmallString implements IPred<String> {

  /**
   * Applies this predicate to a given String.
   * @param s String to be tested with.
   * @return boolean representing whether or not this string is of length less than 5.
   */
  public boolean apply(String s) {
    return s.length() < 5;
  }
}
