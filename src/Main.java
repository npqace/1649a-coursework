import data.TestData;
import menu.MainMenu;
import services.BookService;
import services.OrderService;

public class Main {
    private BookService bookService;
    private OrderService orderService;
    private MainMenu mainMenu;

    /**
     * Creates a new instance of the `Main` class and initializes the necessary
     * services.
     */
    public Main() {
        // Use the pre-populated book service from TestData
        TestData.initializeData();
        this.bookService = TestData.bookService;
        this.orderService = TestData.orderService;
        this.mainMenu = new MainMenu(bookService, orderService);
    }

    /**
     * The main entry point of the application.
     *
     * @param args Command-line arguments (not used in this case).
     * @throws Exception Handles potential exceptions during the application's
     *                   execution.
     */
    public static void main(String[] args) throws Exception {
        Main bookstore = new Main();
        bookstore.mainMenu.start();
    }
}