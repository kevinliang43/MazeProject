/**
 * Created by KevinLiang on 5/17/17.
 */

import java.awt.*;

import javalib.impworld.WorldScene;
import javalib.worldimages.OutlineMode;
import javalib.worldimages.RectangleImage;

/**
 * Represents a player playing the game.
 */
public class Player {
  // coordinates of the player
  int row;
  int col;
  int nCell; // Number of the cell that the player is on.

  Player(int row, int col, int nCell) {
    this.row = row;
    this.col = col;
    this.nCell = nCell;
  }

  /**
   * function to move the player.
   *
   * @param cell represents the cell in which this player is trying to move to.
   * @param key represents the direction of movement of the player
   * @return this player with the location of the player updated.
   */
  public Player movePlayer(Node cell, String key) {
    if (key.equals("left") && !cell.right) {
      this.col = col - 1;
      cell.traveled = true;
    }
    if (key.equals("right") && !cell.left) {
      this.col = col + 1;
      cell.traveled = true;
    }
    if (key.equals("up") && !cell.down) {
      this.row = row - 1;
      cell.traveled = true;
    }
    if (key.equals("down") && !cell.up) {
      this.row = row + 1;
      cell.traveled = true;
    }
    return this;
  }

  /**
   * Rendering function for the player.
   *
   * @param ws World Scene in which the player is to be drawn on.
   * @return World Scene with the player drawn.
   */
  public WorldScene drawPlayer(WorldScene ws) {
    ws.placeImageXY(new RectangleImage(Maze.CELL_SIZE, Maze.CELL_SIZE,
                    OutlineMode.SOLID, Color.CYAN),
            this.col * Maze.CELL_SIZE + Maze.CELL_SIZE / 2 + 1,
            this.row * Maze.CELL_SIZE + Maze.CELL_SIZE / 2 + 1);
    return ws;
  }
}