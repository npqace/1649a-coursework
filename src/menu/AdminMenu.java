package menu;

import java.util.Scanner;

import algorithms.SortAlgorithm.SortBy;
import models.Book;
import models.Order;
import models.enums.OrderStatus;
import services.BookService;
import services.OrderService;

public class AdminMenu {
    private final BookService bookService; // Service for interacting with books
    private final OrderService orderService; // Service for interacting with orders
    private final Scanner scanner; // Scanner object for user input
    private final MainMenu mainMenu; // Reference to the main menu

    /**
     * Constructor to initialize the AdminMenu with necessary services and
     * references.
     *
     * @param bookService  The BookService object
     * @param orderService The OrderService object
     * @param mainMenu     The MainMenu object
     */
    public AdminMenu(BookService bookService, OrderService orderService, MainMenu mainMenu) {
        this.bookService = bookService;
        this.orderService = orderService;
        this.scanner = new Scanner(System.in);
        this.mainMenu = mainMenu;
    }

    /**
     * Displays the Admin Menu and handles user interactions.
     * This method loops until the user chooses to go back to the main menu.
     */
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
                        // Push the current method call onto the navigation stack before entering Book
                        // Management
                        mainMenu.getNavigationStack().push(() -> show());
                        showBookManagementMenu();
                        break;
                    case 2:
                        // Push the current method call onto the navigation stack before entering Order
                        // Management
                        mainMenu.getNavigationStack().push(() -> show());
                        showOrderManagementMenu();
                        break;
                    case 3:
                        if (!mainMenu.getNavigationStack().isEmpty()) {
                            // Pop the top element from the navigation stack and call its run method (back
                            // to main menu)
                            mainMenu.getNavigationStack().pop().run();
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

    /**
     * Displays the Book Management Menu and handles user interactions.
     */
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
                        // Display all books sorted by ID
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
                            // Pop the top element from the navigation stack and call its run method (back
                            // to admin menu)
                            mainMenu.getNavigationStack().pop().run();
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

    /**
     * Displays the Order Management Menu and handles user interactions related to
     * order management.
     * This method loops continuously until the user chooses to go back to the Admin
     * Menu.
     */
    private void showOrderManagementMenu() {
        while (true) {
            System.out.println("\n=== Order Management Menu ===");
            System.out.println("1. View all orders in queue");
            System.out.println("2. Process next order");
            System.out.println("3. View order details by ID");
            System.out.println("4. View active orders");
            System.out.println("5. View completed orders");
            System.out.println("6. Back to Admin Menu");
            System.out.print("Enter your choice: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        // Display all orders using the orderService
                        orderService.displayAllOrders();
                        waitForKeyPress();
                        break;
                    case 2:
                        orderService.processNextOrder();
                        waitForKeyPress();
                        break;
                    case 3:
                        viewOrderDetails();
                        break;
                    case 4:
                        orderService.displayActiveOrders();
                        waitForKeyPress();
                        break;
                    case 5:
                        orderService.displayCompletedOrders();
                        waitForKeyPress();
                        break;
                    case 6:
                        // Check if there's a previous menu to return to
                        if (!mainMenu.getNavigationStack().isEmpty()) {
                            // Pop the top element from the navigation stack and call its run method (back
                            // to admin menu)
                            mainMenu.getNavigationStack().pop().run();
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

    /**
     * Prompts the user to enter details for a new book and adds it to the
     * inventory.
     *
     * This method handles user input, validates it, and calls the `BookService` to
     * add the new book.
     * It also handles potential exceptions like invalid input or errors during book
     * addition.
     */
    private void addNewBook() {
        try {
            // Prompt the user for book details
            System.out.print("Enter book title: ");
            String title = scanner.nextLine();
            System.out.print("Enter author name: ");
            String author = scanner.nextLine();
            System.out.print("Enter price: ");
            double price = Double.parseDouble(scanner.nextLine());
            System.out.print("Enter quantity: ");
            int quantity = Integer.parseInt(scanner.nextLine());

            // Add the new book to the inventory using the BookService
            bookService.addBook(title, author, price, quantity);
            System.out.println("Book added successfully!");
        } catch (NumberFormatException e) {
            // Handle invalid input for price or quantity
            System.out.println("Invalid input. Please enter valid numbers for price and quantity.");
        } catch (Exception e) {
            // Handle general exceptions during book addition
            System.out.println("Failed to add book: " + e.getMessage());
        }
        waitForKeyPress();
    }

    /**
     * Updates the details of an existing book.
     *
     * This method prompts the user to enter the book ID and new details (title,
     * author, and price).
     * It then retrieves the book using the `BookService` and updates its properties
     * if necessary.
     * Error handling is included to catch invalid input and potential exceptions
     * during the update process.
     */
    private void updateBookDetails() {
        try {
            // Prompt the user for the book ID and new details
            System.out.print("Enter book ID: ");
            int bookId = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter new title (or press Enter to skip): ");
            String title = scanner.nextLine();
            System.out.print("Enter new author (or press Enter to skip): ");
            String author = scanner.nextLine();
            System.out.print("Enter new price (or 0 to skip): ");
            double price = Double.parseDouble(scanner.nextLine());

            // Find the book by ID
            Book book = bookService.findBookById(bookId);
            if (book != null) {
                // Update the book's details if necessary
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
            // Handle invalid input for book ID or price
            System.out.println("Invalid input. Please enter valid numbers for book ID and price.");
        } catch (IllegalArgumentException e) {
            // Handle other potential exceptions during book update
            System.out.println("Error: " + e.getMessage());
        }
        waitForKeyPress();
    }

    /**
     * Updates the stock quantity of an existing book.
     * 
     * This method prompts the user to enter the book ID and new quantity.
     * It then calls the `BookService` to update the book's stock.
     * Error handling is included to catch invalid input and potential exceptions
     * during the update process.
     */
    private void updateBookStock() {
        try {
            // Prompt the user for the book ID and new quantity
            System.out.print("Enter book ID: ");
            int bookId = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter new quantity: ");
            int quantity = Integer.parseInt(scanner.nextLine());

            // Update the book's stock using the BookService
            if (bookService.updateStock(bookId, quantity)) {
                System.out.println("Stock updated successfully");
            } else {
                System.out.println("Book not found");
            }
        } catch (NumberFormatException e) {
            // Handle invalid input for book ID or quantity
            System.out.println("Invalid input. Please enter valid numbers for book ID and quantity.");
        } catch (IllegalArgumentException e) {
            // Handle other potential exceptions during stock update
            System.out.println("Error: " + e.getMessage());
        }
        waitForKeyPress();
    }

    /**
     * Removes a book from the inventory.
     * 
     * This method prompts the user to enter the book ID and calls the `BookService`
     * to remove the book.
     * Error handling is included to catch invalid input and potential exceptions
     * during the removal process.
     */
    private void removeBook() {
        try {
            // Prompt the user for the book ID
            System.out.print("Enter book ID: ");
            int bookId = Integer.parseInt(scanner.nextLine());

            // Remove the book using the BookService
            if (bookService.removeBook(bookId)) {
                System.out.println("Book removed successfully");
            } else {
                System.out.println("Book not found");
            }
        } catch (NumberFormatException e) {
            // Handle invalid input for book ID
            System.out.println("Invalid input. Please enter a valid number for book ID.");
        } catch (IllegalArgumentException e) {
            // Handle other potential exceptions during book removal
            System.out.println("Error: " + e.getMessage());
        }
        waitForKeyPress();
    }

    private void viewOrderDetails() {
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

    /**
     * Pauses the program execution until the user presses any key.
     * 
     * This method is used to keep the console window open after displaying
     * information.
     */
    private void waitForKeyPress() {
        System.out.println("Press any key to continue...");
        scanner.nextLine();
    }
}
