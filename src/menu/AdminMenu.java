package menu;

import java.util.Scanner;

import algorithms.SortAlgorithm.SortBy;
import models.Book;
import models.Order;
import models.enums.OrderStatus;
import services.BookService;
import services.OrderService;

public class AdminMenu {
    private final BookService bookService;
    private final OrderService orderService;
    private final Scanner scanner;
    private final MainMenu mainMenu;

    public AdminMenu(BookService bookService, OrderService orderService, MainMenu mainMenu) {
        this.bookService = bookService;
        this.orderService = orderService;
        this.scanner = new Scanner(System.in);
        this.mainMenu = mainMenu;
    }

    public void show() {
        while (true) {
            System.out.println("\n=== Admin Menu ===");
            System.out.println("1. Book Management");
            System.out.println("2. Order Management");
            System.out.println("3. Back to Main Menu");
            System.out.print("Enter your choice: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        mainMenu.getNavigationStack().push(() -> show());
                        showBookManagementMenu();
                        break;
                    case 2:
                        mainMenu.getNavigationStack().push(() -> show());
                        showOrderManagementMenu();
                        break;
                    case 3:
                        if (!mainMenu.getNavigationStack().isEmpty()) {
                            mainMenu.getNavigationStack().pop().run(); // Pop and return to main menu
                        }
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private void showBookManagementMenu() {
        while (true) {
            System.out.println("\n=== Book Management Menu ===");
            System.out.println("1. View all books");
            System.out.println("2. Add new book");
            System.out.println("3. Update book details");
            System.out.println("4. Update book stock");
            System.out.println("5. Remove book");
            System.out.println("6. Back to Admin Menu");
            System.out.print("Enter your choice: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        bookService.displayBooks(SortBy.ID);
                        waitForKeyPress();
                        break;
                    case 2:
                        addNewBook();
                        break;
                    case 3:
                        updateBookDetails();
                        break;
                    case 4:
                        updateBookStock();
                        break;
                    case 5:
                        removeBook();
                        break;
                    case 6:
                        if (!mainMenu.getNavigationStack().isEmpty()) {
                            mainMenu.getNavigationStack().pop().run(); // Pop and return to admin menu
                        }
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private void showOrderManagementMenu() {
        while (true) {
            System.out.println("\n=== Order Management Menu ===");
            System.out.println("1. View all orders");
            System.out.println("2. View active orders");
            System.out.println("3. Process pending orders");
            System.out.println("4. Update order status");
            System.out.println("5. Back to Admin Menu");
            System.out.print("Enter your choice: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        orderService.displayAllOrders();
                        waitForKeyPress();
                        break;
                    case 2:
                        orderService.displayActiveOrders();
                        waitForKeyPress();
                        break;
                    case 3:
                        orderService.processNextPendingOrder();
                        waitForKeyPress();
                        break;
                    case 4:
                        updateOrderStatus();
                        break;
                    case 5:
                        if (!mainMenu.getNavigationStack().isEmpty()) {
                            mainMenu.getNavigationStack().pop().run(); // Pop and return to admin menu
                        }
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private void addNewBook() {
        try {
            System.out.print("Enter book title: ");
            String title = scanner.nextLine();
            System.out.print("Enter author name: ");
            String author = scanner.nextLine();
            System.out.print("Enter price: ");
            double price = Double.parseDouble(scanner.nextLine());
            System.out.print("Enter quantity: ");
            int quantity = Integer.parseInt(scanner.nextLine());

            bookService.addBook(title, author, price, quantity);
            System.out.println("Book added successfully!");
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter valid numbers for price and quantity.");
        } catch (Exception e) {
            System.out.println("Failed to add book: " + e.getMessage());
        }
    }

    private void updateBookDetails() {
        try {
            System.out.print("Enter book ID: ");
            int bookId = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter new title (or press Enter to skip): ");
            String title = scanner.nextLine();
            System.out.print("Enter new author (or press Enter to skip): ");
            String author = scanner.nextLine();
            System.out.print("Enter new price (or 0 to skip): ");
            double price = Double.parseDouble(scanner.nextLine());

            Book book = bookService.findBookById(bookId);
            if (book != null) {
                if (!title.isEmpty())
                    book.setTitle(title);
                if (!author.isEmpty())
                    book.setAuthor(author);
                if (price > 0)
                    book.setPrice(price);
                System.out.println("Book details updated successfully");
            } else {
                System.out.println("Book not found");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter valid numbers for book ID and price.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void updateBookStock() {
        try {
            System.out.print("Enter book ID: ");
            int bookId = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter new quantity: ");
            int quantity = Integer.parseInt(scanner.nextLine());

            if (bookService.updateStock(bookId, quantity)) {
                System.out.println("Stock updated successfully");
            } else {
                System.out.println("Book not found");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter valid numbers for book ID and quantity.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void removeBook() {
        try {
            System.out.print("Enter book ID: ");
            int bookId = Integer.parseInt(scanner.nextLine());

            if (bookService.removeBook(bookId)) {
                System.out.println("Book removed successfully");
            } else {
                System.out.println("Book not found");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number for book ID.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void updateOrderStatus() {
        System.out.println("\n=== Update Order Status ===");
        orderService.displayActiveOrders();

        try {
            System.out.print("\nEnter order ID to update (0 to cancel): ");
            int orderId = Integer.parseInt(scanner.nextLine());
            if (orderId == 0)
                return;

            Order order = orderService.findOrderById(orderId);
            if (order == null || !isActiveOrder(order)) {
                System.out.println("Invalid order ID");
                return;
            }

            System.out.println("\nAvailable statuses:");
            System.out.println("1. SHIPPING");
            System.out.println("2. DELIVERED");
            System.out.print("Enter new status number(1-2): ");

            int statusChoice = Integer.parseInt(scanner.nextLine());
            OrderStatus newStatus = switch (statusChoice) {
                case 1 -> OrderStatus.SHIPPING;
                case 2 -> OrderStatus.DELIVERED;
                default -> throw new IllegalArgumentException("Invalid status choice");
            };

            orderService.updateOrderStatus(order, newStatus);
            System.out.println("Order status updated successfully");
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter valid numbers for order ID and status choice.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private boolean isActiveOrder(Order order) {
        return order.getStatus() != OrderStatus.DELIVERED &&
                order.getStatus() != OrderStatus.CANCELLED;
    }

    private void waitForKeyPress() {
        System.out.println("Press any key to continue...");
        scanner.nextLine();
    }
}
