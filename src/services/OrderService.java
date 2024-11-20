package services;

import data_structures.InventoryItem;
import data_structures.OrderQueue;
import models.Book;
import models.Order;
import models.enums.OrderStatus;

/**
 * Service class for managing orders in the system.
 * Handles creating, updating, and processing orders.
 */
public class OrderService {
    private OrderQueue<Order> orderQueue; // Queue to manage orders in FIFO order
    private OrderQueue<Order> completedQueue; // Queue to store completed/cancelled orders
    private BookService bookService; // Service to handle book-related operations

    /**
     * Constructor to initialize the OrderService with a BookService instance.
     * 
     * @param bookService The service for book management.
     */
    public OrderService(BookService bookService) {
        if (bookService == null) {
            throw new IllegalArgumentException("Book service cannot be null");
        }
        this.orderQueue = new OrderQueue<>();
        this.completedQueue = new OrderQueue<>();
        this.bookService = bookService;
    }

    /**
     * Creates a new order with the provided customer details.
     * 
     * @param customerName    The customer's name.
     * @param shippingAddress The shipping address.
     * @return A new Order instance.
     */
    public Order createOrder(String customerName, String shippingAddress) {
        if (customerName == null || customerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name cannot be empty");
        }
        if (shippingAddress == null || shippingAddress.trim().isEmpty()) {
            throw new IllegalArgumentException("Shipping address cannot be empty");
        }
        return new Order(customerName.trim(), shippingAddress.trim());
    }

    /**
     * Adds a book to an order if available in stock.
     * 
     * @param order    The order to which the book will be added.
     * @param bookId   The ID of the book.
     * @param quantity The quantity of the book to add.
     * @return True if the book was successfully added, false otherwise.
     */
    public boolean addBookToOrder(Order order, int bookId, int quantity) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        if (bookId <= 0) {
            throw new IllegalArgumentException("Book ID must be positive");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        try {
            Book book = bookService.findBookById(bookId);
            if (book != null && bookService.isAvailable(bookId, quantity)) {
                order.addBook(book, quantity);
                return true;
            }
        } catch (Exception e) {
            System.out.println("Error adding book to order: " + e.getMessage());
        }
        return false;
    }

    /**
     * Finds an order in the queue by its ID.
     * 
     * @param orderId The ID of the order to find.
     * @return The found order, or null if not found.
     */
    private Order findOrderInQueue(OrderQueue<Order> queue, int orderId) {
        OrderQueue<Order> tempQueue = new OrderQueue<>();
        Order foundOrder = null;

        try {
            while (!queue.isEmpty()) {
                Order order = queue.poll();
                if (order.getOrderId() == orderId) {
                    foundOrder = order;
                }
                tempQueue.offer(order);
            }

            // Restore the queue
            while (!tempQueue.isEmpty()) {
                queue.offer(tempQueue.poll());
            }
        } catch (Exception e) {
            System.out.println("Error finding order: " + e.getMessage());
        }
        return foundOrder;
    }

    // Public method to search both queues
    public Order findOrderById(int orderId) {
        if (orderId <= 0) {
            throw new IllegalArgumentException("Order ID must be positive");
        }

        // Search active orders first
        Order order = findOrderInQueue(orderQueue, orderId);

        // If not found, search completed orders
        if (order == null) {
            order = findOrderInQueue(completedQueue, orderId);
        }

        return order;
    }

    /**
     * Submits an order to the queue after validation.
     * 
     * @param order The order to submit.
     */
    public void submitOrder(Order order) {
        if (!isValidOrder(order)) {
            throw new IllegalArgumentException("Invalid order!");
        }

        try {
            if (!validateInventory(order)) {
                order.setStatus(OrderStatus.CANCELLED);
                completedQueue.offer(order);
                // throw new IllegalStateException("Insufficient stock for one or more items");
            }

            // Update inventory and confirm order
            updateInventoryStock(order);
            order.setStatus(OrderStatus.CONFIRMED);
            orderQueue.offer(order);
        } catch (Exception e) {
            // System.out.println("Error submitting order: " + e.getMessage());
            // throw e;
        }
    }

