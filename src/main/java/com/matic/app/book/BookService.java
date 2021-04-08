package com.matic.app.book;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service //equivalent to the Component annotation, but used when we wanna annotate a service
@AllArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public List<Book> getAllbooks() {
        return bookRepository.findAll();
    }

    public void addBook(Book book) {
        bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        boolean exists = bookRepository.existsById(id);
        if(!exists){
            throw new IllegalStateException("book with id " + id + " does not exist");
        }

        bookRepository.deleteById(id);
    }

    @Transactional
    public void updateBook(Long bookId, String isbn, String author, String title, String publisher){
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new IllegalStateException("book with id " + bookId + " does not exist"));

        if(isbn != null && isbn.length() > 0 && !Objects.equals(book.getIsbn(), isbn)){
            book.setIsbn(isbn);
        }

        if(author != null && author.length() > 0 && !Objects.equals(book.getAuthor(), author)){
            book.setAuthor(author);
        }

        if(title != null && title.length() > 0 && !Objects.equals(book.getTitle(), title)){
            book.setTitle(title);
        }

        if(publisher != null && publisher.length() > 0 && !Objects.equals(book.getPublisher(), publisher)){
            book.setPublisher(publisher);
        }
    }

    public List<Book> getBookByIsbn(String isbn) {
        Book book = bookRepository.findByIsbn(isbn);

        if(Objects.isNull(book)){
            return List.of();
        }

        return List.of(book);
    }
}
