package services;

import data_structures.InventoryItem;
import data_structures.OrderQueue;
import models.Book;
import models.Order;
import models.enums.OrderStatus;

public class OrderService {
    private OrderQueue<Order> orderQueue;
    private BookService bookService;

    public OrderService(BookService bookService) {
        if (bookService == null) {
            throw new IllegalArgumentException("Book service cannot be null");
        }
        this.orderQueue = new OrderQueue<>();
        this.bookService = bookService;
    }

    public Order createOrder(String customerName, String shippingAddress) {
        if (customerName == null || customerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name cannot be empty");
        }
        if (shippingAddress == null || shippingAddress.trim().isEmpty()) {
            throw new IllegalArgumentException("Shipping address cannot be empty");
        }
        return new Order(customerName.trim(), shippingAddress.trim());
    }

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

    public Order findOrderById(int orderId) {
        if (orderId <= 0) {
            throw new IllegalArgumentException("Order ID must be positive");
        }

        OrderQueue<Order> tempQueue = new OrderQueue<>();
        Order foundOrder = null;

        try {
            while (!orderQueue.isEmpty()) {
                Order order = orderQueue.poll();
                if (order.getOrderId() == orderId) {
                    foundOrder = order;
                }
                tempQueue.offer(order);
            }

            // Restore queue
            while (!tempQueue.isEmpty()) {
                orderQueue.offer(tempQueue.poll());
            }
        } catch (Exception e) {
            System.out.println("Error finding order: " + e.getMessage());
        }
        return foundOrder;
    }

    public void displayAllOrders() {
        if (orderQueue.isEmpty()) {
            System.out.println("No orders found");
            return;
        }

        OrderQueue<Order> tempQueue = new OrderQueue<>();
        System.out.println(Order.getTableHeader());

        try {
            // Display and preserve FIFO order
            while (!orderQueue.isEmpty()) {
                Order order = orderQueue.poll();
                System.out.println(order);
                tempQueue.offer(order);
            }

            // Restore queue
            while (!tempQueue.isEmpty()) {
                orderQueue.offer(tempQueue.poll());
            }
        } catch (Exception e) {
            System.out.println("Error displaying orders: " + e.getMessage());
        }
    }

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

            // Restore queue
            while (!tempQueue.isEmpty()) {
                orderQueue.offer(tempQueue.poll());
            }
        } catch (Exception e) {
            System.out.println("Error displaying active orders: " + e.getMessage());
        }
    }

    private boolean isActiveOrder(Order order) {
        return order.getStatus() != OrderStatus.DELIVERED &&
                order.getStatus() != OrderStatus.CANCELLED;
    }

    // Update submitOrder with enhanced validation
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

    public void processNextPendingOrder() {
        if (orderQueue.isEmpty()) {
            System.out.println("No orders in queue");
            return;
        }

        OrderQueue<Order> tempQueue = new OrderQueue<>();
        Order pendingOrder = null;

        try {
            // Find next pending order
            while (!orderQueue.isEmpty() && pendingOrder == null) {
                Order order = orderQueue.poll();
                if (order.getStatus() == OrderStatus.PENDING) {
                    pendingOrder = order;
                }
                tempQueue.offer(order);
            }

            // Process remaining orders
            while (!orderQueue.isEmpty()) {
                tempQueue.offer(orderQueue.poll());
            }

            // If found pending order, process it
            if (pendingOrder != null) {
                System.out.println("\nProcessing Order #" + pendingOrder.getOrderId());
                System.out.println("Order Details:");
                displayOrder(pendingOrder);

                boolean allInStock = true;
                String outOfStockBookTitle = "";

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
                    pendingOrder.setStatus(OrderStatus.CANCELLED);
                    System.out.println(
                            "Order #" + pendingOrder.getOrderId() + " has been canceled. " + outOfStockBookTitle
                                    + " is out of stock.");
                }
            } else {
                System.out.println("No pending orders found");
            }

            // Restore queue
            while (!tempQueue.isEmpty()) {
                orderQueue.offer(tempQueue.poll());
            }
        } catch (Exception e) {
            System.out.println("Error processing order: " + e.getMessage());
        }
    }

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

    public void displayOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        System.out.println(Order.getTableHeader());
        System.out.println(order.toString());
    }
}
