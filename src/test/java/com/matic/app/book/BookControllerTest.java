package com.matic.app.book;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest {

    private AutoCloseable autoCloseable;

    @Mock
    private BookService service;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();

    }

   @Test
   void addBook() throws Exception {

       Book book = getTestBooks().get(0);

       mockMvc.perform(post("/")
               .contentType(MediaType.APPLICATION_JSON)
               .content(asJsonString(book)))
               .andExpect(status().isCreated());

       //czy to zadziała jeśli nie wezwałem w kodzie bezpośrednio metody addBook, ale została ona wywołana przez post request?
       verify(service, times(1)).addBook(book);
    }

    @Test
    void getAllBooks() throws Exception {
        List<Book> list = getTestBooks();
        doReturn(list).when(service).getAllbooks();

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string(HttpHeaders.LOCATION, "/")).andExpect(jsonPath("$", hasSize(3)))

                .andExpect(jsonPath("$[0].id", is(1L)))
                .andExpect(jsonPath("$[0].isbn", is(list.get(0).getIsbn())))
                .andExpect(jsonPath("$[0].author", is(list.get(0).getAuthor())))
                .andExpect(jsonPath("$[0].title", is(list.get(0).getTitle())))
                .andExpect(jsonPath("$[0].publisher", is(list.get(0).getPublisher())))

                .andExpect(jsonPath("$[1].id", is(2L)))
                .andExpect(jsonPath("$[1].isbn", is(list.get(1).getIsbn())))
                .andExpect(jsonPath("$[1].author", is(list.get(1).getAuthor())))
                .andExpect(jsonPath("$[1].title", is(list.get(1).getTitle())))
                .andExpect(jsonPath("$[1].publisher", is(list.get(1).getPublisher())))

                .andExpect(jsonPath("$[2].id", is(1L)))
                .andExpect(jsonPath("$[2].isbn", is(list.get(2).getIsbn())))
                .andExpect(jsonPath("$[2].author", is(list.get(2).getAuthor())))
                .andExpect(jsonPath("$[2].title", is(list.get(2).getTitle())))
                .andExpect(jsonPath("$[2].publisher", is(list.get(2).getPublisher())));
    }

    @Test
    void deleteBook() {
        //TODO
    }

    @Test
    void updateBook() {
        //TODO
    }

    @Test
    void getBookByIsbn() throws Exception {
        // Setup our mocked service
        Book book = getTestBooks().get(0);
        doReturn(book).when(service).getBookByIsbn(book.getIsbn());


        mockMvc.perform(get("/{isbn}", book.getIsbn()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string(HttpHeaders.LOCATION, "/9780739360385"))

                .andExpect(jsonPath("$.id", is(1L)))
                .andExpect(jsonPath("$.isbn", is(book.getIsbn())))
                .andExpect(jsonPath("$.author", is(book.getAuthor())))
                .andExpect(jsonPath("$.title", is(book.getTitle())))
                .andExpect(jsonPath("$.publisher", is(book.getPublisher())));
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

    static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
