import data.TestData;
import menu.MainMenu;
import services.BookService;
import services.OrderService;

public class Main {
    private BookService bookService;
    private OrderService orderService;
    private MainMenu mainMenu;

    // Initialize services and menu
    public Main() {
        TestData.initializeData();
        this.bookService = TestData.bookService;
        this.orderService = TestData.orderService;
        this.mainMenu = new MainMenu(bookService, orderService);
    }

    // Application entry point
    public static void main(String[] args) throws Exception {
        Main bookstore = new Main();
        bookstore.mainMenu.start();
    }
}
