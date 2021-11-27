package pt.pa.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class MatrixHashMap<E> implements Matrix<E>, Iterable<E> {

    private HashMap<IntTuple, E> contents;

    public MatrixHashMap() {
        clear();
    }

    @Override
    public Iterator<E> iterator() {
        return new MatrixHashMapIterator();
    }

    @Override
    public void put(int x, int y, E elem) {
        if (x < 0 || y < 0) throw new IllegalArgumentException("Positions must be 0 or above!");

        contents.put(new IntTuple(x, y), elem);
    }

    @Override
    public E get(int x, int y) {
        if (x < 0 || y < 0)
            throw new IllegalArgumentException("Positions must be 0 or above!");
        if (!contents.containsKey(new IntTuple(x, y)))
            return null;

        E elem = contents.get(new IntTuple(x, y));

        if (elem == null)
            throw new NoSuchElementException("The intented element is null!");

        return elem;
    }

    private IntTuple positionOfElement(E elem) {
        for (IntTuple it: contents.keySet()) {
            if (contents.get(it).equals(elem)) return it;
        }
        return null;
    }

    @Override
    public int width() {
        int w = 0;

        for(IntTuple it: contents.keySet()) {
            if (it.x > w) w = it.x;
        }

        return w + 1;
    }

    @Override
    public int height() {
        int h = 0;

        for(IntTuple it: contents.keySet()) {
            if (it.y > h) h = it.y;
        }

        return h + 1;
    }

    @Override
    public void clear() {
        contents = new HashMap<>();
    }

    @Override
    public int size() {
        return contents.size();
    }

    @Override
    public boolean contains(E elem) {
        return contents.containsValue(elem);
    }

    @Override
    public boolean isEmpty() {
        return contents.isEmpty();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder("");

        for (E elem: this) {
            IntTuple pos = positionOfElement(elem);
            sb.append("Position " + pos.x + ", " + pos.y + ": " + elem + "\n");
        }

        return sb.substring(0, sb.lastIndexOf("\n"));
    }

    private class IntTuple {
        private int x, y;

        public IntTuple(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            IntTuple intTuple = (IntTuple) o;
            return x == intTuple.x && y == intTuple.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    private class MatrixHashMapIterator implements Iterator<E> {

        private int x, y, maxX, maxY, matSize, elemCounter;

        public MatrixHashMapIterator() {
            this.x = 0;
            this.y = 0;
            this.maxX = width() - 1;
            this.maxY = height() - 1;
            this.elemCounter = 0;
            this.matSize = contents.size();
        }

        @Override
        public boolean hasNext() {
            return elemCounter < matSize;
        }

        @Override
        public E next() {
            if (!hasNext()) throw new NoSuchElementException("No further element exists!");

            boolean found = false;
            IntTuple pos = new IntTuple(x, y);

            System.out.println("" + x + ", " + y);

            while (!found) {
                if (!found) {
                    x++;
                    if (x > maxX) {
                        x = 0;
                        y++;
                    }
                    pos = new IntTuple(x, y);
                }
                found = contents.containsKey(pos);
            }

            elemCounter++;

            return contents.get(pos);
        }
    }
}
