package interfaces;

import data_structures.InventoryItem;

public interface IInventoryList<E> {
    boolean add(E book, int quantity);
    InventoryItem<E>[] getEntries();
    int size();
    boolean isEmpty();
    void remove(E book);
    int getQuantities(E book);
    int indexOf(E book);
    boolean contains(E book);
}
