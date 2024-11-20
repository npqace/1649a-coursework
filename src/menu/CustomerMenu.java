package menu;

import services.BookService;
import services.OrderService;
import algorithms.SortAlgorithm.SortBy;
import models.Book;
import models.Order;

import java.util.Scanner;

// Customer interface for book store operations
public class CustomerMenu {
    private final BookService bookService;
    private final OrderService orderService;
    private final Scanner scanner;
    private final MainMenu mainMenu;

    public CustomerMenu(BookService bookService, OrderService orderService, MainMenu mainMenu) {
        this.bookService = bookService;
        this.orderService = orderService;
        this.scanner = new Scanner(System.in);
        this.mainMenu = mainMenu;
    }

    // Display menu and handle user input
    public void show() {
        while (true) {
            System.out.println("\n=== Customer Menu ===");
            System.out.println("1. View all books");
            System.out.println("2. Search book");
            System.out.println("3. Sort book");
            System.out.println("4. Place order");
            System.out.println("5. Track order");
            System.out.println("6. Back");
            System.out.print("Enter choice: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    bookService.displayBooks(SortBy.ID);
                    waitForKeyPress();
                    break;
                case "2":
                    searchBooks();
                    break;
                case "3":
                    sortBooks();
                    break;
                case "4":
                    placeOrder();
                    break;
                case "5":
                    trackOrder();
                    break;
                case "6":
                    if (!mainMenu.getNavigationStack().isEmpty()) {
                        mainMenu.getNavigationStack().pop().run();
                    }
                    return;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    // Search books by title
    private void searchBooks() {
        System.out.print("Enter search term: ");
        String term = scanner.nextLine();

        try {
            Book[] results = bookService.findBooksByTitle(term);
            if (results.length == 0) {
                System.out.println("No books found");
            } else {
                System.out.println(Book.getTableHeader());
                for (Book book : results) {
                    System.out.println(book);
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
        waitForKeyPress();
    }

    // Sort books by title or price
    private void sortBooks() {
        System.out.println("\nSort by:");
        System.out.println("1. Title");
        System.out.println("2. Price");
        System.out.print("Enter choice: ");

        String sortChoice = scanner.nextLine();
        switch (sortChoice) {
            case "1":
                bookService.displayBooks(SortBy.TITLE);
                break;
            case "2":
                bookService.displayBooks(SortBy.PRICE);
                break;
            default:
                System.out.println("Invalid sort option");
        }
        waitForKeyPress();
    }

    // Create and submit new order
    private void placeOrder() {
        try {
            System.out.print("Enter your name: ");
            String name = scanner.nextLine();
            System.out.print("Enter your address: ");
            String address = scanner.nextLine();

            Order order = orderService.createOrder(name, address);
            bookService.displayBooks(SortBy.ID);

            while (true) {
                System.out.print("Enter book ID (0 to finish): ");
                int bookId = Integer.parseInt(scanner.nextLine());
                if (bookId == 0)
                    break;

                System.out.print("Enter quantity: ");
                int quantity = Integer.parseInt(scanner.nextLine());

                if (orderService.addBookToOrder(order, bookId, quantity)) {
                    System.out.println("Book added to order");
                } else {
                    System.out.println("Failed to add book (not found or insufficient stock)");
                }
            }

            if (!order.getBooks().isEmpty()) {
                orderService.submitOrder(order);
                System.out.println("Order submitted successfully!");
                System.out.println("Your order ID is: " + order.getOrderId());
            } else {
                System.out.println("Order cancelled - no books added");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter valid numbers for book ID and quantity.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        waitForKeyPress();
    }

    // Track order by ID
    private void trackOrder() {
        System.out.print("Enter order ID: ");
        try {
            int orderId = Integer.parseInt(scanner.nextLine());
            Order order = orderService.findOrderById(orderId);
            
            if (order != null) {
                System.out.println("\nOrder Details:");
                orderService.displayOrder(order);
            } else {
                System.out.println("Order not found");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid order ID format");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
        waitForKeyPress();
    }

    // Pause execution until user input
    private void waitForKeyPress() {
        System.out.println("Press any key to continue...");
        scanner.nextLine();
    }
}
