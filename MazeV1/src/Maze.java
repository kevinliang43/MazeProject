import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

import tester.Tester;

import java.awt.Color;

import javalib.impworld.World;
import javalib.impworld.WorldScene;
import javalib.worldimages.LineImage;
import javalib.worldimages.OutlineMode;
import javalib.worldimages.Posn;
import javalib.worldimages.RectangleImage;
import javalib.worldimages.TextImage;


// Represents the conceptual Maze

class Maze extends World {

  // Constants: use to change game size
  // Maze width (up to 100 cells wide)
  static final int MAZE_WIDTH = 10;
  // Maze height (up to 60 cells long)
  static final int MAZE_HEIGHT = 10;
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

  // constructor
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

/**
 * Node Class. Represents each cell of the maze.
 */
class Node extends DNode<Integer> {
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

/**
 * Edge class. Edge is created by two nodes.
 */
class Edge implements Comparable<Edge> {
  // represent nodes that constitute an edge.
  Node a;
  Node b;
  // weight of the edge
  int weight;

  // constructor for the edge class.
  Edge(Node a, Node b, int weight) {
    this.a = a;
    this.b = b;
    this.weight = weight;
  }

  // comparator for Collections.sort()

  /**
   * function to return a comparator for edge weights.
   * @param e other edge to be compared to
   * @return an integer representing whether this edge is less, the same, or greater than the given
   * edge, in terms of weight.
   */
  public int compareTo(Edge e) {
    return this.weight - e.weight;
  }
}

// to represent a player manually moving through the maze
class Player {
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

// to represent examples and tests
class ExampleMaze {

  // runs the game
  void testMaze(Tester t) {
    this.init();

    this.m1.bigBang(Maze.MAZE_WIDTH * Maze.CELL_SIZE,
            Maze.MAZE_HEIGHT * Maze.CELL_SIZE, .1);
  }

  Maze m1;
    
    /* A-----B-----C
     * |     |     |
     * |  1  12 2  |
     * |     |     |
     * D--10-E--4--F
     * |     |     |
     * |  3  9  4  |
     * |     |     |
     * G-----H-----J
     */

  Node n1 = new Node(1, 1, 1, true, true, true, true);
  Node n2 = new Node(1, 2, 2, true, true, true, true);
  Node n3 = new Node(1, 3, 3, true, true, true, true);
  Node n4 = new Node(2, 1, 4, true, true, true, true);
  Node n5 = new Node(1, 1, 1, false, false, false, false);

  Edge be = new Edge(this.n1, this.n2, 12);
  Edge de = new Edge(this.n1, this.n3, 10);
  Edge ef = new Edge(this.n2, this.n4, 4);
  Edge eh = new Edge(this.n3, this.n4, 9);

  ArrayList<Node> arr1 = new ArrayList<Node>();
  ArrayList<Edge> arr2 = new ArrayList<Edge>();
  ArrayList<Integer> arr3 = new ArrayList<Integer>();
  HashMap<Node, Node> hm1 = new HashMap<Node, Node>();

  ArrayList<Edge> edgesInTree = new ArrayList<Edge>();

  Player player;

  // initialize sample data
  void init() {
    m1 = new Maze();
    this.arr1.add(n1);
    this.arr1.add(n2);
    this.arr1.add(n3);
    this.arr1.add(n4);

    this.arr2.add(be);
    this.arr2.add(de);
    this.arr2.add(ef);
    this.arr2.add(eh);

    this.arr3.add(1);
    this.arr3.add(2);
    this.arr3.add(3);
    this.arr3.add(4);

    player = new Player(0, 0, 0);
    n5 = new Node(0, 0, 0, false, false, false, false);
  }

  // test initRep that produces the hashmap
  void testInitRep(Tester t) {
    this.init();
    this.m1.initRep();
    t.checkExpect(this.m1.representatives.isEmpty(), false);
    t.checkExpect(this.m1.representatives.size(),
            Maze.MAZE_HEIGHT * Maze.MAZE_WIDTH);
    t.checkExpect(this.m1.representatives.get(m1.maze.get(0).get(0)),
            m1.maze.get(0).get(0));
    t.checkExpect(this.m1.representatives.get(
            m1.maze.get(Maze.MAZE_HEIGHT - 1).get(
                    Maze.MAZE_WIDTH - 1)),
            m1.maze.get(Maze.MAZE_HEIGHT - 1).get(
                    Maze.MAZE_WIDTH - 1));

  }

  // test initMaze that produces the arrayList<arrayList<Node>>
  void testInitMaze(Tester t) {
    this.init();
    this.m1.initMaze();
    t.checkExpect(this.m1.maze.get(0).get(0),
            new Node(0, 0, 0, true, true, true, true));
    t.checkExpect(this.m1.maze.get(1).get(1),
            new Node(1, 1, m1.col + 1,
                    true, true, true, true));
  }

