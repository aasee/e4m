package e4m.util;

import java.util.Iterator;

public class FIFO<E> extends Linked<E> {

  public FIFO() { super(); }

  public void put(E object) {
    Item i = new Item();
    i.object = object;
    i.next = anchor.next;
    anchor.next = i;
    anchor = i;
  }

  public E get() {
    Item i = (anchor.next).next;
    (anchor.next).next = i.next;
    return i.object;
  }

  public E peek() {
    return (anchor.next).next.object;
  }

  public Iterator<E> iterator() {
    return iterator(anchor.next);
  }

}
