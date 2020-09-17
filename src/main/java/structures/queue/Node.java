package structures.queue;

import java.io.PrintStream;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

class Node<T> {
  private T data;
  private Node<T> next;
  private Node<T> previous;

  Node(T data) {
    this.data = data;
    this.next = null;
    this.previous = null;
  }

  Node(T data, Node<T> previous) {
    this.data = data;
    this.next = null;
    this.previous = previous;
  }

  T getData() {
    return data;
  }

  Node<T> getNext() {
    return next;
  }

  void setNext(Node<T> next) {
    this.next = next;
  }

  void makeParent() {
    previous = null;
  }

  Node<T> lastNode() {
    if (nonNull(next)) {
      return next.lastNode();
    }

    return this;
  }

  void printParent(PrintStream stream) {
    stream.print(data);

    if (nonNull(previous)) {
      stream.print(", ");
      previous.printParent(stream);
    }
  }
}
