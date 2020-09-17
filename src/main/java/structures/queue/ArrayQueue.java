package structures.queue;

public class ArrayQueue<T> implements Queue<T> {
  private int rear = -1;
  private int front = 0;
  private int size = 0;
  private int capacity = 10000;
  private T[] items = (T[]) new Object[capacity];



  @Override
  public void queue(T item) {
    rear = (rear + 1) % capacity;
    items[rear] = item;
    size++;
  }

  @Override
  public T dequeue() {
    T item = items[front];
    size--;

    front = (front + 1) % capacity;

    return item;
  }

  @Override
  public int size() {
    return size;
  }

  @Override
  public void print() {
    for (int i = 0; i < size; i++) {
      System.out.print(items[front + i] + ", ");
    }
  }
}
