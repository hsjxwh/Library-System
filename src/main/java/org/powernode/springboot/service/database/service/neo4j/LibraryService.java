package org.powernode.springboot.service.database.service.neo4j;

import org.powernode.springboot.bean.neo4j.Author;
import org.powernode.springboot.bean.neo4j.Book;
import org.powernode.springboot.bean.neo4j.Type;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.HashMap;
import java.util.List;

public interface LibraryService {
    Author addAuthor(Author author);
    Type addType(Type type);
    Book addBook(Book book);

    List<String> addAuthors(List<Author> authors);
    HashMap<String,List<String>> addBooks(List<Book> books);
    List<String> addTypes(List<Type> types);

    Book deleteBook(Book book);
    Author deleteAuthor(Author author);
    Type deleteType(Type type);
}
