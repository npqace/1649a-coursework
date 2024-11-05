package data_structures;

public interface IList<E> {
    boolean add(E element);
    E get(int index);
    E remove(int index);
    int indexOf(E element);
    boolean contains(E element);
    int size();
    boolean isEmpty();
}
