package com.near.u.api.book;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookService implements IBookService{
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;


    @Override
    public BookResponse borrowBook(Long bookId){
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            if (!book.isBorrowed()) {
                book.setBorrowed(true);
                return bookMapper.entityToBookResponse(bookRepository.save(book));
            } else {
                throw new BookIsAlreadyBorrowedException("Book ID " + bookId + " is already borrowed");
            }
        } else {
            throw new BookNotFoundException("Book not found");
        }
    }

    @Override
    public BookResponse returnBook(Long bookId){
        Optional<Book> bookBorrowed = bookRepository.findByIdAndBorrowed(bookId, true);

        if (bookBorrowed.isPresent()) {
            Book book = bookBorrowed.get();
            book.setBorrowed(false);
            return bookMapper.entityToBookResponse(bookRepository.save(book));
        } else {
            throw new BookWasNotBorrowedException("Book ID " + bookId + " is not listed as borrowed and therefore cannot be returned.");
        }
    }

    @Override
    public List<BookResponse> listBorrowedBooks() {
        return bookRepository
                .findByBorrowedTrue()
                .stream()
                .map(b -> bookMapper.entityToBookResponse(b))
                .collect(Collectors.toList());
    }

    @Override
    public BookResponse addBook(BookRequest bookRequest) throws JsonProcessingException {
        Book book = bookMapper.bookRequestToEntity(bookRequest);
        return bookMapper.entityToBookResponse(bookRepository.save(book));
    }

    @Override
    public List<BookResponse> listAllBooks() {
        return bookRepository.findAll().stream()
                .map(b -> bookMapper.entityToBookResponse(b))
                .collect(Collectors.toList());
    }

}
