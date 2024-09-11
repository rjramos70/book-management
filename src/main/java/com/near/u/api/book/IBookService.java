package com.near.u.api.book;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface IBookService {
    BookResponse borrowBook(Long bookId);
    BookResponse returnBook(Long bookId);
    List<BookResponse> listBorrowedBooks();
    BookResponse addBook(BookRequest bookRequest) throws JsonProcessingException;
    List<BookResponse> listAllBooks();

}
