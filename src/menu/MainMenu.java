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
        navigationStack.push(this::showMainMenu);
        showMainMenu();
    }

    public void showMainMenu() {
        while (true) {
            System.out.println("\n===== Bookstore Management System =====");
            System.out.print("Please choose your role (admin/customer/exit): ");
            String role = scanner.nextLine().toLowerCase().trim();

            switch (role) {
                case "admin":
                    navigationStack.push(this::showMainMenu);
                    adminMenu.show();
                    break;
                case "customer":
                    navigationStack.push(this::showMainMenu);
                    customerMenu.show();
                    break;
                case "exit":
                    System.out.println("Thank you for using our system!");
                    return;
                default:
                    System.out.println("Invalid role. Please try again.");
            }
        }
    }
}