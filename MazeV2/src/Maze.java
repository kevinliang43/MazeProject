/**
 * Created by KevinLiang on 5/17/17.
 */

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

import javalib.impworld.World;
import javalib.impworld.WorldScene;
import javalib.worldimages.LineImage;
import javalib.worldimages.OutlineMode;
import javalib.worldimages.Posn;
import javalib.worldimages.RectangleImage;
import javalib.worldimages.TextImage;


// Represents the conceptual Maze

public class Maze extends World {

  // Constants: use to change game size
  // Maze width (up to 100 cells wide)
  static int MAZE_WIDTH = 10;
  // Maze height (up to 60 cells long)
  static int MAZE_HEIGHT = 10;
  // defines the cell dimensions (in pixels). Auto-calibrates.
  static final int CELL_SIZE =
          Math.min(900 / Maze.MAZE_HEIGHT, 1500 / Maze.MAZE_WIDTH);

  //hashmap to represent representatives of each node
  HashMap<Node, Node> representatives;
  //represent the edges in the final hashmap
  ArrayList<Edge> edgesInTree;
  //worklist of all edges available in the maze, sorted by edge weights
  ArrayList<Edge> worklist;

  int row; // number of rows in maze
  int col; // number of columns in maze
  int nCells; // number of cells in maze

  ArrayList<ArrayList<Node>> maze; // represent the maze as a 2d array
  ArrayList<Node> mazeList; // represent the maze as a single list
  Player p1; // represent a player manually traversing the maze
  HashMap<Node, Node> cameFromEdge;
  ArrayList<Node> shortestPath; // represent the shortest path to finish maze
  ArrayList<Node> visited; // represents all visited nodes

  int time; // represents time elapsed
  // toggle for showing player's path
  // 0 = toggle off
  // 1 = dfs
  // 2 = bfs
  boolean togglePath;
  // if true, animate the selected path
  int toggleSearch;
  // length of the bfs search
  int bfsLength;
  // length of the dfs search
  int dfsLength;

  /**
   * Constructor for the Maze class.
   */
  Maze() {
    this.row = MAZE_HEIGHT;
    this.col = MAZE_WIDTH;
    this.nCells = MAZE_WIDTH * MAZE_HEIGHT;

    this.initMaze();
    this.initRep();
    this.initWorkList();
    this.initMazeList();
    this.kruskalAlgo();
    this.makePath();
    cameFromEdge = new HashMap<Node, Node>();
    p1 = new Player(0, 0, 0);
    shortestPath = new ArrayList<Node>();
    visited = new ArrayList<Node>();
    this.time = 0;
    togglePath = false;
    toggleSearch = 0;
    this.bfs(this.maze.get(0).get(0),
            this.maze.get(Maze.MAZE_HEIGHT - 1).get(
                    Maze.MAZE_WIDTH - 1));
    bfsLength = this.visited.size();
    this.constructShortestPath();
    this.dfs(this.maze.get(0).get(0),
            this.maze.get(Maze.MAZE_HEIGHT - 1).get(
                    Maze.MAZE_WIDTH - 1));
    dfsLength = this.visited.size();
  }

  Maze(int height, int width) {

    if (height > 60 || height <= 0) {
      throw new IllegalArgumentException("Maze must have a height greater than 0 and less than " +
              "or equal to 60 units. ");
    }

    if (width > 100 || width <= 0) {
      throw new IllegalArgumentException("Maze must have a width greater than 0 and less than" +
              "or equal to 100 units. ");
    }

    this.MAZE_HEIGHT = height;
    this.MAZE_WIDTH = width;

    this.row = MAZE_HEIGHT;
    this.col = MAZE_WIDTH;
    this.nCells = MAZE_WIDTH * MAZE_HEIGHT;

    this.initMaze();
    this.initRep();
    this.initWorkList();
    this.initMazeList();
    this.kruskalAlgo();
    this.makePath();
    cameFromEdge = new HashMap<Node, Node>();
    p1 = new Player(0, 0, 0);
    shortestPath = new ArrayList<Node>();
    visited = new ArrayList<Node>();
    this.time = 0;
    togglePath = false;
    toggleSearch = 0;
    this.bfs(this.maze.get(0).get(0),
            this.maze.get(Maze.MAZE_HEIGHT - 1).get(
                    Maze.MAZE_WIDTH - 1));
    bfsLength = this.visited.size();
    this.constructShortestPath();
    this.dfs(this.maze.get(0).get(0),
            this.maze.get(Maze.MAZE_HEIGHT - 1).get(
                    Maze.MAZE_WIDTH - 1));
    dfsLength = this.visited.size();
  }

