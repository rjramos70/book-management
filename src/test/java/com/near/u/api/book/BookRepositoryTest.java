package com.near.u.api.book;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        // Clean the table
        bookRepository.deleteAllInBatch();

        // Initialize the database with some books
        Book book1 = new Book();
        book1.setTitle("Book 1");
        book1.setAuthor("Author 1");
        book1.setBorrowed(false);
        bookRepository.save(book1);

        Book book2 = new Book();
        book2.setTitle("Book 2");
        book2.setAuthor("Author 2");
        book2.setBorrowed(true);
        bookRepository.save(book2);

        Book book3 = new Book();
        book3.setTitle("Book 3");
        book3.setAuthor("Author 3");
        book3.setBorrowed(true);
        bookRepository.save(book3);

    }

    @Test
    void testFindByBorrowedTrue() {
        // Get all borrowed books
        List<Book> borrowedBooks = bookRepository.findByBorrowedTrue();

        // Assert: There should be 2 borrowed books
        assertEquals(2, borrowedBooks.size());
        assertTrue(borrowedBooks.stream().allMatch(Book::isBorrowed));
    }

    @Test
    void testFindByIdAndBorrowed() {

        // Find a borrowed book by ID
        Optional<Book> bookOpt = bookRepository.findByIdAndBorrowed(3L, true);

        // Assert: Book should be found, and it should be borrowed
        assertTrue(bookOpt.isPresent());
        assertTrue(bookOpt.get().isBorrowed());
        assertEquals("Book 2", bookOpt.get().getTitle());
    }

    @Test
    void testFindByIdAndBorrowedNotFound() {
        // Try to find a non-borrowed book by ID
        Optional<Book> bookOpt = bookRepository.findByIdAndBorrowed(1L, true);

        // Assert: No book should be found, since book 1 is not borrowed
        assertFalse(bookOpt.isPresent());
    }
}
