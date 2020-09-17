package structures.list;

import java.io.PrintStream;

public interface LinkedList<T> {

  void add(T item);

  void add(T item, int index);

  T get(int index);

  void remove(int index);

  int size();

  void print(PrintStream stream);
}
