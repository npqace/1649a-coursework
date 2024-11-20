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

    // Initialize menus, scanner and navigation stack
    public MainMenu(BookService bookService, OrderService orderService) {
        if (bookService == null || orderService == null) {
            throw new IllegalArgumentException("BookService and OrderService cannot be null");
        }
        this.adminMenu = new AdminMenu(bookService, orderService, this);
        this.customerMenu = new CustomerMenu(bookService, orderService, this);
        this.scanner = new Scanner(System.in);
        this.navigationStack = new NavigationStack<>();
    }

    // Start the main menu
    public void start() {
        showMainMenu();
    }

    // Display and handle main menu options
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
                    // Navigate to admin menu
                    navigationStack.push(() -> showMainMenu());
                    adminMenu.show();
                    break;
                case 2:
                    // Navigate to customer menu
                    navigationStack.push(() -> showMainMenu());
                    customerMenu.show();
                    break;
                case 3:
                    // Exit system
                    while (!navigationStack.isEmpty()) {
                        navigationStack.pop();
                    }
                    System.out.println("Thank you for using our system!");
                    System.exit(0);
                default:
                    System.out.println("Invalid role. Please try again.");
            }
        }
    }

    // Get navigation stack
    public NavigationStack<Runnable> getNavigationStack() {
        return navigationStack;
    }
}