  /**
   * Changes the Maze game World depending on the Key Stroke given.
   * Pressing "r" creates a randomized maze.
   * Pressing "b" initiates breadth-first search on the generated maze.
   * Pressing "d" initiates depth-first search on the generated maze.
   *
   * Player mode:
   * Pressing "left arrow" moves the player left one space, if no walls are blocking the player.
   * Pressing "right arrow" moves the player right one space, if no walls are blocking the player.
   * Pressing "down arrow" moves the player down one space, if no walls are blocking the player.
   * Pressing "up arrow" moves the player up one space, if no walls are blocking the player.
   * Pressing "t" toggles the vizualization of the path that the player has already taken.
   *
   * @param key represents the key pressed in String form.
   */
  public void onKeyEvent(String key) {
    // pressing "r" creates a new random maze
    if (key.equals("r")) {
      this.initMaze();
      this.initRep();
      this.initWorkList();
      this.initMazeList();
      this.kruskalAlgo();
      this.makePath();
      this.makeScene();
      this.shortestPath = new ArrayList<Node>();
      this.p1 = new Player(0, 0, 0);
      this.time = 0;
      toggleSearch = 0;
      this.bfs(this.maze.get(0).get(0),
              this.maze.get(Maze.MAZE_HEIGHT - 1).get(
                      Maze.MAZE_WIDTH - 1));
      bfsLength = this.visited.size();
      this.constructShortestPath();
      this.dfs(this.maze.get(0).get(0),
              this.maze.get(Maze.MAZE_HEIGHT - 1).get(
                      Maze.MAZE_WIDTH - 1));
      dfsLength = this.visited.size();
    }
    // pressing "b" initiates breadth-first search onto a generated maze
    if (key.equals("b")) {
      this.time = 0;
      this.bfs(this.maze.get(0).get(0),
              this.maze.get(MAZE_HEIGHT - 1).get(MAZE_WIDTH - 1));
      toggleSearch = 2;
    }
    // use depth-first search to find the path
    if (key.equals("d")) {
      this.time = 0;
      this.dfs(this.maze.get(0).get(0),
              this.maze.get(MAZE_HEIGHT - 1).get(MAZE_WIDTH - 1));
      toggleSearch = 1;
    }

    // move the player according to key
    if (key.equals("left") && p1.col - 1 >= 0) {
      p1.movePlayer(maze.get(p1.row).get(p1.col - 1), key);
    }
    if (key.equals("right") && p1.col + 1 < Maze.MAZE_WIDTH) {
      p1.movePlayer(maze.get(p1.row).get(p1.col + 1), key);
    }
    if (key.equals("up") && p1.row - 1 >= 0) {
      p1.movePlayer(maze.get(p1.row - 1).get(p1.col), key);
    }
    if (key.equals("down") && p1.row + 1 < Maze.MAZE_HEIGHT) {
      p1.movePlayer(maze.get(p1.row + 1).get(p1.col), key);
    }
    if (key.equals("t")) {
      togglePath = !togglePath;
    }
  }


  /**
   * Function to initialize the Representatives Hashmap. All nodes will be initialized with themself
   * as the representative.
   *
   * @return a Hashmap with each node as their own representative.
   */
  HashMap<Node, Node> initRep() {
    representatives = new HashMap<Node, Node>();
    for (int i = 0; i < row; i++) {
      for (int j = 0; j < col; j++) {
        representatives.put(maze.get(i).get(j), maze.get(i).get(j));
      }
    }
    return representatives;
  }

  /**
   * Creates and populates a maze with nodes.
   *
   * @return an ArrayList of ArrayList of nodes, representing the maze.
   */
  ArrayList<ArrayList<Node>> initMaze() {
    maze = new ArrayList<ArrayList<Node>>();
    for (int i = 0; i < this.row; i++) {
      maze.add(new ArrayList<Node>());
      for (int j = 0; j < this.col; j++) {
        maze.get(i).add(new Node(i, j, (i * col) + j,
                true, true, true, true));
      }
    }
    return maze;
  }


  /**
   * initializes a list of all nodes within the maze.
   *
   * @return Arraylist of all nodes in the current maze.
   */
  ArrayList<Node> initMazeList() {
    mazeList = new ArrayList<Node>();
    for (int i = 0; i < row; i++) {
      for (int j = 0; j < col; j++) {
        mazeList.add(maze.get(i).get(j));
      }
    }
    return mazeList;
  }

  // initialize the worklist of all possible edges. Set random weights to
  // each edge

