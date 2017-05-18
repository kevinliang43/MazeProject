/**
 * Created by KevinLiang on 5/17/17.
 */

//is this given integer even value?
public class IsEven implements IPred<Integer> {
  //is the given integer even value?
  public boolean apply(Integer n) {
    return n % 2 == 0;
  }
}