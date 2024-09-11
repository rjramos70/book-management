package com.near.u.api.book;

public class BookIsAlreadyBorrowedException extends RuntimeException {
    public BookIsAlreadyBorrowedException(String message) {
        super(message);
    }
}
