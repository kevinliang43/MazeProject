/**
 * Created by KevinLiang on 5/17/17.
 */

import java.awt.*;

import javalib.impworld.World;
import javalib.impworld.WorldScene;
import javalib.worldimages.LineImage;
import javalib.worldimages.OutlineMode;
import javalib.worldimages.Posn;
import javalib.worldimages.RectangleImage;

/**
 * Node Class. Represents each cell of the maze.
 */
public class Node extends DNode<Integer> {
  int row; // row of the node
  int col; // column of the node
  int nCell; // number of node from left to right, top to bottom

  // booleans representing if neighboring walls exist
  boolean left;
  boolean right;
  boolean up;
  boolean down;

  // boolean representing if this node is part of the final path taken.
  boolean partOfFinalPath;
  // boolean representing if this node is part of the path taken by the player.
  boolean traveled;

  // constructor for node class
  Node(int row, int col, int nCell, boolean left, boolean right, boolean up,
       boolean down) {
    super(nCell);
    this.row = row;
    this.col = col;
    this.nCell = nCell;
    this.left = left;
    this.right = right;
    this.up = up;
    this.down = down;
    partOfFinalPath = false;
    traveled = false;
  }

  /**
   * Draws the cell grid lines
   * @param ws Scene to be drawn on.
   * @return WorldScene with grid lines drawn.
   */
  WorldScene drawLines(WorldScene ws) {
    if (this.left) {
      ws.placeImageXY(new LineImage(new Posn(0, Maze.CELL_SIZE),
                      Color.BLACK), this.col * Maze.CELL_SIZE,
              this.row * Maze.CELL_SIZE + Maze.CELL_SIZE / 2);
    }
    if (this.up) {
      ws.placeImageXY(new LineImage(new Posn(Maze.CELL_SIZE, 0),
                      Color.BLACK), this.col *
                      Maze.CELL_SIZE + Maze.CELL_SIZE / 2,
              this.row * Maze.CELL_SIZE);
    }
    return ws;
  }

  // determines color of cell when visited for animating the search
  // final path color is dark grey until entire path has been animated

  /**
   * Determines the color of the cell when visited by BFS or DFS while search is in progress
   * Final path color will change color once search has been completed.
   * @param t time representing whether or not the game has started.
   * @return A color depending on whether or not the end cell has been found.
   */
  Color visitCol(int t) {
    if (this.partOfFinalPath && t > -1) {
      return Color.lightGray;
    } else {
      return Color.darkGray;
    }
  }

  /**
   * Cell Rendering function
   * @param col Color in which the cell is to be colored.
   * @param ws World Scene in which the cells are rendered on top of.
   */
  void drawCell(Color col, WorldScene ws) {
    ws.placeImageXY(new RectangleImage(Maze.CELL_SIZE, Maze.CELL_SIZE,
                    OutlineMode.SOLID, col),
            this.col * Maze.CELL_SIZE + Maze.CELL_SIZE / 2 + 1,
            this.row * Maze.CELL_SIZE + Maze.CELL_SIZE / 2 + 1);
  }
}