package org.powernode.springboot.service.database.service.neo4j.impl;

import org.powernode.springboot.bean.neo4j.Author;
import org.powernode.springboot.bean.neo4j.Book;
import org.powernode.springboot.bean.neo4j.Type;
import org.powernode.springboot.repository.AccountRepository;
import org.powernode.springboot.repository.AuthorRepository;
import org.powernode.springboot.repository.BookRepository;
import org.powernode.springboot.repository.TypeRepository;
import org.powernode.springboot.service.database.service.neo4j.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class LibraryServiceImpl implements LibraryService {
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private TypeRepository typeRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private Neo4jTemplate template;
    @Override
    public Author addAuthor(Author author) {
        authorRepository.save(author);
        return author;
    }

    @Override
    public Type addType(Type type) {
        typeRepository.save(type);
        return null;
    }

    @Override
    public Book addBook(Book book) {
        bookRepository.save(book);
        return null;
    }

    @Override
    public List<String> addAuthors(List<Author> authors) {
        return List.of();
    }

    @Override
    public HashMap<String, List<String>> addBooks(List<Book> books) {
        return null;
    }

    @Override
    public List<String> addTypes(List<Type> types) {
        return List.of();
    }

    @Override
    public Book deleteBook(Book book) {
        return null;
    }

    @Override
    public Author deleteAuthor(Author author) {
        return null;
    }

    @Override
    public Type deleteType(Type type) {
        return null;
    }
}
