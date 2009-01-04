package e4m.util;

import java.util.Arrays;

public class IntPool {
  
  protected short[] indirect;
  protected int[] pool;
  protected int used;
  int free;
  int fill;
 
  public IntPool(int size, int fill) {
    this.indirect = new short[size];
    this.pool = new int[64]; // initial attribute pool size
    this.fill = fill;
    clear();
  }
  
  public void clear() {
    Arrays.fill(indirect,(short)0);
    Arrays.fill(pool,fill);
    used = 0;
    free = 0;
  }
  
  public int get(int index) {
    return (indirect[index] < 1) ? fill : pool[ indirect[index] - 1 ];
  }
  
  public void put(int index, int bits) {
    remove(index);
    find_next_free();
    pool[free] = bits;
    used++;
    indirect[index] = (short)( free + 1 );
  }

  public void remove(int index) {
    if (indirect[index] < 1) return;
    free = indirect[index] - 1;
    pool[free] = fill; 
    used--;
    indirect[index] = 0;
  }

  void find_next_free() {
    if (used < pool.length) {
      while (pool[free] != fill) {
        free = (++free) % pool.length;
      }  
    }
    else { // used >= pool.length
      free = pool.length;
      int newLength = free + 16;  // grow by 16, but only until 16K
      pool = Arrays.copyOf(pool, newLength);
      Arrays.fill(pool, free, pool.length, fill);
    }  
  }

  public int next(int from) {
    for (int index = from; index < indirect.length; index++) {
      if (indirect[index] != 0)
        return index;
    }  
    return -1;
  }

}
