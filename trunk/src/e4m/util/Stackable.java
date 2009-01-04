package e4m.util;

public interface Stackable<E> {

  boolean empty();

  E peek();
  E pop();
  E push(E object);

}
