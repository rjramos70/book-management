package com.near.u.api.book;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/books")
public class BookController {
    private final IBookService bookService;

    @PostMapping
    public ResponseEntity<BookResponse> addBook(@RequestBody BookRequest bookRequest) throws JsonProcessingException {
        BookResponse createdBook = bookService.addBook(bookRequest);
        return ResponseEntity.ok(createdBook);
    }

    @PatchMapping("/{id}/borrow")
    public ResponseEntity<BookResponse> borrowBook(@PathVariable Long id) {
        BookResponse borrowedBook = bookService.borrowBook(id);
        if (borrowedBook != null){
            return ResponseEntity.ok(borrowedBook);
        }
        return ResponseEntity.badRequest().body(null);
    }

    @PatchMapping("/{id}/return")
    public ResponseEntity<BookResponse> returnBook(@PathVariable Long id) {
        BookResponse returnedBook = bookService.returnBook(id);
        if(returnedBook != null){
            return ResponseEntity.ok(returnedBook);
        }
        return ResponseEntity.badRequest().body(null);
    }

    @GetMapping("/borrowed")
    public ResponseEntity<List<BookResponse>> listBorrowedBooks() {
        List<BookResponse> borrowedBooks = bookService.listBorrowedBooks();
        return ResponseEntity.ok(borrowedBooks);
    }

    @GetMapping
    public ResponseEntity<List<BookResponse>> listAllBooks() {
        List<BookResponse> borrowedBooks = bookService.listAllBooks();
        return ResponseEntity.ok(borrowedBooks);
    }
}
