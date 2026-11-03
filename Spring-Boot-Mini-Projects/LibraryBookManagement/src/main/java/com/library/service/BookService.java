
package com.library.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.library.dto.BookRequest;
import com.library.dto.BookResponse;
import com.library.exception.BookNotFoundException;
import com.library.exception.DuplicateBookException;
import com.library.model.Book;

@Service
public class BookService {

    private final List<Book> books = new ArrayList<>();

    private final AtomicLong idGenerator = new AtomicLong(1);

    // Add Book
    public BookResponse addBook(BookRequest request) {

        boolean duplicate = books.stream()
                .anyMatch(book ->
                        book.getBookName().equalsIgnoreCase(request.getBookName())
                                && book.getAuthor().equalsIgnoreCase(request.getAuthor()));

        if (duplicate) {
            throw new DuplicateBookException(
                    "This book already exists for the given author");
        }

        Long bookId = idGenerator.getAndIncrement();

        Book book = new Book(
                bookId,
                request.getBookName(),
                request.getAuthor(),
                request.getPrice(),
                request.getAvailable()
        );

        books.add(book);

        return convertToResponse(book);
    }

    // Get All Books
    public List<BookResponse> getAllBooks() {

        return books.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // Get Book By ID
    public BookResponse getBookById(Long bookId) {

        Book book = findBookById(bookId);

        return convertToResponse(book);
    }

    // Update Complete Book Details
    public BookResponse updateBook(Long bookId, BookRequest request) {

        Book book = findBookById(bookId);

        boolean duplicate = books.stream()
                .anyMatch(existingBook ->
                        !existingBook.getBookId().equals(bookId)
                                && existingBook.getBookName()
                                .equalsIgnoreCase(request.getBookName())
                                && existingBook.getAuthor()
                                .equalsIgnoreCase(request.getAuthor()));

        if (duplicate) {
            throw new DuplicateBookException(
                    "Another book with the same name and author already exists");
        }

        book.setBookName(request.getBookName());
        book.setAuthor(request.getAuthor());
        book.setPrice(request.getPrice());
        book.setAvailable(request.getAvailable());

        return convertToResponse(book);
    }

    // Update Book Availability
    public BookResponse updateAvailability(Long bookId, Boolean available) {

        Book book = findBookById(bookId);

        book.setAvailable(available);

        return convertToResponse(book);
    }

    // Delete Book
    public String deleteBook(Long bookId) {

        Book book = findBookById(bookId);

        books.remove(book);

        return "Book deleted successfully with ID: " + bookId;
    }

    // Search Books By Name
    public List<BookResponse> searchByName(String name) {

        return books.stream()
                .filter(book ->
                        book.getBookName().toLowerCase()
                                .contains(name.toLowerCase()))
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // Search Books By Author
    public List<BookResponse> searchByAuthor(String author) {

        return books.stream()
                .filter(book ->
                        book.getAuthor().toLowerCase()
                                .contains(author.toLowerCase()))
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // Get Available Books
    public List<BookResponse> getAvailableBooks() {

        return books.stream()
                .filter(Book::getAvailable)
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // Get Unavailable Books
    public List<BookResponse> getUnavailableBooks() {

        return books.stream()
                .filter(book -> !book.getAvailable())
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // Filter Books By Price Range
    public List<BookResponse> filterByPrice(Double min, Double max) {

        if (min < 0 || max < 0 || min > max) {
            throw new IllegalArgumentException("Invalid price range");
        }

        return books.stream()
                .filter(book ->
                        book.getPrice() >= min && book.getPrice() <= max)
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // Sort Books By Name Or Price
    public List<BookResponse> sortBooks(String by, String order) {

        Comparator<Book> comparator;

        if ("name".equalsIgnoreCase(by)) {
            comparator = Comparator.comparing(
                    Book::getBookName,
                    String.CASE_INSENSITIVE_ORDER);
        } else if ("price".equalsIgnoreCase(by)) {
            comparator = Comparator.comparing(Book::getPrice);
        } else {
            throw new IllegalArgumentException(
                    "Sort field must be name or price");
        }

        if ("desc".equalsIgnoreCase(order)) {
            comparator = comparator.reversed();
        } else if (!"asc".equalsIgnoreCase(order)) {
            throw new IllegalArgumentException(
                    "Sort order must be asc or desc");
        }

        return books.stream()
                .sorted(comparator)
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // Find Book By ID
    private Book findBookById(Long bookId) {

        return books.stream()
                .filter(book -> book.getBookId().equals(bookId))
                .findFirst()
                .orElseThrow(() ->
                        new BookNotFoundException(
                                "Book not found with ID: " + bookId));
    }

    // Convert Book To BookResponse
    private BookResponse convertToResponse(Book book) {

        return new BookResponse(
                book.getBookId(),
                book.getBookName(),
                book.getAuthor(),
                book.getPrice(),
                book.getAvailable()
        );
    }
}