    /**
     * Processes the next order in the queue.
     * If the order is valid and all items are in stock, it is confirmed.
     * Otherwise, the order is canceled.
     */
    public void processNextOrder() {
        if (orderQueue.isEmpty()) {
            System.out.println("No orders to process");
            return;
        }

        Order order = orderQueue.poll();
        if (order == null)
            return;

        // Display order details first
        System.out.println("\nProcessing Order:");
        displayOrder(order);

        switch (order.getStatus()) {
            case CONFIRMED:
                order.setStatus(OrderStatus.SHIPPING);
                System.out.println("Order #" + order.getOrderId() + " is now shipping");
                orderQueue.offer(order);
                break;
            case SHIPPING:
                order.setStatus(OrderStatus.DELIVERED);
                System.out.println("Order #" + order.getOrderId() + " is delivered");
                moveToCompleted(order);
                break;
            case DELIVERED:
            case CANCELLED:
                moveToCompleted(order);
                break;
        }
    }

    // private void processPendingOrder(Order order) {
    // if (validateInventory(order)) {
    // updateInventoryStock(order);
    // order.setStatus(OrderStatus.CONFIRMED);
    // System.out.println("Order #" + order.getOrderId() + " is confirmed");
    // } else {
    // order.setStatus(OrderStatus.CANCELLED);
    // System.out.println("Order #" + order.getOrderId() + " is cancelled -
    // insufficient stock");
    // moveToCompleted(order);
    // }
    // }

    private void moveToCompleted(Order order) {
        orderQueue.poll(); // Remove from active queue
        completedQueue.offer(order);
    }

    public void displayAllOrders() {
        System.out.println("\n=== Current Order Status ===");

        System.out.println("\nActive Orders:");
        if (orderQueue.isEmpty()) {
            System.out.println("No active orders");
        } else {
            System.out.println(Order.getTableHeader());
            Order current = orderQueue.peek();
            while (current != null) {
                System.out.println(current);
                current = current.next;
            }
        }

        System.out.println("\nCompleted Orders:");
        if (completedQueue.isEmpty()) {
            System.out.println("No completed orders");
        } else {
            System.out.println(Order.getTableHeader());
            Order current = completedQueue.peek();
            while (current != null) {
                System.out.println(current);
                current = current.next;
            }
        }
    }

    /**
     * Displays only active orders from the queue.
     * Active orders exclude those with statuses DELIVERED or CANCELLED.
     */
    public void displayActiveOrders() {
        displayQueue(orderQueue, "Active");
    }

    public void displayCompletedOrders() {
        displayQueue(completedQueue, "Completed");
    }

    private void displayQueue(OrderQueue<Order> queue, String type) {
        if (queue.isEmpty()) {
            System.out.println("No " + type.toLowerCase() + " orders");
            return;
        }

        System.out.println("\n=== " + type + " Orders ===");
        System.out.println(Order.getTableHeader());

        Order current = queue.peek();
        while (current != null) {
            System.out.println(current);
            current = current.next; // Need to add next pointer to Order class
        }
    }

    /**
     * Updates the inventory stock based on the items in the processed order.
     * 
     * @param order The order being processed.
     * @throws IllegalStateException If any item's stock is insufficient.
     */
    private void updateInventoryStock(Order order) {
        InventoryItem<Book>[] orderItems = order.getBooks().getEntries();

        for (int i = 0; i < orderItems.length; i++) {
            InventoryItem<Book> item = orderItems[i];
            Book book = item.getBook();

            int currentStock = book.getQuantity();
            int orderedQuantity = item.getQuantity();

            if (currentStock < orderedQuantity) {
                throw new IllegalStateException("Insufficient stock for book: " + book.getTitle());
            }

            // Update the stock for the book
            bookService.updateStock(book.getBookID(), currentStock - orderedQuantity);
        }
    }

    private boolean validateInventory(Order order) {
        for (InventoryItem<Book> item : order.getBooks().getEntries()) {
            if (item.getBook().getQuantity() < item.getQuantity()) {
                return false;
            }
        }
        return true;
    }

    private boolean isValidOrder(Order order) {
        return order != null &&
                !order.getBooks().isEmpty() &&
                order.getTotalPrice() > 0 &&
                order.getCustomerName() != null &&
                order.getShippingAddress() != null;
    }

    /**
     * Displays the details of a specific order.
     * 
     * @param order The order to display.
     */
    public void displayOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        System.out.println(Order.getTableHeader()); // Print the table header
        System.out.println(order.toString()); // Print the order details
    }
}
