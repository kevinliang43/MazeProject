/**
 * Created by KevinLiang on 5/19/17.
 */


import tester.Tester;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

// to represent examples and tests
public class ExampleMazeTests {

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

  IsEven even = new IsEven();
  SmallString small = new SmallString();

  Sentinel<String> sent1 = new Sentinel<String>();
  DNode<String> DNodeA = new DNode<String>("abc", sent1, sent1);
  DNode<String> DNodeB = new DNode<String>("bcd", sent1, DNodeA);
  DNode<String> DNodeC = new DNode<String>("cde", sent1, DNodeB);
  DNode<String> DNodeD = new DNode<String>("def", sent1, DNodeC);
  Deque<String> deque1 = new Deque<String>(sent1);


  Sentinel<Integer> sent2 = new Sentinel<Integer>();
  DNode<Integer> DNode1 = new DNode<Integer>(20, sent2, sent2);
  DNode<Integer> DNode2 = new DNode<Integer>(100, sent2, DNode1);
  DNode<Integer> DNode3 = new DNode<Integer>(-10, sent2, DNode2);
  DNode<Integer> DNode4 = new DNode<Integer>(5, sent2, DNode3);
  DNode<Integer> DNode5 = new DNode<Integer>(500, sent2, DNode4);
  DNode<Integer> DNode6 = new DNode<Integer>(3000, sent2, DNode5);
  Deque<Integer> deque2 = new Deque<Integer>(sent2);

  Sentinel<String> sent3 = new Sentinel<String>();

  Deque<String> deque3 = new Deque<String>();

  Deque<String> deque4 = new Deque<String>(sent3);

  Sentinel<Integer> sent5 = new Sentinel<Integer>();
  Deque<Integer> deque5 = new Deque<Integer>(sent5);

  DNode<Integer> DNode7 = new DNode<Integer>(10, sent5, sent5);
  DNode<Integer> DNode8 = new DNode<Integer>(3, sent5, DNode7);
  DNode<Integer> DNode9 = new DNode<Integer>(7, sent5, DNode8);

  void init2() {

    sent1 = new Sentinel<String>();
    DNodeA = new DNode<String>("abc", sent1, sent1);
    DNodeB = new DNode<String>("bcd", sent1, DNodeA);
    DNodeC = new DNode<String>("cde", sent1, DNodeB);
    DNodeD = new DNode<String>("def", sent1, DNodeC);
    deque1 = new Deque<String>(sent1);

    deque2 = new Deque<Integer>(sent2);
    sent2 = new Sentinel<Integer>();
    DNode1 = new DNode<Integer>(20, sent2, sent2);
    DNode2 = new DNode<Integer>(100, sent2, DNode1);
    DNode3 = new DNode<Integer>(-10, sent2, DNode2);
    DNode4 = new DNode<Integer>(5, sent2, DNode3);
    DNode5 = new DNode<Integer>(500, sent2, DNode4);
    DNode6 = new DNode<Integer>(3000, sent2, DNode5);

    deque3 = new Deque<String>();

    deque4 = new Deque<String>(sent3);

    sent5 = new Sentinel<Integer>();
    deque5 = new Deque<Integer>(sent5);

    DNode7 = new DNode<Integer>(10, sent5, sent5);
    DNode8 = new DNode<Integer>(20, sent5, DNode7);
    DNode9 = new DNode<Integer>(30, sent5, DNode8);

  }

  // test Size
  void testSize(Tester t) {
    t.checkExpect(deque1.size(), 4);
    t.checkExpect(deque2.size(), 6);
    t.checkExpect(deque3.size(), 0);
    t.checkExpect(deque4.size(), 0);
    t.checkExpect(deque5.size(), 3);

  }

  // test addAtHead
  void testAddAtHead(Tester t) {
    this.init2();
    DNode<String> newDNode1 = new DNode<String>("abc", DNodeA, sent1);
    Deque<String> deq1Addnew = new Deque<String>(sent1);
    DNode<String> newDNode2 = new DNode<String>("xyz", sent3, sent3);
    Deque<String> deq3Addnew = new Deque<String>(sent3);


    deque1.addAtHead("abc");
    t.checkExpect(deque1, deq1Addnew);
    deque3.addAtHead("xyz");
    t.checkExpect(deque3, deq3Addnew);
    this.init2();
  }

