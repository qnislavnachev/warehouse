package structures.list;

import java.io.PrintStream;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

class Node<T> {
  private T data;
  private Node<T> next;

  Node(T data) {
    this.data = data;
    this.next = null;
  }

  Node(T data, Node<T> next) {
    this.data = data;
    this.next = next;
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

  Node<T> getNodeAt(int index) {
    return getNodeAt(index, 0);
  }

  Node<T> lastNode() {
    if (nonNull(next)) {
      return next.lastNode();
    }

    return this;
  }

  void printChildren(PrintStream stream) {
    stream.print(data);

    if (nonNull(next)) {
      stream.print(", ");
      next.printChildren(stream);
    }
  }

  private Node<T> getNodeAt(int index, int countIndex) {
    if (countIndex == index) {
      return this;
    }

    if (isNull(next)) {
      return null;
    }

    return next.getNodeAt(index, countIndex + 1);
  }
}
