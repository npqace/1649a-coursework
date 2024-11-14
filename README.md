# Bookstore Management System

A Java-based command-line application for managing a bookstore's inventory and order processing system.

## Features

### Customer Functions
- Browse available books
- Search books by title
- Sort books by title or price
- Place orders
- Track order status

### Admin Functions
- Manage book inventory (add, update, remove)
- View all books with sorting options
- Process pending orders
- Update order status
- View active orders
- Monitor stock levels

## Data Structures
- `OrderQueue`: Custom queue implementation for order processing (FIFO)
- `InventoryList`: Generic list implementation for book inventory management
- `InventoryItem`: Generic wrapper class for items with quantity

## Algorithms
- Binary Search: For finding books by ID
- QuickSort: For sorting books by various attributes
- Linear Search: For searching books by title

## Project Structure
```
src/
├── algorithms/
│ ├── SearchAlgorithm.java
│ └── SortAlgorithm.java
├── data/ 
│ └── TestData.java 
├── data_structures/ 
│ ├── InventoryItem.java 
│ ├── InventoryList.java 
│ └── OrderQueue.java 
├── interfaces/ 
│ ├── IInventoryList.java 
│ └── IOrderQueue.java 
├── menu/ 
│ ├── AdminMenu.java 
│ ├── CustomerMenu.java 
│ └── MainMenu.java 
├── models/ 
│ ├── Book.java 
│ └── Order.java 
├── services/ 
│ ├── BookService.java 
│ └── OrderService.java 
└── Main.java
```

# Getting Started

### Prerequisites
- Java Development Kit (JDK) 17 or higher
- Visual Studio Code with Java extensions

### Running the Application
1. Clone the repository
2. Open the project in Visual Studio Code
3. Run `Main.java`
4. Choose role (admin/customer) to access respective features

## Usage

### Admin Menu
1. View all books
2. Add new book
3. Update book details
4. Update book stock
5. Remove book
6. View all orders
7. Process pending orders
8. View active orders
9. Update order status
10. Back

### Customer Menu
1. View all books
2. Search book
3. Sort book
4. Place order
5. Track order
6. Back

## Implementation Details

### Error Handling
- Input validation for all user inputs
- Exception handling for database operations
- Null checks for critical operations

### Data Management
- In-memory data structures for inventory and orders
- FIFO order processing queue
- Automated ID generation for books and orders

## Future Improvements
- Persistent data storage
- User authentication
- Enhanced search capabilities
- Order history tracking
- Transaction logging