  // test removeAddAtTail
  void testAddAtTail(Tester t) {
    this.init2();
    DNode<String> newDNode1 = new DNode<String>("Kevin", sent1, DNodeD);
    Deque<String> deq1Tailnew = new Deque<String>(sent1);
    DNode<String> ElemY = new DNode<String>("zyx", sent3, sent3);
    Deque<String> deq2Tailnew = new Deque<String>(sent3);


    deque1.addAtTail("Kevin");
    t.checkExpect(deque1, deq1Tailnew);
    deque3.addAtTail("zyx");
    t.checkExpect(deque3, deq2Tailnew);
    this.init2();
  }

  // test removeFromHead
  void testRemoveFromHead(Tester t) {
    this.init2();
    t.checkException(new RuntimeException("Empty"), deque3, "removeFromHead");
    Sentinel<String> sentAlphabet = new Sentinel<String>();
    DNode<String> DNodebcd = new DNode<String>("bcd", sentAlphabet, sentAlphabet);
    DNode<String> DNodecde = new DNode<String>("cde", sentAlphabet, DNodebcd);
    DNode<String> DNodedef = new DNode<String>("def", sentAlphabet, DNodecde);
    Deque<String> dequeAlphabet = new Deque<String>(sentAlphabet);

    deque1.removeFromHead();
    t.checkExpect(deque1, dequeAlphabet);
    this.init2();
  }

  // test removeFromTail
  void testRemoveFromTail(Tester t) {
    this.init2();
    t.checkException(new RuntimeException("Empty"), deque3, "removeFromTail");
    Sentinel<Integer> sentNumber = new Sentinel<Integer>();
    DNode<Integer> DNode1Number = new DNode<Integer>(20, sentNumber, sentNumber);
    DNode<Integer> DNode2Number = new DNode<Integer>(100, sentNumber, DNode1Number);
    DNode<Integer> DNode3Number = new DNode<Integer>(-10, sentNumber, DNode2Number);
    DNode<Integer> DNode4Number = new DNode<Integer>(5, sentNumber, DNode3Number);
    DNode<Integer> DNode5Number = new DNode<Integer>(500, sentNumber, DNode4Number);
    Deque<Integer> deque2Number = new Deque<Integer>(sentNumber);


    deque2.removeFromTail();
    t.checkExpect(deque2, deque2Number);
    this.init2();
  }

  // test find
  void testFind(Tester t) {
    this.init2();

    t.checkExpect(deque1.find(small), sent1.next);
    t.checkExpect(deque2.find(even), sent2.next);
    t.checkExpect(deque3.find(small), new Sentinel<String>());
    t.checkExpect(deque5.find(even), sent5.next);

    // test finHelp
    t.checkExpect(DNode1.findHelp(even), DNode1);
    t.checkExpect(DNode2.findHelp(even), DNode2);
    t.checkExpect(sent2.findHelp(even), sent2);
    t.checkExpect(sent3.findHelp(small), sent3);
    t.checkExpect(sent1.findHelp(small), sent1);
    t.checkExpect(sent5.findHelp(even), sent5);
  }

  // test isEven
  void testIsEven(Tester t) {
    t.checkExpect(even.apply(2), true);
    t.checkExpect(even.apply(3), false);
  }

  // test smallString
  void testSmallString(Tester t) {
    t.checkExpect(small.apply("aa"), true);
    t.checkExpect(small.apply(""), true);
    t.checkExpect(small.apply("lkjaslkdfjlkasj"), false);
  }

  // test removeThis
  void testRemoveThis(Tester t) {
    this.init2();
    Sentinel<String> sent0 = new Sentinel<String>();
  }

  // test asNode
  void testAsNode(Tester t) {
    this.init2();
    t.checkExpect(DNode1.asNode(), DNode1);
    Sentinel<String> newSent1 = new Sentinel<String>();
    t.checkException(new RuntimeException("This isn't a Node"), newSent1, "asNode");
  }

  // test removeNode
  void testRemoveNode(Tester t) {
    this.init2();
    Deque<String> deque1Copy = deque1;
    deque1.removeNode(DNodeA);
    t.checkExpect(deque1, deque1Copy);
    Sentinel<String> sent2RN = new Sentinel<String>();
    DNode<String> ElemARN = new DNode<String>("abc", sent2RN, sent2RN);
    DNode<String> ElemBRN = new DNode<String>("bcd", sent2RN, ElemARN);
    DNode<String> ElemDRN = new DNode<String>("def", sent2RN, ElemBRN);
    Deque<String> deque2RN = new Deque<String>(sent2RN);
    deque1.removeNode(DNodeC);


  }

  // test contains
  void testContains(Tester t) {
    this.init2();
    t.checkExpect(this.deque1.contains("abc"), true);
    t.checkExpect(this.deque1.contains("xyz"), false);
  }


}
