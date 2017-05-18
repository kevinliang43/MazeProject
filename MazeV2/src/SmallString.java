/**
 * Created by KevinLiang on 5/17/17.
 */

// is the length of this string smaller than 5?

public class SmallString implements IPred<String> {
  public boolean apply(String s) {
    return s.length() < 5;
  }
}
