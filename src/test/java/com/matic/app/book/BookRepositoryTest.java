package com.matic.app.book;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BookRepositoryTest {

    @Autowired
    private BookRepository repositoryTesting;

    @AfterEach
    void tearDown() {
        repositoryTesting.deleteAll();
    }

    @Test
    void itShouldRetrieveBookByIsbn() {

        String isbn = "9780739360385";
        String author = "J.K. Rowling";
        String title = "Harry Potter and the Deathly Hallows";
        String publisher = "Bloomsbury";

        Book initial_book = new Book(isbn, author, title, publisher);
        repositoryTesting.save(initial_book);
        Book retrieved_book = repositoryTesting.findByIsbn(isbn);
        assertEquals(initial_book, retrieved_book);
    }

    @Test
    void itShouldReturnNull() {
        String isbn = "9780739360385";
        Book retrieved_book = repositoryTesting.findByIsbn(isbn);
        System.out.println(retrieved_book);
        assertEquals(null, retrieved_book);
    }
}
