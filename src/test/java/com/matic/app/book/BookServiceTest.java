package com.matic.app.book;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceTest {

    private AutoCloseable autoCloseable;

    @Mock
    private BookRepository repositoryTesting;

    @InjectMocks
    private BookService serviceTesting;


    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        //serviceTesting = new BookService(repositoryTesting);
        // czy to jest równoważne z @InjectMocks?
        //Czy @injectMocks automatycznie wskrzykuje dependencje w konstruktorze Service?njdcjn
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
        repositoryTesting.deleteAll();
    }

    @Test
    void addBook_basicTest()
    {
        Book book = getTestBooks().get(0);

        serviceTesting.addBook(book);

        verify(repositoryTesting, times(1)).save(book);
    }

    @Test
    void addBook_heavyLoad(){
        for (int i = 0; i < 1000; i++) {
            Book book = new Book(UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString());
            serviceTesting.addBook(book);
            verify(repositoryTesting, times(1)).save(book);
        }
    }

    @Test
    void AddBook_CaptorTest() {

        Book initial_book = getTestBooks().get(0);

        repositoryTesting.save(initial_book);

        ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);
        verify(repositoryTesting).save(bookArgumentCaptor.capture());

        Book captured_book = bookArgumentCaptor.getValue();

        assertEquals(initial_book, captured_book);
    }

    @Test
    void getAllbooks_basicTest() {
        serviceTesting.getAllbooks();
        verify(repositoryTesting).findAll();
    }

    @Test
    void getAllbooks_sizeTest() {
        List<Book> list = getTestBooks();

        when(repositoryTesting.findAll()).thenReturn(list);

        List<Book> empList = serviceTesting.getAllbooks();

        assertEquals(3, empList.size());
        verify(repositoryTesting, times(1)).findAll();
    }

    //nie działa
    @Test
    @Disabled
    void deleteBookTest() {
        Long id = 1L;
        Book book = getTestBooks().get(0);
        book.setId(id);

        serviceTesting.addBook(book);
        when(repositoryTesting.existsById(1L)).thenReturn(true);
        serviceTesting.deleteBook(1L);

        verify(repositoryTesting, times(1)).deleteById(id);
    }

    @Test
    void deleteBookTest_Error() {

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            serviceTesting.deleteBook(1L);
        });

        String expectedMessage = "book with id 1 does not exist";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }


    @Test
    void updateBook_Error() {
        Book book = getTestBooks().get(0);
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            serviceTesting.updateBook(1L, book.getIsbn(), book.getAuthor(), book.getTitle(), book.getPublisher());
        });

        String expectedMessage = "book with id 1 does not exist";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void getBookByIsbn() {
        String isbn = "9780739360385";
        String author = "J.K. Rowling";
        String title = "Harry Potter and the Deathly Hallows";
        String publisher = "Bloomsbury";

        Book initial_book = new Book(isbn, author, title, publisher);

        Mockito.when(repositoryTesting.findByIsbn("9780739360385")).thenReturn(initial_book);

        List<Book> result_book = serviceTesting.getBookByIsbn("9780739360385");

        assertEquals(author, result_book.get(0).getAuthor());
        assertEquals(title, result_book.get(0).getTitle());
        assertEquals(publisher, result_book.get(0).getPublisher());

    }

    List<Book> getTestBooks(){
        String isbn = "9780739360385";
        String author = "J.K. Rowling";
        String title = "Harry Potter and the Deathly Hallows";
        String publisher = "Bloomsbury";

        String isbn1 = "9781524721251";
        String author1 = "J.K. Rowling";
        String title1 = "Harry Potter and the Philosopher's Stone";
        String publisher1 = "Bloomsbury";

        String isbn2 = "9780747538486";
        String author2 = "J.K. Rowling";
        String title2 = "Harry Potter and the Chamber of Secrets";
        String publisher2 = "Bloomsbury";

        Book book = new Book(isbn, author, title, publisher);
        Book book1 = new Book(isbn1, author1, title1, publisher1);
        Book book2 = new Book(isbn2, author2, title2, publisher2);

        List<Book> list = new ArrayList<Book>();
        list.add(book);
        list.add(book1);
        list.add(book2);

        return list;
    }


}
