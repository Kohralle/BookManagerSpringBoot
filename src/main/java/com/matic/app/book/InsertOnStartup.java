package com.matic.app.book;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import java.util.List;


@Component
public class InsertOnStartup {
    @Bean
    CommandLineRunner commandLineRunner(BookRepository repository){
        return args -> {
            Book book1 = new Book("1440p", "Samsung", "Monitor", "Samsung Publishing");
            Book book2 = new Book("823", "Bashar Jackson", "Meet The Woo", "Victor Victor");
            repository.saveAll(List.of(book1, book2));
        };

    }

}
