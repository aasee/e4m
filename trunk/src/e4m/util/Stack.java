package e4m.util;

public class Stack<E> extends LIFO<E> implements Stackable<E> {

  public Stack() {
    super();
  }

  public E pop() {
    return get();
  }

  public E push(E item) {
    put(item);
    return item;
  }

}
