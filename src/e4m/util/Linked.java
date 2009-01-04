package e4m.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

public abstract class Linked<E> implements Iterable<E>{

  protected class Item {
    Item next;
    E object;
  }

  Item anchor;

  Linked() {
    anchor = new Item();
    anchor.next = anchor;
  }

  public boolean empty() {
    return (anchor.next == anchor.next.next);
  }

  abstract public void put(E o);
  abstract public E get();
  abstract public E peek();

  protected Iterator<E> iterator(final Item tip) {
    return new Iterator<E>() {

      Item edge = tip;
      Item current = edge;
      Item previous = null;

      public boolean hasNext() {
        return (current.next != edge);
      }

      public E next() {
        if ( ! hasNext()) {
          throw new NoSuchElementException();
        }
        previous = current;
        current = previous.next;
        return current.object;
      }

      public void remove() {
        if (previous == null) {
          throw new IllegalStateException();
        }
        previous.next = previous.next.next;
        previous = null;
      }
    };
  }

}
