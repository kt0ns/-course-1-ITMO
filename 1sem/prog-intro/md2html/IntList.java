package md2html;

import java.util.Arrays;

public class IntList {
    private int[] array;
    private int pos = 0;
    private int len;

    public IntList(int size) {
        this.array = new int[size];
        this.len = size;
    }

    public IntList() {
        this.array = new int[8];
        this.len = 8;
    }

    public void add(int i) {
        if (pos == len - 1) {
            resize();
        }
        array[pos] = i;
        pos++;
    }

    public boolean isEmpty() {
        return pos == 0;
    }

    public int size() {
        return pos;
    }

    public void clear() {
        array = new int[8];
        pos = 0;
    }

    public String toString() {
        if (pos == 0)
            return "";
        int iMax = pos - 1;

        StringBuilder b = new StringBuilder();
        for (int i = 0; ; i++) {
            b.append(array[i]);
            if (i == iMax)
                return b.toString();
            b.append(" ");
        }
    }

    public String toStringPairs() {
        if (pos == 0)
            return "";
        int iMax = pos - 2;

        StringBuilder b = new StringBuilder();
        for (int i = 0; ; i += 2) {
            b.append(array[i]);
            b.append(":");
            b.append(array[i+1]);
            if (i == iMax) {
                return b.toString();
            }
            b.append(" ");
        }
    }
    public int get(int i) {
        return array[i];
    }
    private void resize() {
        len = array.length * 2;
        array = Arrays.copyOf(array, len);
    }
}
