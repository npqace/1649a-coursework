package interfaces;

public interface IOrderQueue<E> {
    void offer(E element); // add element
    E poll(); // remove and return front element
    E peek(); // view front element
    int size(); // return size of queue
    boolean isEmpty(); // check if queue is empty
}
