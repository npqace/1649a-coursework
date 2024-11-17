package data_structures;

/**
 * This class represents an item in an inventory system.
 * It can hold any type of object (`E`) as the item itself and keeps track of
 * its quantity.
 * 
 * @param <E> The type of object this InventoryItem holds.
 */
public class InventoryItem<E> {
    /**
     * The actual item stored in this inventory item.
     */
    private E book;
    /**
     * The quantity of the item currently in stock.
     */
    private int quantity;

    /**
     * Constructor for InventoryItem.
     * 
     * @param book     The item to be stored.
     * @param quantity The initial quantity of the item in stock.
     * @throws IllegalArgumentException if the book is null or the quantity is
     *                                  negative.
     */
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

    /**
     * Getter method to access the item stored in this InventoryItem.
     * 
     * @return The item object.
     */
    public E getBook() {
        return book;
    }

    /**
     * Getter method to access the current quantity of the item in stock.
     * 
     * @return The quantity of the item.
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Setter method to update the quantity of the item in stock.
     * 
     * @param quantity The new quantity of the item.
     * @throws IllegalArgumentException if the new quantity is negative.
     */
    public void setQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        this.quantity = quantity;
    }
}