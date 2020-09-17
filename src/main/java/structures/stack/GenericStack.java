package structures.stack;

import java.util.Arrays;

public class GenericStack<T> implements Stack<T> {
  private int size = 0;
  private int capacity = 5;
  private T[] items = (T[]) new Object[capacity];

  @Override
  public void push(T item) {
    if (size == capacity) {
      extend();
    }

    items[size] = item;
    size++;
  }

  @Override
  public T pop() {
    T item = items[size - 1];
    items[size - 1] = null;
    size--;

    return item;
  }

  @Override
  public T peek() {
    return items[size - 1];
  }

  @Override
  public int size() {
    return size;
  }

  @Override
  public void print() {
    System.out.print("Elements: ");
    for (int i = 0; i < size; i++) {
      System.out.print(", " + items[i]);
    }

    System.out.println();
  }

  private void extend() {
    capacity *= 2;
    items = Arrays.copyOf(items, capacity);
  }
}
