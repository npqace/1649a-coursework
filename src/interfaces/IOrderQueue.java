package interfaces;

public interface IOrderQueue<E> {
    void offer(E element);
    E poll();
    E peek();
    int size();
    boolean isEmpty();
}