  // test initMazeList that produces the arrayList<Node>
  void testInitMazeList(Tester t) {
    this.init();
    this.m1.initMazeList();
    t.checkExpect(this.m1.mazeList.get(0).row, 0);
    t.checkExpect(this.m1.mazeList.get(0).col, 0);
    t.checkExpect(this.m1.mazeList.get(0).nCell, 0);
    t.checkExpect(this.m1.mazeList.get(2).row, 0);
    t.checkExpect(this.m1.mazeList.get(2).col, 2);
    t.checkExpect(this.m1.mazeList.get(2).nCell, 2);
  }

  // tests initWorkList that produces the arrayList<Edge>
  void testInitWorkList(Tester t) {
    this.init();
    this.m1.initWorkList();
    // test if edges have random weights
    t.checkRange(this.m1.worklist.get(10).weight, 0, this.m1.nCells - 1);
    t.checkRange(this.m1.worklist.get(20).weight, 0, this.m1.nCells - 1);
    // test if worklist is sorted
    t.checkExpect(this.m1.worklist.get(0).weight <
            this.m1.worklist.get(10).weight, true);
    t.checkExpect(this.m1.worklist.get(0).weight <
            this.m1.worklist.get(100).weight, true);

    // test edge comparator
    t.checkExpect(this.ef.compareTo(this.eh), -5);
    t.checkExpect(this.ef.compareTo(this.ef), 0);
    t.checkExpect(this.eh.compareTo(this.ef), 5);

  }

  // tests for kruskalAlgo that produces the spanning tree
  void testKruskalAlgo(Tester t) {
    this.init();
    this.m1.initMaze();
    this.m1.initRep();
    this.m1.initWorkList();
    this.m1.initMazeList();
    this.m1.edgesInTree = new ArrayList<Edge>();
    t.checkExpect(this.m1.edgesInTree.size(), 0);
    t.checkExpect(this.m1.worklist.size(),
            2 * (Maze.MAZE_HEIGHT) * (Maze.MAZE_WIDTH) -
                    Maze.MAZE_HEIGHT - Maze.MAZE_WIDTH);

    this.m1.kruskalAlgo();
    t.checkExpect(this.m1.edgesInTree.size(),
            Maze.MAZE_HEIGHT * Maze.MAZE_WIDTH - 1);
    t.checkRange(this.m1.worklist.size(), 0,
            2 * (Maze.MAZE_HEIGHT) * (Maze.MAZE_WIDTH) -
                    Maze.MAZE_HEIGHT - Maze.MAZE_WIDTH - this.m1.nCells + 1);
  }

  // test union/find methods using declared elements
  void testHM(Tester t) {
    this.init();

    for (Node i : arr1) {
      this.hm1.put(i, i);
    }

    t.checkExpect(hm1.size(), 4);

    // initially all ints are linked to themselves
    t.checkExpect(this.hm1.get(n1), n1);
    t.checkExpect(this.m1.find(this.hm1, n1), n1);
    t.checkExpect(this.m1.find(this.hm1, n4), n4);

    // link two representatives
    this.m1.union(this.hm1, n4, n2);
    this.m1.union(this.hm1, n1, n4);

    // after linking
    t.checkExpect(this.hm1.get(n4), n1);
    t.checkExpect(this.m1.find(this.hm1, n4), n1);
    t.checkExpect(this.hm1.get(n2), n4);
    t.checkExpect(this.m1.find(this.hm1, n2), n1);
  }

  // tests for makePath to determine correct walls of each node
  void testMakePath(Tester t) {
    this.init();
    this.m1.makePath();

    for (Edge e : this.edgesInTree) {
      if (e.b.col - e.a.col == 1) {
        t.checkExpect(e.b.left, false);
        t.checkExpect(e.a.right, false);
      }
      if (e.b.row - e.a.row == 1) {
        t.checkExpect(e.b.down, false);
        t.checkExpect(e.a.up, false);
      }

    }
    // for the wrong case, the walls are built
    // even though there should be none(since they're connected)
    for (Edge e : this.arr2) {
      if (e.b.col - e.a.col == 1) {
        t.checkExpect(e.b.left, true);
        t.checkExpect(e.a.right, true);
      }
      if (e.b.row - e.a.row == 1) {
        t.checkExpect(e.b.down, true);
        t.checkExpect(e.a.up, true);
      }

    }

    t.checkExpect(this.m1.edgesInTree.size(),
            Maze.MAZE_HEIGHT * Maze.MAZE_WIDTH - 1);
  }

  // tests for dfs
  void testDfs(Tester t) {
    this.init();
    t.checkExpect(m1.dfs(m1.maze.get(0).get(0),
            m1.maze.get(Maze.MAZE_HEIGHT - 1).get(
                    Maze.MAZE_WIDTH - 1)), true);

  }

  // tests for dfs
  void testBfs(Tester t) {
    this.init();
    t.checkExpect(m1.bfs(m1.maze.get(0).get(0),
            m1.maze.get(Maze.MAZE_HEIGHT - 1).get(Maze.MAZE_WIDTH - 1)),
            true);
  }

