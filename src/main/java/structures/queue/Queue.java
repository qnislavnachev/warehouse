package structures.queue;

public interface Queue<T> {

  void queue(T item);

  T dequeue();

  int size();

  void print();
}
