package structures;

import structures.queue.GenericQueue;
import structures.queue.Queue;

public class Demo1 {

  public static void main(String[] args) {
//    LinkedList<String> list = new GenericLinkedList<>();
//
//    list.add("one");
//    list.add("two");
//    list.add("three");
//
//    list.add("two and half", 2);
//
//
//    list.print(System.out);

//    Stack<String> stack = new GenericStack<>();
//    stack.push("one");
//    stack.push("two");
//    stack.push("three");
//    stack.push("four");
//    stack.push("five");
//    stack.push("six");
//    stack.push("seven");
//    stack.push("eight");
//    stack.push("nine");
//    stack.push("ten");
//
//
//    stack.print();
//    stack.peek();
//    stack.pop();
//    stack.print();
    long s = System.currentTimeMillis();
    Queue<String> queue = new GenericQueue<>();

    queue.queue("one");
    queue.queue("two");
    queue.queue("three");
    queue.queue("four");
    queue.queue("five");
    queue.queue("six");

    queue.print();

    System.out.println();
    System.out.println("It took: " + (System.currentTimeMillis() - s));
  }
}