  // test for constructFinalPath
  void constructFinalPath(Tester t) {
    this.init();
    int shortestPossible = (int) (Math.pow((Math.pow(Maze.MAZE_HEIGHT, 2) +
            Math.pow(Maze.MAZE_WIDTH, 2)), .5));
    int longestPossible = Maze.MAZE_HEIGHT + Maze.MAZE_WIDTH;
    t.checkRange(m1.constructFinalPath().size(),
            shortestPossible, longestPossible);
    t.checkExpect(m1.maze.get(0).get(0).partOfFinalPath, true);
    t.checkExpect(m1.maze.get(Maze.MAZE_HEIGHT - 1)
            .get(Maze.MAZE_HEIGHT - 1).partOfFinalPath, true);
  }

  // test for constructShortestPath
  void constructShortestPath(Tester t) {
    this.init();
    int shortestPossible = (int) (Math.pow((Math.pow(Maze.MAZE_HEIGHT, 2) +
            Math.pow(Maze.MAZE_WIDTH, 2)), .5));
    int longestPossible = Maze.MAZE_HEIGHT + Maze.MAZE_WIDTH;
    t.checkRange(m1.constructShortestPath().size(),
            shortestPossible, longestPossible);
    t.checkExpect(m1.constructShortestPath().contains(
            m1.maze.get(Maze.MAZE_HEIGHT - 1).get(Maze.MAZE_WIDTH - 1)),
            true);
    t.checkExpect(m1.constructShortestPath().contains(
            m1.maze.get(0).get(0)), true);
  }

  // test for visitCol
  void testVisitCol(Tester t) {
    this.init();
    t.checkExpect(m1.maze.get(0).get(0).visitCol(0), Color.lightGray);
    t.checkExpect(m1.maze.get(0).get(0).visitCol(-1), Color.darkGray);
    t.checkExpect(new Node(0, 0, 0, true, true, true, true).visitCol(0),
            Color.darkGray);
    t.checkExpect(new Node(0, 0, 0, true, true, true, true).visitCol(-1),
            Color.darkGray);
  }

  // test for movePlayer
  void testMovePlayer(Tester t) {
    this.init();
    t.checkExpect(player.col, 0);
    t.checkExpect(player.row, 0);
    t.checkExpect(n1.traveled, false);
    player.movePlayer(n1, "left");
    t.checkExpect(player.col, 0);
    t.checkExpect(player.row, 0);
    t.checkExpect(n1.traveled, false);
    player.movePlayer(n1, "right");
    t.checkExpect(player.col, 0);
    t.checkExpect(player.row, 0);
    t.checkExpect(n1.traveled, false);
    player.movePlayer(n1, "up");
    t.checkExpect(player.col, 0);
    t.checkExpect(player.row, 0);
    t.checkExpect(n1.traveled, false);
    player.movePlayer(n1, "down");
    t.checkExpect(player.col, 0);
    t.checkExpect(m1.p1.row, 0);
    t.checkExpect(n1.traveled, false);
    player.movePlayer(n5, "left");
    t.checkExpect(player.col, -1);
    t.checkExpect(player.row, 0);
    t.checkExpect(n5.traveled, true);
    this.init();
    player.movePlayer(n5, "right");
    t.checkExpect(player.col, 1);
    t.checkExpect(player.row, 0);
    t.checkExpect(n5.traveled, true);
    this.init();
    player.movePlayer(n5, "up");
    t.checkExpect(player.col, 0);
    t.checkExpect(player.row, -1);
    t.checkExpect(n5.traveled, true);
    this.init();
    player.movePlayer(n5, "down");
    t.checkExpect(player.col, 0);
    t.checkExpect(player.row, 1);
    t.checkExpect(n5.traveled, true);

  }

  // test onkeyevent
  void testOnKeyEvent(Tester t) {
    this.init();
    m1.p1 = new Player(1, 1, 1);
    m1.time = 5;
    m1.toggleSearch = 2;
    m1.onKeyEvent("r");
    t.checkExpect(m1.p1, new Player(0, 0, 0));
    t.checkExpect(m1.time, 0);
    t.checkExpect(m1.toggleSearch, 0);
    m1.time = 5;
    m1.p1 = new Player(1, 1, 1);
    m1.toggleSearch = 0;
    m1.onKeyEvent("b");
    t.checkExpect(m1.p1, new Player(1, 1, 1));
    t.checkExpect(m1.time, 0);
    t.checkExpect(m1.toggleSearch, 2);
    m1.time = 5;
    m1.p1 = new Player(1, 1, 1);
    m1.toggleSearch = 0;
    m1.onKeyEvent("d");
    t.checkExpect(m1.p1, new Player(1, 1, 1));
    t.checkExpect(m1.time, 0);
    t.checkExpect(m1.toggleSearch, 1);
  }

}