package e4m.util;

import java.util.Iterator;

public class LIFO<E> extends Linked<E> {

  public LIFO() { super(); }

  public void put(E object) {
    Item i = new Item();
    i.object = object;
    i.next = anchor.next;
    anchor.next = i;
  }

  public E get() {
    Item i = anchor.next;
    anchor.next = i.next;
    return i.object;
  }

  public E peek() {
    return anchor.next.object;
  }

  public Iterator<E> iterator() {
    return iterator(anchor);
  }

}
