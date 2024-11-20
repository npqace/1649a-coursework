package services;

import data_structures.InventoryItem;
import data_structures.OrderQueue;
import models.Book;
import models.Order;
import models.enums.OrderStatus;

// Manages order operations in the system
public class OrderService {
    private OrderQueue<Order> activeQueue; // Active orders queue
    private OrderQueue<Order> completedQueue; // Completed orders queue
    private BookService bookService; // Book management service

    // Initialize with BookService
    public OrderService(BookService bookService) {
        if (bookService == null) {
            throw new IllegalArgumentException("Book service cannot be null");
        }
        this.activeQueue = new OrderQueue<>();
        this.completedQueue = new OrderQueue<>();
        this.bookService = bookService;
    }

    // Create new order
    public Order createOrder(String customerName, String shippingAddress) {
        if (customerName == null || customerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name cannot be empty");
        }
        if (shippingAddress == null || shippingAddress.trim().isEmpty()) {
            throw new IllegalArgumentException("Shipping address cannot be empty");
        }
        return new Order(customerName.trim(), shippingAddress.trim());
    }

    // Add book to order if in stock
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

    // Find order in specified queue
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

            while (!tempQueue.isEmpty()) {
                queue.offer(tempQueue.poll());
            }
        } catch (Exception e) {
            System.out.println("Error finding order: " + e.getMessage());
        }
        return foundOrder;
    }

    // Search for order in both queues
    public Order findOrderById(int orderId) {
        if (orderId <= 0) {
            throw new IllegalArgumentException("Order ID must be positive");
        }

        Order order = findOrderInQueue(activeQueue, orderId);
        if (order == null) {
            order = findOrderInQueue(completedQueue, orderId);
        }
        return order;
    }

    // Submit order to queue
    public void submitOrder(Order order) {
        if (!isValidOrder(order)) {
            throw new IllegalArgumentException("Invalid order!");
        }

        try {
            if (!validateInventory(order)) {
                order.setStatus(OrderStatus.CANCELLED);
                completedQueue.offer(order);
            }

            updateInventoryStock(order);
            order.setStatus(OrderStatus.CONFIRMED);
            activeQueue.offer(order);
        } catch (Exception e) {
        }
    }

    // Process next order in queue
    public void processNextOrder() {
        if (activeQueue.isEmpty()) {
            System.out.println("No orders to process");
            return;
        }

        Order order = activeQueue.poll();
        if (order == null)
            return;

        System.out.println("\nProcessing Order:");
        displayOrder(order);

        switch (order.getStatus()) {
            case CONFIRMED:
                order.setStatus(OrderStatus.SHIPPING);
                System.out.println("Order #" + order.getOrderId() + " is now shipping");
                activeQueue.offer(order);
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

    // Move order to completed queue
    private void moveToCompleted(Order order) {
        activeQueue.poll();
        completedQueue.offer(order);
    }

    // Display all orders in both queues
    public void displayAllOrders() {
        System.out.println("\n=== Current Order Status ===");

        System.out.println("\nActive Orders:");
        if (activeQueue.isEmpty()) {
            System.out.println("No active orders");
        } else {
            System.out.println(Order.getTableHeader());
            Order current = activeQueue.peek();
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

    // Display active orders
    public void displayActiveOrders() {
        displayQueue(activeQueue, "Active");
    }

    // Display completed orders
    public void displayCompletedOrders() {
        displayQueue(completedQueue, "Completed");
    }

    // Display orders from specified queue
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
            current = current.next;
        }
    }

    // Update inventory after order processing
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

            bookService.updateStock(book.getBookID(), currentStock - orderedQuantity);
        }
    }

    // Check if inventory has sufficient stock
    private boolean validateInventory(Order order) {
        for (InventoryItem<Book> item : order.getBooks().getEntries()) {
            if (item.getBook().getQuantity() < item.getQuantity()) {
                return false;
            }
        }
        return true;
    }

    // Validate order details
    private boolean isValidOrder(Order order) {
        return order != null &&
                !order.getBooks().isEmpty() &&
                order.getTotalPrice() > 0 &&
                order.getCustomerName() != null &&
                order.getShippingAddress() != null;
    }

    // Display single order details
    public void displayOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        System.out.println(Order.getTableHeader());
        System.out.println(order.toString());
    }
}
