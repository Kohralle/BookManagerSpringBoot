package com.matic.app.book;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest {

    @Autowired
    private BookService service;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        service.deleteAllBooks();
    }

    @Test
   void addBook() throws Exception {

       Book book = getTestBooks().get(0);

       mockMvc.perform(post("/")
               .contentType(MediaType.APPLICATION_JSON)
               .content(asJsonString(book)))
               .andExpect(status().isCreated());
    }

    @Test
    void getAllBooks() throws Exception {
        List<Book> list = getTestBooks();
        service.addBook(list.get(0));
        service.addBook(list.get(1));
        service.addBook(list.get(2));

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))

                .andExpect(jsonPath("$[2].isbn", is(list.get(0).getIsbn())))
                .andExpect(jsonPath("$[2].author", is(list.get(0).getAuthor())))
                .andExpect(jsonPath("$[2].title", is(list.get(0).getTitle())))
                .andExpect(jsonPath("$[2].publisher", is(list.get(0).getPublisher())))

                .andExpect(jsonPath("$[1].isbn", is(list.get(1).getIsbn())))
                .andExpect(jsonPath("$[1].author", is(list.get(1).getAuthor())))
                .andExpect(jsonPath("$[1].title", is(list.get(1).getTitle())))
                .andExpect(jsonPath("$[1].publisher", is(list.get(1).getPublisher())))

                .andExpect(jsonPath("$[0].isbn", is(list.get(2).getIsbn())))
                .andExpect(jsonPath("$[0].author", is(list.get(2).getAuthor())))
                .andExpect(jsonPath("$[0].title", is(list.get(2).getTitle())))
                .andExpect(jsonPath("$[0].publisher", is(list.get(2).getPublisher())));
    }

    @Test
    void deleteBook() throws Exception {
        List<Book> list = getTestBooks();
        service.addBook(list.get(0));
        service.addBook(list.get(1));
        service.addBook(list.get(2));

        mockMvc.perform(delete("/{id}", 3))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))

                .andExpect(jsonPath("$[1].isbn", is(list.get(1).getIsbn())))
                .andExpect(jsonPath("$[1].author", is(list.get(1).getAuthor())))
                .andExpect(jsonPath("$[1].title", is(list.get(1).getTitle())))
                .andExpect(jsonPath("$[1].publisher", is(list.get(1).getPublisher())))

                .andExpect(jsonPath("$[0].isbn", is(list.get(2).getIsbn())))
                .andExpect(jsonPath("$[0].author", is(list.get(2).getAuthor())))
                .andExpect(jsonPath("$[0].title", is(list.get(2).getTitle())))
                .andExpect(jsonPath("$[0].publisher", is(list.get(2).getPublisher())));


    }

    @Test
    void updateBook() throws Exception {
        List<Book> list = getTestBooks();
        service.addBook(list.get(0));
        service.addBook(list.get(1));
        service.addBook(list.get(2));

        //Book book = new Book(1L,"8374", "vrcfind", "vicfhu", "ivf");

        mockMvc.perform(put("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(new Book(1L,"8374", "vrcfind", "vicfhu", "ivf"))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$[2].isbn", is(list.get(0).getIsbn())))
                .andExpect(jsonPath("$[2].author", is(list.get(0).getAuthor())))
                .andExpect(jsonPath("$[2].title", is(list.get(0).getTitle())))
                .andExpect(jsonPath("$[2].publisher", is(list.get(0).getPublisher())))

                .andExpect(jsonPath("$[1].isbn", is("8374")))
                .andExpect(jsonPath("$[1].author", is("vrcfind")))
                .andExpect(jsonPath("$[1].title", is("vicfhu")))
                .andExpect(jsonPath("$[1].publisher", is("ivf")))

                .andExpect(jsonPath("$[0].isbn", is(list.get(2).getIsbn())))
                .andExpect(jsonPath("$[0].author", is(list.get(2).getAuthor())))
                .andExpect(jsonPath("$[0].title", is(list.get(2).getTitle())))
                .andExpect(jsonPath("$[0].publisher", is(list.get(2).getPublisher())));
    }

    @Test
    void getBookByIsbn() throws Exception {
        Book book = getTestBooks().get(0);
        service.addBook(book);

        mockMvc.perform(get("/{isbn}", book.getIsbn()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].isbn", is(book.getIsbn())))
                .andExpect(jsonPath("$[0].author", is(book.getAuthor())))
                .andExpect(jsonPath("$[0].title", is(book.getTitle())))
                .andExpect(jsonPath("$[0].publisher", is(book.getPublisher())));
    }

    List<Book> getTestBooks(){
        String isbn = "harry0";
        String author = "J.K. Rowling";
        String title = "Harry Potter and the Deathly Hallows";
        String publisher = "Bloomsbury";

        String isbn1 = "harry1";
        String author1 = "J.K. Rowling";
        String title1 = "Harry Potter and the Philosopher's Stone";
        String publisher1 = "Bloomsbury";

        String isbn2 = "harry2";
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
