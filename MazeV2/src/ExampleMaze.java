/**
 * Created by KevinLiang on 5/17/17.
 */

import tester.Tester;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

// to represent examples and tests
public class ExampleMaze {

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
