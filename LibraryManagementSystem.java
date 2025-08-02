import java.io.*;
import java.util.*;

class Book {
    String title;
    String author;
    boolean isBorrowed;

    public Book(String title, String author, boolean isBorrowed) {
        this.title = title;
        this.author = author;
        this.isBorrowed = isBorrowed;
    }

    @Override
    public String toString() {
        return title + "," + author + "," + isBorrowed;
    }
}

public class LibraryManagementSystem {
    static final String FILE_NAME = "library.txt";
    static List<Book> books = new ArrayList<>();

    public static void main(String[] args) {
        loadBooksFromFile();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== Library Management System =====");
            System.out.println("1. Add Book");
            System.out.println("2. View Books");
            System.out.println("3. Borrow Book");
            System.out.println("4. Return Book");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    addBook(sc);
                    break;
                case 2:
                    viewBooks();
                    break;
                case 3:
                    borrowBook(sc);
                    break;
                case 4:
                    returnBook(sc);
                    break;
                case 5:
                    saveBooksToFile();
                    System.out.println("Library data saved. Exiting...");
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    // Load books from file
    static void loadBooksFromFile() {
        try {
            File file = new File(FILE_NAME);
            if (!file.exists()) {
                file.createNewFile();
            }
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            books.clear();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    books.add(new Book(parts[0], parts[1], Boolean.parseBoolean(parts[2])));
                }
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Error loading books: " + e.getMessage());
        }
    }

    // Save books to file
    static void saveBooksToFile() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME));
            for (Book b : books) {
                bw.write(b.toString());
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            System.out.println("Error saving books: " + e.getMessage());
        }
    }

    static void addBook(Scanner sc) {
        System.out.print("Enter book title: ");
        String title = sc.nextLine();
        System.out.print("Enter book author: ");
        String author = sc.nextLine();
        books.add(new Book(title, author, false));
        saveBooksToFile();
        System.out.println("Book added successfully.");
    }

    static void viewBooks() {
        if (books.isEmpty()) {
            System.out.println("No books in library.");
            return;
        }
        System.out.println("\nAvailable Books:");
        for (int i = 0; i < books.size(); i++) {
            Book b = books.get(i);
            System.out.printf("%d. %s by %s [%s]%n", i + 1, b.title, b.author,
                    b.isBorrowed ? "Borrowed" : "Available");
        }
    }

    static void borrowBook(Scanner sc) {
        viewBooks();
        System.out.print("Enter book number to borrow: ");
        int index = sc.nextInt() - 1;
        if (index >= 0 && index < books.size()) {
            if (!books.get(index).isBorrowed) {
                books.get(index).isBorrowed = true;
                saveBooksToFile();
                System.out.println("You borrowed the book.");
            } else {
                System.out.println("Book is already borrowed.");
            }
        } else {
            System.out.println("Invalid book number.");
        }
    }

    static void returnBook(Scanner sc) {
        viewBooks();
        System.out.print("Enter book number to return: ");
        int index = sc.nextInt() - 1;
        if (index >= 0 && index < books.size()) {
            if (books.get(index).isBorrowed) {
                books.get(index).isBorrowed = false;
                saveBooksToFile();
                System.out.println("Book returned successfully.");
            } else {
                System.out.println("This book was not borrowed.");
            }
        } else {
            System.out.println("Invalid book number.");
        }
    }
}
