package com.matic.app.book;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

//TODO
//Dodać error handling, np gdy kasujemy książke której nie ma
//JSON wyświetla się w złej kolejności 321 zamiast 123 po id

@RestController
@AllArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks(){
        return ResponseEntity.ok().body(bookService.getAllbooks());
    }

    @PostMapping
    public ResponseEntity<List<Book>> addBook(@RequestBody Book book) throws URISyntaxException {
        bookService.addBook(book);
        URI uri = new URI("/");
        return ResponseEntity.created(uri).body(bookService.getAllbooks());
    }

    @DeleteMapping(path = "{bookId}")
    public ResponseEntity<List<Book>> deleteBook(@PathVariable("bookId") Long id) throws URISyntaxException {
        bookService.deleteBook(id);
        URI uri = new URI("/");
        return ResponseEntity.ok().location(uri).body(bookService.getAllbooks());
    }

    @PutMapping
    public ResponseEntity<List<Book>> updateBook(@RequestBody Book book) throws URISyntaxException {
        bookService.updateBook(book.getId(), book.getIsbn(), book.getTitle(), book.getAuthor(), book.getPublisher());
        URI uri = new URI("/");
        return ResponseEntity.ok().location(uri).body(bookService.getAllbooks());
    }

    @GetMapping(path = "{isbn}")
    public ResponseEntity<List<Book>> getBookByIsbn(@PathVariable("isbn") String isbn){
        return ResponseEntity.ok().body(bookService.getBookByIsbn(isbn));

    }
}
