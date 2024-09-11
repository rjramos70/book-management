package com.near.u.api.book;

public class BookWasNotBorrowedException extends RuntimeException {
    public BookWasNotBorrowedException(String message) {
        super(message);
    }
}
