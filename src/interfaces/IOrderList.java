package interfaces;

public interface IOrderList<E> {
    boolean add(E element);
    E get(int index);
    E remove(int index);
    int indexOf(E element);
    boolean contains(E element);
    int size();
    boolean isEmpty();
}
