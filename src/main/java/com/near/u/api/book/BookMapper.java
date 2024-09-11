package com.near.u.api.book;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {
    private final ObjectMapper objectMapper;

    public BookMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Converts a BookRequest DTO to a Book Entity.
     *
     * @param bookRequest the BookRequest DTO
     * @return the Book Entity
     */
    public Book bookRequestToEntity(BookRequest bookRequest) {
        // Convert DTO to Entity using ObjectMapper
        Book book = objectMapper.convertValue(bookRequest, Book.class);

        // Initialize additional fields not present in DTO
        book.setBorrowed(false); // New books are not borrowed by default

        return book;
    }

    /**
     * Converts a Book Entity to a BookRequest DTO.
     *
     * @param book the Book Entity
     * @return the BookRequest DTO
     */
    public BookRequest entityToBookRequest(Book book) {
        // Convert Entity to DTO using ObjectMapper
        return objectMapper.convertValue(book, BookRequest.class);
    }

    /**
     * Converts a Book Entity to a BookResponse DTO.
     *
     * @param book the Book Entity
     * @return the BookResponse DTO
     */
    public BookResponse entityToBookResponse(Book book) {
        // Convert Entity to DTO using ObjectMapper
        return objectMapper.convertValue(book, BookResponse.class);
    }
}
