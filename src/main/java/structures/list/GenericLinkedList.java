package structures.list;

import java.io.PrintStream;

public class GenericLinkedList<T> implements LinkedList<T> {
  private Node<T> root = null;
  private int size = 0;

  @Override
  public void add(T item) {
    // current size is the index of the next element
    Node<T> newNode = new Node<>(item);

    if (size == 0) {
      root = newNode;
      size++;
      return;
    }

    Node<T> lastNode = root.lastNode();
    lastNode.setNext(newNode);
    size++;
  }

  @Override
  public void add(T item, int index) {
    if (size < index || index < 0) {
      return;
    }

    Node<T> node = root.getNodeAt(index - 1);
    Node<T> nextNode = node.getNext();

    node.setNext(new Node<>(item, nextNode));
    size++;
  }

  @Override
  public T get(int index) {
    if (size == 0 || size <= index || index < 0) {
      return null;
    }

    if (index == 0) {
      return root.getData();
    }

    Node<T> node = root.getNodeAt(index);
    return node.getData();
  }

  @Override
  public void remove(int index) {
    if (size == 0 || size <= index || index < 0) {
      return;
    }

    Node<T> node = root.getNodeAt(index - 1);
    Node<T> nodeToDelete = node.getNext();

    node.setNext(nodeToDelete.getNext());
    size--;
  }

  @Override
  public int size() {
    return size;
  }

  @Override
  public void print(PrintStream stream) {
    if (size == 0) {
      return;
    }

    stream.print("Elements: ");
    root.printChildren(stream);
  }
}
