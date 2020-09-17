package structures.stack;

public interface Stack<T> {

  void push(T item);

  T pop();

  T peek();

  int size();

  void print();
}
