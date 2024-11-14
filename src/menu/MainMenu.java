package menu;

import services.BookService;
import services.OrderService;
import java.util.Scanner;

public class MainMenu {
    private final AdminMenu adminMenu;
    private final CustomerMenu customerMenu;
    private final Scanner scanner;

    public MainMenu(BookService bookService, OrderService orderService) {
        if (bookService == null || orderService == null) {
            throw new IllegalArgumentException("BookService and OrderService cannot be null");
        }
        this.adminMenu = new AdminMenu(bookService, orderService);
        this.customerMenu = new CustomerMenu(bookService, orderService);
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            System.out.println("\n===== Bookstore Management System =====");
            System.out.print("Please choose your role (admin/customer/exit): ");
            String role = scanner.nextLine().toLowerCase().trim();

            switch (role) {
                case "admin":
                    try {
                        adminMenu.show();
                    } catch (Exception e) {
                        System.out.println("An error occurred in the Admin Menu: " + e.getMessage());
                    }
                    break;
                case "customer":
                    try {
                        customerMenu.show();
                    } catch (Exception e) {
                        System.out.println("An error occurred in the Customer Menu: " + e.getMessage());
                    }
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