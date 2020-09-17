package structures.queue;

public class GenericQueue<T> implements Queue<T> {
  private Node<T> front;
  private int size = 0;

  @Override
  public void queue(T item) {
    if (size == 0) {
      front = new Node<>(item);
      size++;
      return;
    }

    Node<T> node = front.lastNode();
    node.setNext(new Node<>(item, node));
    size++;
  }

  @Override
  public T dequeue() {
    Node<T> frontNode = front;

    if (size == 1) {
      front = null;
    } else {
      front = front.getNext();
      front.makeParent();
    }

    size--;

    return frontNode.getData();
  }

  @Override
  public int size() {
    return size;
  }

  @Override
  public void print() {
    System.out.print("Elements: ");
    Node<T> lastNode = front.lastNode();

    lastNode.printParent(System.out);
    System.out.println();
  }
}