  /**
   * Initializes a worklist of all possible edges and assigns a random weight to each edge.
   * Then sorts the worklist according to weight.
   *
   * @return Arraylist of Edges with randomly assigned weights.
   */
  ArrayList<Edge> initWorkList() {
    worklist = new ArrayList<Edge>();
    Random rand = new Random();

    for (int i = 0; i < this.row; i++) {
      for (int j = 0; j < this.col; j++) {
        if (i != this.row - 1) {
          worklist.add(new Edge(maze.get(i).get(j),
                  maze.get(i + 1).get(j), rand.nextInt(nCells)));
        }
        if (j != this.col - 1) {
          worklist.add(new Edge(maze.get(i).get(j),
                  maze.get(i).get(j + 1), rand.nextInt(nCells)));
        }
      }
    }

    // sort the worklist in ascending order
    Collections.sort(worklist);
    return worklist;
  }

  // return completed spanning tree (list of edges) using kruskal's algorithm

  /**
   * Using Kruskal's Algorithm, creates a completed minimum spanning tree using the edges.
   * 1. all edges start off as its own "tree"
   * 2. edges are connected one by one, building bigger "trees"
   * 3. Process ends when edges are all connected under one tree.
   *
   * Method works by using the list of edges sorted by weight:
   *
   * 1. Goes through the list of edges, and picks out the first of the list.
   * 2. Checks the edge's nodes to see if their representatives equal each other. If it does, this
   * creates a cycle in the maze, which means the edge cannot be used and is removed from the
   * work list.
   * 3. If the representatives do not equal each other, then the representative is set to each other
   * and the edge is added to the tree.
   * 4. Cycle continues until maze paths are complete.
   *
   * @return an Arraylist of edges with updated representatives.
   */
  ArrayList<Edge> kruskalAlgo() {
    edgesInTree = new ArrayList<Edge>();
    // create minimum spanning tree
    while ((edgesInTree.size() < mazeList.size() - 1)) {
      // find shortest edge (first in list)
      Edge first = this.worklist.get(0);
      // does this edge create a cycle? (union/find)
      if (this.find(representatives, first.a) ==
              (this.find(representatives, first.b))) {
        this.worklist.remove(first);
      } else {
        edgesInTree.add(first);
        union(representatives, find(representatives, first.a),
                find(representatives, first.b));
        union(representatives, find(representatives, first.b),
                find(representatives, first.a));
        this.worklist.remove(first);
      }
    }
    return edgesInTree;
  }

  /**
   * Find function used go through a node's representatives recursively.
   *
   * If a node's representative is itself, then it is itself's representative.
   * Otherwise, it will continue going through each node until it finds a node with itself as
   * its representative.
   *
   * @param hm represents the hashmap of nodes used to map each node to its representative.
   * @param key the starting node
   * @return the end node representing the beginning node's representative.
   */
  Node find(HashMap<Node, Node> hm, Node key) {
    Node temp = key;
    if (hm.get(key) == (key)) {
      return temp;
    } else {
      temp = hm.get(key);
      return this.find(hm, temp);
    }
  }

  // links a representative to another representative

  /**
   *
   * Sets a given node as the representative to another node.
   *
   * @param hm the hashmap with all nodes.
   * @param link the node whose representative is being used.
   * @param n the node whose representative is being set to the link's representative.
   * @return
   */
  HashMap<Node, Node> union(HashMap<Node, Node> hm, Node link, Node n) {
    hm.put(n, link);
    return hm;
  }

  /**
   * Determines walls for each node.
   * Mutates nodes within this maze game so that each node has its correct walls
   */
  void makePath() {
    for (Edge e : this.edgesInTree) {
      if (e.b.col - e.a.col == 1) {
        e.b.left = false;
        e.a.right = false;
      }

      if (e.b.row - e.a.row == 1) {
        e.b.up = false;
        e.a.down = false;
      }
    }
  }

  /**
   * Render function to draw the world.
   *
   * @return a WorldScene representing the graphic view of the current maze game state.
   */
  public WorldScene makeScene() {
    WorldScene ws =
            new WorldScene(row * CELL_SIZE, col * CELL_SIZE);
    this.drawBG(ws);
    this.drawStartEnd(ws);

    if (togglePath) {
      this.drawTraveled(ws);
    }

    this.drawVisited(ws);

    p1.drawPlayer(ws);

    for (Node n : mazeList) {
      n.drawLines(ws);
    }
    this.drawWall(ws);

    this.winScene(ws);

    return ws;
  }

