package data_structures;

public class BookEntry<E> {
    private E book;
    private int quantity;

    public BookEntry(E book, int quantity) {
        this.book = book;
        this.quantity = quantity;
    }

    public E getBook() { return book; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
