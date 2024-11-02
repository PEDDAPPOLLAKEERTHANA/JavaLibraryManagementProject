import java.io.*;
import java.util.*;

// Book class representing a book entity
class Book implements Serializable {
    private static final long serialVersionUID = 1L;
    String bookId, bookName, authorName;
    int publishedYear; // Added published year attribute
    boolean isIssued;

    public Book(String bookId, String bookName, String authorName, int publishedYear) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.authorName = authorName;
        this.publishedYear = publishedYear; // Initialize published year
        this.isIssued = false;
    }

    @Override
    public String toString() {
        return "Book ID: " + bookId + ", Title: " + bookName + ", Author: " + authorName
                + ", Published Year: " + publishedYear + ", Status: "
                + (isIssued ? "Issued" : "Available");
    }
}

// Library class to manage the collection of books
class Library {
    private List<Book> books = new ArrayList<>();
    private final String filePath = "libraryData.dat"; // Changed to .dat for binary data

    public Library() {
        loadLibraryData(); // Load existing data from the file

        // Adding default books if no data is present
        if (books.isEmpty()) {
            addBook(new Book("B001", "Java Programming", "James Gosling", 2005));
            addBook(new Book("B002", "Effective Java", "Joshua Bloch", 2008));
            addBook(new Book("B003", "Clean Code", "Robert C. Martin", 2008));
            saveLibraryData(); // Save the pre-loaded books
            System.out.println("Pre-loaded default books into the library.");
        }
    }

    // Method to add a new book
    public void addBook(Book book) {
        // Check for duplicate Book ID
        for (Book b : books) {
            if (b.bookId.equals(book.bookId)) {
                System.out.println("A book with this ID already exists. Please use a unique Book ID.");
                return;
            }
        }
        books.add(book);
        System.out.println("Book added successfully!");
    }

    // Method to view all books
    public void viewBooks() {
        if (books.isEmpty()) {
            System.out.println("No books available in the library.");
        } else {
            // Print the headers
            System.out.printf("%-10s %-30s %-20s %-15s %-20s%n", "Book ID", "Title", "Author", "Published Year",
                    "Status");
            System.out.println(
                    "------------------------------------------------------------------------------------------------");

            // Print each book's details in a formatted way
            for (Book book : books) {
                System.out.printf("%-10s %-30s %-20s %-15d %-10s%n", book.bookId, book.bookName, book.authorName,
                        book.publishedYear, book.isIssued ? "Issued" : "Available");
            }
        }
    }

    // Method to issue a book
    public void issueBook(String bookId) {
        for (Book b : books) {
            if (b.bookId.equals(bookId)) {
                if (!b.isIssued) {
                    b.isIssued = true;
                    System.out.println("Book issued successfully.");
                    return;
                } else {
                    System.out.println("Book is already issued.");
                    return;
                }
            }
        }
        System.out.println("Book not found.");
    }

    // Method to return a book
    public void returnBook(String bookId) {
        for (Book book : books) {
            if (book.bookId.equals(bookId)) {
                if (book.isIssued) {
                    book.isIssued = false;
                    System.out.println("Book returned successfully.");
                    return;
                } else {
                    System.out.println("Book was not issued.");
                    return;
                }
            }
        }
        System.out.println("Book not found.");
    }

    // Method to delete a book
    public void deleteBook(String bookId) {
        Iterator<Book> iterator = books.iterator();
        while (iterator.hasNext()) {
            Book book = iterator.next();
            if (book.bookId.equals(bookId)) {
                if (book.isIssued) {
                    System.out.println("Cannot delete the book as it is currently issued.");
                    return;
                }
                iterator.remove();
                System.out.println("Book deleted successfully.");
                return;
            }
        }
        System.out.println("Book not found.");
    }

    // Method to save library data to file
    public void saveLibraryData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(books);
            System.out.println("Library data saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving library data.");
            e.printStackTrace();
        }
    }

    // Method to load library data from file
    @SuppressWarnings("unchecked")
    private void loadLibraryData() {
        File file = new File(filePath);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                books = (List<Book>) ois.readObject();
                System.out.println("Library data loaded successfully.");
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error loading library data.");
                e.printStackTrace();
            }
        }
    }
}

// Main class with menu-driven program and authentication
public class LibraryManagementSystem {
    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "password";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Authentication
        System.out.println("=== Library Management System Login ===");
        System.out.print("Enter Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine().trim();

        if (!DEFAULT_USERNAME.equals(username) || !DEFAULT_PASSWORD.equals(password)) {
            System.out.println("Invalid credentials. Access denied.");
            scanner.close();
            return;
        }
        
        System.out.println("Access granted. Welcome to the Library Management System!");

        Library library = new Library();

        while (true) {
            System.out.println("\n=== Library Management System ===");
            System.out.println("1. View Stored Data");
            System.out.println("2. Add Book");
            System.out.println("3. Issue Book");
            System.out.println("4. Return Book");
            System.out.println("5. Delete Book");
            System.out.println("6. Save and Exit");

            System.out.print("Enter your choice: ");
            int choice;

            // Input validation for integer choices
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number between 1 and 6.");
                scanner.next(); // Clear the invalid input
                continue;
            }
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    // View all stored books
                    library.viewBooks();
                    break;

                case 2:
                    // Add a new book
                    System.out.print("Enter Book ID: ");
                    String bookId = scanner.nextLine().trim();
                    System.out.print("Enter Book Title: ");
                    String bookName = scanner.nextLine().trim();
                    System.out.print("Enter Author Name: ");
                    String authorName = scanner.nextLine().trim();
                    System.out.print("Enter Published Year: ");
                    int publishedYear;

                    // Input validation for published year
                    try {
                        publishedYear = scanner.nextInt();
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input! Please enter a valid year.");
                        scanner.next(); // Clear the invalid input
                        continue;
                    }
                    scanner.nextLine();  // Consume newline
                    
                    if (bookId.isEmpty() || bookName.isEmpty() || authorName.isEmpty()) {
                        System.out.println("All fields are required. Book not added.");
                        break;
                    }
                    
                    library.addBook(new Book(bookId, bookName, authorName, publishedYear));
                    library.saveLibraryData();  // Save after adding a book
                    break;

                case 3:
                    // Issue a book
                    System.out.print("Enter Book ID to issue: ");
                    String issueId = scanner.nextLine().trim();
                    library.issueBook(issueId);
                    library.saveLibraryData();  // Save after issuing
                    break;

                case 4:
                    // Return a book
                    System.out.print("Enter Book ID to return: ");
                    String returnId = scanner.nextLine().trim();
                    library.returnBook(returnId);
                    library.saveLibraryData();  // Save after returning
                    break;

                case 5:
                    // Delete a book
                    System.out.print("Enter Book ID to delete: ");
                    String deleteId = scanner.nextLine().trim();
                    library.deleteBook(deleteId);
                    library.saveLibraryData();  // Save after deletion
                    break;

                case 6:
                    // Save data and exit the system
                    library.saveLibraryData();
                    System.out.println("Exiting system and saving data...");
                    scanner.close();
                    System.exit(0);
                    break;

                default:
                    // Invalid input handling
                    System.out.println("Invalid choice! Please enter a number between 1 and 6.");
            }
        }
    }
}
