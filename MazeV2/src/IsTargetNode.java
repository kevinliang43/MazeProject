/**
 * Created by KevinLiang on 5/17/17.
 */

// isTargetnode
public class IsTargetNode<T> implements IPred<T> {
  T targetData;

  //checks if this is the node we are trying to remove
  IsTargetNode(ANode<T> n) {
    this.targetData = n.asNode().data;
  }

  //is the given T the same as the target Data
  public boolean apply(T t) {
    return t.equals(targetData);
  }
}