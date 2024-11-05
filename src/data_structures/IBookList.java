package data_structures;

public interface IBookList<E> {
    boolean add(E book, int quantity);
    BookEntry<E>[] getEntries();
    int size();
    boolean isEmpty();
    void remove(E book);
    BookEntry<E> removeAtIndex(int index);
    int getQuantities(E book);
    int indexOf(E book);
    boolean contains(E book);
}