  /**
   * Draws the walls of this maze on top of a given WorldScene.
   *
   * @param ws WorldScene to be drawn upon.
   */
  void drawWall(WorldScene ws) {

    ws.placeImageXY(new LineImage(new Posn(0, this.row * CELL_SIZE),
                    Color.BLACK), this.col * CELL_SIZE,
            this.row * CELL_SIZE / 2);
    ws.placeImageXY(new LineImage(new Posn(this.col * CELL_SIZE, 0),
                    Color.BLACK), this.col * CELL_SIZE / 2,
            this.row * CELL_SIZE);
  }

  /**
   * Breadth First Search. Uses a Queue to organize the nodes to be searched through.
   *
   * @param from represents the node from which to start.
   * @param to represents the end node in which the search ends.
   * @return boolean representing if the node has been found.
   */
  boolean bfs(Node from, Node to) {
    return searchHelp(from, to, new Queue<Node>());
  }


  /**
   * Depth First Search. Uses a Stack to organize the nodes to be searched through.
   *
   * @param from represents the node from which to start.
   * @param to represents the end node in which the search ends.
   * @return boolean representing if the node has been found.
   */
  boolean dfs(Node from, Node to) {
    return searchHelp(from, to, new Stack<Node>());
  }

  /**
   * Helper function for the search methods.
   * @param from represents the node where the search starts from.
   * @param to represents the node where the search ends.
   * @param worklist Organization of nodes to be searched through. Queue is for BFS, Stack is for
   *                 DFS.
   * @return boolean representing if the node has been found.
   */
  boolean searchHelp(Node from, Node to, ICollection<Node> worklist) {
    Deque<Node> alreadySeen = new Deque<Node>();
    this.cameFromEdge = new HashMap<Node, Node>();
    this.visited = new ArrayList<Node>();

    // Initialize the worklist with the from Node
    worklist.add(from);
    // As long as the worklist isn't empty...
    while (!worklist.isEmpty()) {
      Node next = worklist.remove();
      if (next.equals(to)) {
        visited.add(next);
        this.constructFinalPath();
        return true; // Node has been found.
      } else if (alreadySeen.contains(next)) {
        // do nothing, Node has been seen.
      } else {
        // add next to visited
        visited.add(next);
        // add all neighbors to be searched.
        if (!next.up) {
          worklist.add(maze.get(next.row - 1).get(next.col));
          if (!alreadySeen.contains(maze.get(next.row - 1).get(next.col))) {
            cameFromEdge.put(maze.get(next.row - 1).get(next.col), next);
          }
        }
        if (!next.left) {
          worklist.add(maze.get(next.row).get(next.col - 1));
          if (!alreadySeen.contains(maze.get(next.row).get(next.col - 1))) {
            cameFromEdge.put(maze.get(next.row).get(next.col - 1), next);
          }
        }
        if (!next.right) {
          worklist.add(maze.get(next.row).get(next.col + 1));
          if (!alreadySeen.contains(maze.get(next.row).get(next.col + 1))) {
            cameFromEdge.put(maze.get(next.row).get(next.col + 1), next);
          }
        }
        if (!next.down) {
          worklist.add(maze.get(next.row + 1).get(next.col));
          if (!alreadySeen.contains(maze.get(next.row + 1).get(next.col))) {
            cameFromEdge.put(maze.get(next.row + 1).get(next.col), next);
          }
        }
        // add next to alreadySeen, since we're done with it
        alreadySeen.addAtHead(next);
      }
    }
    // No more nodes left.
    return false;
  }

  /**
   * Constructs the list of nodes that constitutes the solution set to the maze.
   *
   * @return an ArrayList of nodes representing the solution set.
   */
  ArrayList<Node> constructFinalPath() {
    Node next = maze.get(Maze.MAZE_HEIGHT - 1)
            .get(Maze.MAZE_WIDTH - 1);
    ArrayList<Node> finalPath = new ArrayList<Node>();
    next.partOfFinalPath = true;

    while (next != maze.get(0).get(0)) {
      next = cameFromEdge.get(next);
      next.partOfFinalPath = true;
      finalPath.add(next);
    }

    return finalPath;
  }

  /**
   * Constructs the shortest path from beginning to end of the maze.
   *
   * @return Arraylist of nodes that constitute the shortest path.
   */
  ArrayList<Node> constructShortestPath() {
    for (Node n : this.mazeList) {
      if (n.partOfFinalPath) {
        shortestPath.add(n);
      }
    }

    return shortestPath;
  }

