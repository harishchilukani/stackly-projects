
package com.library.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import com.library.dto.BookRequest;
import com.library.dto.BookResponse;
import com.library.service.BookService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    // Add Book
    @PostMapping
    public ResponseEntity<BookResponse> addBook(
            @Valid @RequestBody BookRequest request) {

        return new ResponseEntity<>(
                bookService.addBook(request),
                HttpStatus.CREATED);
    }

    // Get All Books
    @GetMapping
    public ResponseEntity<List<BookResponse>> getAllBooks() {

        return ResponseEntity.ok(bookService.getAllBooks());
    }

    // Get Book By ID
    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(
            @PathVariable Long id) {

        return ResponseEntity.ok(bookService.getBookById(id));
    }

    // Update Complete Book Details
    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> updateBook(
            @PathVariable Long id,
            @Valid @RequestBody BookRequest request) {

        return ResponseEntity.ok(
                bookService.updateBook(id, request));
    }

    // Update Book Availability
    @PatchMapping("/{id}/availability")
    public ResponseEntity<BookResponse> updateAvailability(
            @PathVariable Long id,
            @RequestParam Boolean available) {

        return ResponseEntity.ok(
                bookService.updateAvailability(id, available));
    }

    // Delete Book
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                bookService.deleteBook(id));
    }

    // Search Books By Name
    @GetMapping("/search")
    public ResponseEntity<List<BookResponse>> searchByName(
            @RequestParam String name) {

        return ResponseEntity.ok(
                bookService.searchByName(name));
    }

    // Search Books By Author
    @GetMapping("/author")
    public ResponseEntity<List<BookResponse>> searchByAuthor(
            @RequestParam String name) {

        return ResponseEntity.ok(
                bookService.searchByAuthor(name));
    }

    // Get Available Books
    @GetMapping("/available")
    public ResponseEntity<List<BookResponse>> getAvailableBooks() {

        return ResponseEntity.ok(
                bookService.getAvailableBooks());
    }

    // Get Unavailable Books
    @GetMapping("/unavailable")
    public ResponseEntity<List<BookResponse>> getUnavailableBooks() {

        return ResponseEntity.ok(
                bookService.getUnavailableBooks());
    }

    // Filter Books By Price
    @GetMapping("/price")
    public ResponseEntity<List<BookResponse>> filterByPrice(
            @RequestParam Double min,
            @RequestParam Double max) {

        return ResponseEntity.ok(
                bookService.filterByPrice(min, max));
    }

    // Sort Books
    @GetMapping("/sort")
    public ResponseEntity<List<BookResponse>> sortBooks(
            @RequestParam String by,
            @RequestParam(defaultValue = "asc") String order) {

        return ResponseEntity.ok(
                bookService.sortBooks(by, order));
    }
}
