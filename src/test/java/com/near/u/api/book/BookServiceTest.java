package com.near.u.api.book;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

//@DataJpaTest
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test: Happy path for borrowing a book
    @Test
    public void testBorrowBook_Success() {
        Long bookId = 1L;
        Book book = new Book();
        book.setId(bookId);
        book.setTitle("Title");
        book.setAuthor("Author");
        book.setBorrowed(false);

        BookResponse expectedResponse = new BookResponse(bookId, "Title", "Author", true);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(bookMapper.entityToBookResponse(book)).thenReturn(expectedResponse);

        BookResponse response = bookService.borrowBook(bookId);

        assertNotNull(response);
        assertTrue(response.borrowed());
        assertEquals(expectedResponse, response);

        verify(bookRepository).findById(bookId);
        verify(bookRepository).save(book);
        verify(bookMapper).entityToBookResponse(book);
    }

    // Test: Exception when borrowing a non-existing book
    @Test
    public void testBorrowBook_BookNotFound() {
        Long bookId = 1L;

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.borrowBook(bookId));

        verify(bookRepository).findById(bookId);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    // Test: Exception when borrowing an already borrowed book
    @Test
    public void testBorrowBook_BookAlreadyBorrowed() {
        Long bookId = 1L;

        Book book = new Book();
        book.setId(bookId);
        book.setTitle("Title");
        book.setAuthor("Author");
        book.setBorrowed(true);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        assertThrows(BookIsAlreadyBorrowedException.class, () -> bookService.borrowBook(bookId));

        verify(bookRepository).findById(bookId);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    // Test: Happy path for returning a borrowed book
    @Test
    public void testReturnBook_Success() {
        Long bookId = 1L;

        Book book = new Book();
        book.setId(bookId);
        book.setTitle("Title");
        book.setAuthor("Author");
        book.setBorrowed(true);

        BookResponse expectedResponse = new BookResponse(bookId, "Title", "Author", false);

        when(bookRepository.findByIdAndBorrowed(bookId, true)).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.entityToBookResponse(book)).thenReturn(expectedResponse);

        BookResponse response = bookService.returnBook(bookId);

        assertNotNull(response);
        assertFalse(response.borrowed());
        assertEquals(expectedResponse, response);

        verify(bookRepository).findByIdAndBorrowed(bookId, true);
        verify(bookRepository).save(book);
        verify(bookMapper).entityToBookResponse(book);
    }

    // Test: Exception when returning a book that wasn't borrowed
    @Test
    public void testReturnBook_BookWasNotBorrowed() {
        Long bookId = 1L;

        when(bookRepository.findByIdAndBorrowed(bookId, true)).thenReturn(Optional.empty());

        assertThrows(BookWasNotBorrowedException.class, () -> bookService.returnBook(bookId));

        verify(bookRepository).findByIdAndBorrowed(bookId, true);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    // Test: List all borrowed books
    @Test
    public void testListBorrowedBooks_Success() {
        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Title1");
        book1.setAuthor("Author1");
        book1.setBorrowed(true);

        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Title2");
        book2.setAuthor("Author2");
        book2.setBorrowed(true);

        BookResponse response1 = new BookResponse(1L, "Title1", "Author1", true);
        BookResponse response2 = new BookResponse(2L, "Title2", "Author2", true);

        when(bookRepository.findByBorrowedTrue()).thenReturn(List.of(book1, book2));
        when(bookMapper.entityToBookResponse(book1)).thenReturn(response1);
        when(bookMapper.entityToBookResponse(book2)).thenReturn(response2);

        List<BookResponse> responses = bookService.listBorrowedBooks();

        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals(response1, responses.get(0));
        assertEquals(response2, responses.get(1));

        verify(bookRepository).findByBorrowedTrue();
        verify(bookMapper).entityToBookResponse(book1);
        verify(bookMapper).entityToBookResponse(book2);
    }

    // Test: Add a new book
    @Test
    public void testAddBook_Success() throws JsonProcessingException {
        BookRequest bookRequest = new BookRequest("Title", "Author");

        Book book = new Book();
        book.setId(1L);
        book.setTitle("Title");
        book.setAuthor("Author");
        book.setBorrowed(false);

        BookResponse expectedResponse = new BookResponse(1L, "Title", "Author", false);

        when(bookMapper.bookRequestToEntity(bookRequest)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.entityToBookResponse(book)).thenReturn(expectedResponse);

        BookResponse response = bookService.addBook(bookRequest);

        assertNotNull(response);
        assertEquals(expectedResponse, response);

        verify(bookMapper).bookRequestToEntity(bookRequest);
        verify(bookRepository).save(book);
        verify(bookMapper).entityToBookResponse(book);
    }

    // Test: List all books
    @Test
    public void testListAllBooks_Success() {

        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Title1");
        book1.setAuthor("Author1");
        book1.setBorrowed(false);

        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Title2");
        book2.setAuthor("Author2");
        book2.setBorrowed(true);


        BookResponse response1 = new BookResponse(1L, "Title1", "Author1", false);
        BookResponse response2 = new BookResponse(2L, "Title2", "Author2", true);

        when(bookRepository.findAll()).thenReturn(List.of(book1, book2));
        when(bookMapper.entityToBookResponse(book1)).thenReturn(response1);
        when(bookMapper.entityToBookResponse(book2)).thenReturn(response2);

        List<BookResponse> responses = bookService.listAllBooks();

        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals(response1, responses.get(0));
        assertEquals(response2, responses.get(1));

        verify(bookRepository).findAll();
        verify(bookMapper).entityToBookResponse(book1);
        verify(bookMapper).entityToBookResponse(book2);
    }
}