  /**
   * Creates the background to the world.
   * @param ws WorldScene to be drawn on.
   * @return WorldScene representing the background of the Visual Maze Game.
   */
  WorldScene drawBG(WorldScene ws) {
    ws.placeImageXY(new RectangleImage(Maze.CELL_SIZE * Maze.MAZE_WIDTH,
                    Maze.CELL_SIZE * Maze.MAZE_HEIGHT, OutlineMode.SOLID,
                    Color.gray),
            Maze.CELL_SIZE * Maze.MAZE_WIDTH / 2,
            Maze.CELL_SIZE * Maze.MAZE_HEIGHT / 2);
    return ws;
  }

  /**
   * on tick function that increases "time" by 1 for every tick of the game clock.
   */
  public void onTick() {
    this.time = this.time + 1;
  }

  /**
   * Renders visited Cells.
   *
   * @param ws WorldScene to be drawn on.
   */
  public void drawVisited(WorldScene ws) {
    String wrongSteps = "";
    String endText = "";
    if (this.toggleSearch != 0) {
      for (int i = 0; i < Math.min(visited.size(), time); i++) {
        visited.get(i).drawCell(
                visited.get(i).visitCol(time - visited.size()), ws);
      }

      if (bfsLength > dfsLength && time >= visited.size()) {
        endText = "DFS finished "
                + (bfsLength - dfsLength)
                + " steps quicker than BFS";
      } else if (time >= visited.size()) {
        endText = "BFS finished "
                + (dfsLength - bfsLength)
                + " steps quicker than DFS";
      }

      if (toggleSearch == 1 && time >= visited.size()) {
        wrongSteps = "DFS took "
                + (dfsLength - shortestPath.size())
                + " wrong steps";
      } else if (toggleSearch == 2 && time >= visited.size()) {
        wrongSteps = "BFS took "
                + (bfsLength - shortestPath.size())
                + " wrong steps";
      }
    }
    ws.placeImageXY(
            new TextImage(endText, Maze.MAZE_WIDTH * Maze.CELL_SIZE / 20
                    , Color.WHITE),
            MAZE_WIDTH * CELL_SIZE / 2,
            (int) (.75 * MAZE_HEIGHT * CELL_SIZE / 2));
    ws.placeImageXY(
            new TextImage(wrongSteps, Maze.MAZE_WIDTH * Maze.CELL_SIZE / 20
                    , Color.WHITE),
            MAZE_WIDTH * CELL_SIZE / 2,
            (int) (1.25 * MAZE_HEIGHT * CELL_SIZE / 2));


  }

  /**
   * Game ending screen. Returns the number of wrong moves the player took.
   *
   * @param ws End Game Screen.
   */
  public void winScene(WorldScene ws) {
    int totMoves = 0;
    if (p1.row == (this.row - 1) &&
            p1.col == (this.col - 1)) {
      toggleSearch = 0;
      for (Node n : this.mazeList) {
        if (n.traveled) {
          n.drawCell(Color.ORANGE, ws);
          totMoves++;
        }

        if (n.partOfFinalPath) {
          n.drawCell(Color.magenta, ws);
        }
      }
      ws.placeImageXY(
              new TextImage("You Win!!",
                      Maze.MAZE_WIDTH * Maze.CELL_SIZE / 10, Color.WHITE),
              MAZE_WIDTH * CELL_SIZE / 2,
              (int) (.75 * (MAZE_HEIGHT * CELL_SIZE / 2)));

      String wmText = "Wrong Moves: " +
              (totMoves - shortestPath.size() + 1);

      ws.placeImageXY(
              new TextImage(wmText,
                      Maze.MAZE_WIDTH * Maze.CELL_SIZE / 10, Color.WHITE),
              MAZE_WIDTH * CELL_SIZE / 2,
              (int) (1.25 * (MAZE_HEIGHT * CELL_SIZE / 2)));
    }
  }

  /**
   * Colors the beginning and Ending Cell.
   *
   * @param ws World Scene with beginning and ending cell colored in.
   */
  void drawStartEnd(WorldScene ws) {
    Node toDraw = this.maze.get(0).get(0);
    toDraw.drawCell(Color.GREEN, ws);
    toDraw = this.maze.get(Maze.MAZE_HEIGHT - 1).get(Maze.MAZE_WIDTH - 1);
    toDraw.drawCell(Color.BLUE, ws);
  }

  /**
   * Colors the cells that have been traveled by the player.
   * @param ws Maze Game Scene to be drawn on
   * @return Maze Game Scene with the traveled Cells highlighted.
   */
  WorldScene drawTraveled(WorldScene ws) {
    for (Node n : this.mazeList) {
      if (n.traveled) {
        n.drawCell(Color.ORANGE, ws);
      }
    }

    return ws;
  }
}
