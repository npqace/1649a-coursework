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
    public Order findOrderById(int orderId) {
        if (orderId <= 0) {
            throw new IllegalArgumentException("Order ID must be positive");
        }

        OrderQueue<Order> tempQueue = new OrderQueue<>();
        Order foundOrder = null;

        try {
            // Search through the queue while preserving its order
            while (!orderQueue.isEmpty()) {
                Order order = orderQueue.poll();
                if (order.getOrderId() == orderId) {
                    foundOrder = order;
                }
                tempQueue.offer(order);
            }

            // Restore the queue after the search
            while (!tempQueue.isEmpty()) {
                orderQueue.offer(tempQueue.poll());
            }
        } catch (Exception e) {
            System.out.println("Error finding order: " + e.getMessage());
        }
        return foundOrder;
    }

    /**
     * Displays all orders currently in the queue.
     */
    public void displayAllOrders() {
        if (orderQueue.isEmpty()) {
            System.out.println("No orders found");
            return;
        }

        OrderQueue<Order> tempQueue = new OrderQueue<>();
        System.out.println(Order.getTableHeader()); // Print table header

        try {
            // Print and temporarily remove orders from the queue
            while (!orderQueue.isEmpty()) {
                Order order = orderQueue.poll();
                System.out.println(order);
                tempQueue.offer(order);
            }

            // Restore the queue
            while (!tempQueue.isEmpty()) {
                orderQueue.offer(tempQueue.poll());
            }
        } catch (Exception e) {
            System.out.println("Error displaying orders: " + e.getMessage());
        }
    }

    /**
     * Displays only active orders from the queue.
     * Active orders exclude those with statuses DELIVERED or CANCELLED.
     */
    public void displayActiveOrders() {
        if (orderQueue.isEmpty()) {
            System.out.println("No active orders");
            return;
        }

        OrderQueue<Order> tempQueue = new OrderQueue<>();
        System.out.println(Order.getTableHeader());

        try {
            while (!orderQueue.isEmpty()) {
                Order order = orderQueue.poll();
                if (isActiveOrder(order)) {
                    System.out.println(order);
                }
                tempQueue.offer(order);
            }

            // Restore the queue
            while (!tempQueue.isEmpty()) {
                orderQueue.offer(tempQueue.poll());
            }
        } catch (Exception e) {
            System.out.println("Error displaying active orders: " + e.getMessage());
        }
    }

    /**
     * Checks if an order is active based on its status.
     * 
     * @param order The order to check.
     * @return True if the order is active, false otherwise.
     */
    private boolean isActiveOrder(Order order) {
        return order.getStatus() != OrderStatus.DELIVERED &&
                order.getStatus() != OrderStatus.CANCELLED;
    }

    /**
     * Submits an order to the queue after validation.
     * 
     * @param order The order to submit.
     */
    public void submitOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        if (order.getBooks().isEmpty()) {
            throw new IllegalArgumentException("Cannot submit empty order");
        }
        if (order.getTotalPrice() <= 0) {
            throw new IllegalArgumentException("Order total price must be positive");
        }
        if (order.getCustomerName() == null || order.getShippingAddress() == null) {
            throw new IllegalArgumentException("Order must have valid customer information");
        }
        try {
            orderQueue.offer(order);
        } catch (Exception e) {
            System.out.println("Error submitting order: " + e.getMessage());
        }
    }

    /**
     * Processes the next pending order in the queue.
     * If the order is valid and all items are in stock, it is confirmed.
     * Otherwise, the order is canceled.
     */
    public void processNextPendingOrder() {
        if (orderQueue.isEmpty()) {
            System.out.println("No orders in queue");
            return;
        }

        OrderQueue<Order> tempQueue = new OrderQueue<>();
        Order pendingOrder = null;

        try {
            // Find the next order with PENDING status
            while (!orderQueue.isEmpty() && pendingOrder == null) {
                Order order = orderQueue.poll();
                if (order.getStatus() == OrderStatus.PENDING) {
                    pendingOrder = order;
                }
                tempQueue.offer(order);
            }

            // Process the remaining orders in the queue
            while (!orderQueue.isEmpty()) {
                tempQueue.offer(orderQueue.poll());
            }

            // Process the pending order if found
            if (pendingOrder != null) {
                System.out.println("\nProcessing Order #" + pendingOrder.getOrderId());
                System.out.println("Order Details:");
                displayOrder(pendingOrder);

                boolean allInStock = true;
                String outOfStockBookTitle = "";

                // Check if all items in the order are in stock
                InventoryItem<Book>[] items = pendingOrder.getBooks().getEntries();
                for (int i = 0; i < items.length; i++) {
                    InventoryItem<Book> item = items[i];
                    Book book = item.getBook();
                    int requiredQuantity = item.getQuantity();
                    if (book.getQuantity() < requiredQuantity) {
                        allInStock = false;
                        outOfStockBookTitle = book.getTitle();
                        break;
                    }
                }

                // If all items are in stock, confirm the order and update inventory
                if (allInStock) {
                    try {
                        updateInventoryStock(pendingOrder);
                        pendingOrder.setStatus(OrderStatus.CONFIRMED);
                        System.out.println("Order #" + pendingOrder.getOrderId() + " has been confirmed");
                    } catch (IllegalStateException e) {
                        pendingOrder.setStatus(OrderStatus.CANCELLED);
                        System.out.println("Order is canceled. " + e.getMessage());
                    }
                } else {
                    // Cancel the order if any item is out of stock
                    pendingOrder.setStatus(OrderStatus.CANCELLED);
                    System.out.println(
                            "Order #" + pendingOrder.getOrderId() + " has been canceled. " + outOfStockBookTitle
                                    + " is out of stock.");
                }
            } else {
                System.out.println("No pending orders found");
            }

            // Restore the queue after processing
            while (!tempQueue.isEmpty()) {
                orderQueue.offer(tempQueue.poll());
            }
        } catch (Exception e) {
            System.out.println("Error processing order: " + e.getMessage());
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

    /**
     * Updates the status of an order if the transition is valid.
     * 
     * @param order     The order whose status needs to be updated.
     * @param newStatus The new status to set.
     */
    public void updateOrderStatus(Order order, OrderStatus newStatus) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        if (newStatus == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }

        OrderStatus currentStatus = order.getStatus();
        if (isValidStatusTransition(currentStatus, newStatus)) {
            order.setStatus(newStatus);
        } else {
            throw new IllegalStateException("Invalid status transition from " +
                    currentStatus + " to " + newStatus);
        }
    }

    /**
     * Validates whether the status transition for an order is allowed.
     * 
     * @param current The current status of the order.
     * @param next    The desired new status.
     * @return True if the transition is valid, false otherwise.
     */
    private boolean isValidStatusTransition(OrderStatus current, OrderStatus next) {
        switch (current) {
            case PENDING:
                return next == OrderStatus.CONFIRMED || next == OrderStatus.CANCELLED;
            case CONFIRMED:
                return next == OrderStatus.SHIPPING;
            case SHIPPING:
                return next == OrderStatus.DELIVERED;
            default:
                return false;
        }
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
