import data.TestData;
import menu.MainMenu;
import services.BookService;
import services.OrderService;

public class Main {
    private BookService bookService;
    private OrderService orderService;
    private MainMenu mainMenu;

    public Main() {
        // Use the bookService from TestData
        bookService = TestData.bookService;
        orderService = new OrderService(bookService);
        mainMenu = new MainMenu(bookService, orderService);
    }

    public static void main(String[] args) throws Exception {
        Main bookstore = new Main();
        bookstore.mainMenu.start();
    }
}