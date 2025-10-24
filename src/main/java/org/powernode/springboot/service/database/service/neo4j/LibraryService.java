package org.powernode.springboot.service.database.service.neo4j;

import org.powernode.springboot.bean.neo4j.Author;
import org.powernode.springboot.bean.neo4j.Book;
import org.powernode.springboot.bean.neo4j.Type;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.HashMap;
import java.util.List;

public interface LibraryService {
    void addAuthor(Author author);
    void addType(Type type);
    void addBook(Book book);

    List<String> addAuthors(List<Author> authors);
    List<String> addBooks(List<Book> books);
    List<String> addTypes(List<Type> types);

    void deleteBook(Book book);
    void deleteAuthor(Author author);
    void deleteType(Type type);
}
