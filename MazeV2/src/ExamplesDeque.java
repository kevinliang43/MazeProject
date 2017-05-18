import tester.Tester;

/**
 * Created by KevinLiang on 5/17/17.
 */

public class ExamplesDeque {

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

  void init() {

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
    this.init();
    DNode<String> newDNode1 = new DNode<String>("abc", DNodeA, sent1);
    Deque<String> deq1Addnew = new Deque<String>(sent1);
    DNode<String> newDNode2 = new DNode<String>("xyz", sent3, sent3);
    Deque<String> deq3Addnew = new Deque<String>(sent3);


    deque1.addAtHead("abc");
    t.checkExpect(deque1, deq1Addnew);
    deque3.addAtHead("xyz");
    t.checkExpect(deque3, deq3Addnew);
    this.init();
  }

  // test removeAddAtTail
  void testAddAtTail(Tester t) {
    this.init();
    DNode<String> newDNode1 = new DNode<String>("Kevin", sent1, DNodeD);
    Deque<String> deq1Tailnew = new Deque<String>(sent1);
    DNode<String> ElemY = new DNode<String>("zyx", sent3, sent3);
    Deque<String> deq2Tailnew = new Deque<String>(sent3);


    deque1.addAtTail("Kevin");
    t.checkExpect(deque1, deq1Tailnew);
    deque3.addAtTail("zyx");
    t.checkExpect(deque3, deq2Tailnew);
    this.init();
  }

  // test removeFromHead
  void testRemoveFromHead(Tester t) {
    this.init();
    t.checkException(new RuntimeException("Empty"), deque3, "removeFromHead");
    Sentinel<String> sentAlphabet = new Sentinel<String>();
    DNode<String> DNodebcd = new DNode<String>("bcd", sentAlphabet, sentAlphabet);
    DNode<String> DNodecde = new DNode<String>("cde", sentAlphabet, DNodebcd);
    DNode<String> DNodedef = new DNode<String>("def", sentAlphabet, DNodecde);
    Deque<String> dequeAlphabet = new Deque<String>(sentAlphabet);

    deque1.removeFromHead();
    t.checkExpect(deque1, dequeAlphabet);
    this.init();
  }

  // test removeFromTail
  void testRemoveFromTail(Tester t) {
    this.init();
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
    this.init();
  }

  // test find
  void testFind(Tester t) {
    this.init();

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
    this.init();
    Sentinel<String> sent0 = new Sentinel<String>();
//        t.checkExpect(deque1.removeNode(sent0), deque1);
  }

  // test asNode
  void testAsNode(Tester t) {
    this.init();
    t.checkExpect(DNode1.asNode(), DNode1);
    Sentinel<String> newSent1 = new Sentinel<String>();
    t.checkException(new RuntimeException("This isn't a Node"), newSent1, "asNode");
  }

  // test removeNode
  void testRemoveNode(Tester t) {
    this.init();
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

  // test constructor of nodes
  void testConstructor(Tester t) {
    this.init();

    t.checkConstructorException(new IllegalArgumentException("You're not allowed to do this"),
            "DNode", "Kevin", null, sent1);
    t.checkConstructorException(new IllegalArgumentException("You're not allowed to do this"),
            "DNode", "Kevin", sent2, null);
    t.checkConstructorException(new IllegalArgumentException("You're not allowed to do this"),
            "DNode", "Kevin", null, null);

    this.init();
  }

  // test contains
  void testContains(Tester t) {
    this.init();
    t.checkExpect(this.deque1.contains("abc"), true);
    t.checkExpect(this.deque1.contains("xyz"), false);
  }
}

