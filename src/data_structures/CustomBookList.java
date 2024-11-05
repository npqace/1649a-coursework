package data_structures;

import java.util.Arrays;

public class CustomBookList<E> implements IBookList<E> {
    private BookEntry<E>[] bookEntries;
    private int size;
    private static final int DEFAULT_CAPACITY = 10;

    @SuppressWarnings("unchecked")
    public CustomBookList() {
        this.bookEntries = new BookEntry[DEFAULT_CAPACITY];
        this.size = 0;
    }

    @Override
    public boolean add(E book, int quantity) {
        for (int i = 0; i < this.size; i++) {
            if (bookEntries[i].getBook().equals(book)) {
                bookEntries[i].setQuantity(quantity);
                return true;
            }
        }
        if (size == bookEntries.length) {
            bookEntries = grow();
        }
        bookEntries[size] = new BookEntry<>(book, quantity);
        size++;
        return true;
    }

    private BookEntry<E>[] grow() {
        return Arrays.copyOf(this.bookEntries, this.bookEntries.length * 2);
    }

    @Override
    public BookEntry<E>[] getEntries() {
        return Arrays.copyOf(bookEntries, size);
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public void remove(E book) {
        int removedEntry = indexOf(book);
        if (removedEntry != -1) {
            for (int i = removedEntry; i < this.size - 1; i++) {
                bookEntries[i] = bookEntries[i + 1];
            }
            this.size--;
            bookEntries[this.size] = null;
        }
    }

    // Add remove by index method if needed
    public BookEntry<E> removeAtIndex(int index) {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        BookEntry<E> removedEntry = bookEntries[index];
        for (int i = index; i < this.size - 1; i++) {
            bookEntries[i] = bookEntries[i + 1];
        }
        this.size--;
        bookEntries[this.size] = null;
        return removedEntry;
    }

    @Override
    public int getQuantities(E book) {
        for (int i = 0; i < this.size; i++) {
            if (bookEntries[i].getBook().equals(book)) {
                return bookEntries[i].getQuantity();
            }
        }
        return 0;
    }

    @Override
    public int indexOf(E book) {
        for (int i = 0; i < this.size; i++) {
            if (bookEntries[i].getBook().equals(book)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean contains(E book) {
        return indexOf(book) != -1;
    }

}
