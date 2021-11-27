package pt.pa.model;

import java.util.Iterator;

public interface Matrix<E> {

    void put(int x, int y, E elem);
    E get(int x, int y);
    int width();
    int height();
    void clear();

    int size();

    boolean contains(E elem);
    boolean isEmpty();

}
