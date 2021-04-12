package com.matic.app.book;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Book{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String isbn;
    private String author;
    private String title;
    private String publisher;

    //can't replace with lombook
    public Book(String isbn, String author, String title, String publisher) {
        this.isbn = isbn;
        this.author = author;
        this.title = title;
        this.publisher = publisher;
    }
}
