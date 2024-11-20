package data_structures;

// Generic inventory item class
public class InventoryItem<E> {
    // Item stored in inventory
    private E book;
    // Current stock quantity
    private int quantity;

    // Constructor
    public InventoryItem(E book, int quantity) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        this.book = book;
        this.quantity = quantity;
    }

    // Get stored item
    public E getBook() {
        return book;
    }

    // Get current quantity
    public int getQuantity() {
        return quantity;
    }

    // Set new quantity
    public void setQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        this.quantity = quantity;
    }
}
