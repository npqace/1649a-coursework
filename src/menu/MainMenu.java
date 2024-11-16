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

    public MainMenu(BookService bookService, OrderService orderService) {
        if (bookService == null || orderService == null) {
            throw new IllegalArgumentException("BookService and OrderService cannot be null");
        }
        this.adminMenu = new AdminMenu(bookService, orderService, this);
        this.customerMenu = new CustomerMenu(bookService, orderService, this);
        this.scanner = new Scanner(System.in);
        this.navigationStack = new NavigationStack<>();
    }

    public void start() {
        showMainMenu();
    }

    public void showMainMenu() {
        while (true) {
            System.out.println("\n===== Bookstore Management System =====");
            System.out.print("Please choose your role (admin/customer/exit): ");
            String role = scanner.nextLine().toLowerCase().trim();

            switch (role) {
                case "admin":
                    navigationStack.push(() -> showMainMenu());
                    adminMenu.show();
                    break;
                case "customer":
                    navigationStack.push(() -> showMainMenu());
                    customerMenu.show();
                    break;
                case "exit":
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

    public NavigationStack<Runnable> getNavigationStack() {
        return navigationStack;
    }
}