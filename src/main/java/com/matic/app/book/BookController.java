package com.matic.app.book;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public List<Book> getAllBooks(){
        return bookService.getAllbooks();
    }

    @PostMapping
    public void addBook(@RequestBody Book book){
        bookService.addBook(book);
    }

    @DeleteMapping(path = "{bookId}")
    public void deleteBook(@PathVariable("bookId") Long id){
        bookService.deleteBook(id);
    }

    @PutMapping(path = "{bookId}")
    public void updateBook( @PathVariable("bookId") Long studentId,
                            @RequestParam(required = false) String isbn,
                            @RequestParam(required = false) String title,
                            @RequestParam(required = false) String author,
                            @RequestParam(required = false) String publisher){

        bookService.updateBook(studentId, isbn, title, author, publisher);
    }

    @GetMapping(path = "{isbn}")
    public List<Book> getBookByIsbn(@PathVariable("isbn") String isbn){
        return bookService.getBookByIsbn(isbn);
    }


}
