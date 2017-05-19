/**
 * Created by KevinLiang on 5/17/17.
 */

/**
 * Predicate to check for the Target Node.
 *
 * @param <T> What this class is generic over. Defines what type of data this node will hold.
 */
public class IsTargetNode<T> implements IPred<T> {
  T targetData;

  /**
   * Constructor for the IsTargetNode class.
   *
   * @param n the node that is being checked.
   */
  IsTargetNode(ANode<T> n) {
    this.targetData = n.asNode().data;
  }

  /**
   * Checks to see if this Node is the same as the given.
   *
   * @param t The Item to be checked.
   * @return boolean representing whether or not this is the target node.
   */
  public boolean apply(T t) {
    return t.equals(targetData);
  }
}