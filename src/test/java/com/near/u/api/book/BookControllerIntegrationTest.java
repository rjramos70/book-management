package com.near.u.api.book;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private RestTemplate restTemplate;

    @Test
    @Order(1)
    void testAddBook() {
        String url = "http://localhost:" + port + "/api/books";
        BookRequest bookRequest = new BookRequest("Integration Test Book", "Carlos Silva");

        ResponseEntity<Book> response = restTemplate.postForEntity(url, bookRequest, Book.class);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Integration Test Book", response.getBody().getTitle());
    }

    @Test
    @Order(2)
    void testBorrowBook() {
        BookRequest bookRequest = new BookRequest("Title 1", "Author 1");

        // Add a Book
        String urlAdd = "http://localhost:" + port + "/api/books";
        ResponseEntity<Book> responseAdd = restTemplate.postForEntity(urlAdd, bookRequest, Book.class);
        Book savedBook = responseAdd.getBody();

        // Borrow a Book
        String url = "http://localhost:" + port + "/api/books/" + savedBook.getId() + "/borrow";
        HttpEntity<Void> requestEntity = new HttpEntity<>(new HttpHeaders());

        ResponseEntity<Book> response = restTemplate.exchange(url, HttpMethod.PATCH, requestEntity, Book.class);
        Book borrowedBook = response.getBody();

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(borrowedBook.isBorrowed());
        assertEquals(3, borrowedBook.getId());
        assertEquals("Title 1", borrowedBook.getTitle());
        assertEquals("Author 1", borrowedBook.getAuthor());
    }

    @Test
    @Order(3)
    void testReturnBook() {

        // Retrieve book
        String url = "http://localhost:" + port + "/api/books/1/return";
        HttpEntity<Void> requestEntity = new HttpEntity<>(new HttpHeaders());

        ResponseEntity<Book> response = restTemplate.exchange(url, HttpMethod.PATCH, requestEntity, Book.class);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(!response.getBody().isBorrowed());
    }

    @Test
    @Order(4)
    void listBorrowedBooks() {
        // Add a borrowed book before
        String urlAdd = "http://localhost:" + port + "/api/books";
        BookRequest bookRequest = new BookRequest("Integration Test Book", "Author 1");

        ResponseEntity<Book> responseAdd = restTemplate.postForEntity(urlAdd, bookRequest, Book.class);
        assertEquals(200, responseAdd.getStatusCodeValue());

        // Retrieve borrowed book list
        String url = "http://localhost:" + port + "/api/books/borrowed";
        HttpEntity<Void> requestEntity = new HttpEntity<>(new HttpHeaders());

        ResponseEntity<Book[]> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Book[].class);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().length > 0);
    }

    @Test
    @Order(5)
    void listAllBooks() {
        // Retrieve book
        String url = "http://localhost:" + port + "/api/books/borrowed";
        HttpEntity<Void> requestEntity = new HttpEntity<>(new HttpHeaders());

        ResponseEntity<Book[]> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Book[].class);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().length > 0);
    }
}