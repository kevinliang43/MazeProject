/**
 * Created by KevinLiang on 5/17/17.
 */

import tester.Tester;

/**
 * This class Runs the game.
 */
public class ExampleMaze {

  /**
   * Runs the game.
   * @param t Uses the tester library Tester class to run the game.
   */
  void testMaze(Tester t) {
    Maze m1 = new Maze();

    m1.bigBang(Maze.MAZE_WIDTH * Maze.CELL_SIZE,
            Maze.MAZE_HEIGHT * Maze.CELL_SIZE, .1);
  }


}
