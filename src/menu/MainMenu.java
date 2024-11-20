package menu;

import services.BookService;
import services.OrderService;
import java.util.Scanner;

import data_structures.NavigationStack;

public class MainMenu {
    private final AdminMenu adminMenu;
    private final CustomerMenu customerMenu;
    private final Scanner scanner;
    private final NavigationStack<Runnable> navigationStack;

    /**
     * Constructor that initializes member variables.
     *
     * @param bookService  An instance of the `BookService` class.
     * @param orderService An instance of the `OrderService` class.
     * @throws IllegalArgumentException If either `bookService` or `orderService` is
     *                                  null.
     */
    public MainMenu(BookService bookService, OrderService orderService) {
        if (bookService == null || orderService == null) {
            throw new IllegalArgumentException("BookService and OrderService cannot be null");
        }
        this.adminMenu = new AdminMenu(bookService, orderService, this);
        this.customerMenu = new CustomerMenu(bookService, orderService, this);
        this.scanner = new Scanner(System.in);
        this.navigationStack = new NavigationStack<>();
    }

    /**
     * Starts the main menu loop.
     */
    public void start() {
        showMainMenu();
    }

    /**
     * Displays the main menu and handles user interaction.
     */
    public void showMainMenu() {
        while (true) {
            System.out.println("\n===== Bookstore Management System =====");
            System.out.println("Please choose your role: ");
            System.out.println("1. Admin");
            System.out.println("2. Customer");
            System.out.println("3. Exit");
            System.out.print("Enter role: ");
            
            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    // Push the current menu display function onto the stack
                    // for back navigation from admin menu.
                    navigationStack.push(() -> showMainMenu());
                    adminMenu.show();
                    break;
                case 2:
                    // Push the current menu display function onto the stack
                    // for back navigation from customer menu.
                    navigationStack.push(() -> showMainMenu());
                    customerMenu.show();
                    break;
                case 3:
                    // Clear the navigation stack to remove any remaining entries.
                    while (!navigationStack.isEmpty()) {
                        navigationStack.pop(); // Clear stack by popping all items
                    }
                    System.out.println("Thank you for using our system!");
                    System.exit(0);
                default:
                    System.out.println("Invalid role. Please try again.");
            }
        }
    }

    /**
     * Provides access to the navigation stack.
     *
     * @return The `NavigationStack` instance used for navigation tracking.
     */
    public NavigationStack<Runnable> getNavigationStack() {
        return navigationStack;
    }
}