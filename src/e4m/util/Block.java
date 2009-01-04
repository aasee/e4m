package e4m.util;


import java.nio.ByteBuffer;

public class Block {

    byte[] text;
    int rows, columns;

    public Block(int rows, int columns) {
        resize(rows,columns);
    }

    public void resize(int rows, int columns) {
        if (text == null || this.rows != rows || this.columns != columns) {
            this.rows = rows;
            this.columns = columns;
            this.text = new byte[ this.rows * this.columns ];
        }
    }

    public int rows() { return rows; }
    public int columns() { return columns; }

    public ByteBuffer toByteBuffer() {
        return toByteBuffer(0,text.length);
    }

    public ByteBuffer toByteBuffer(int offset, int length) {
        return ByteBuffer.wrap(text,offset,length);
    }

}
