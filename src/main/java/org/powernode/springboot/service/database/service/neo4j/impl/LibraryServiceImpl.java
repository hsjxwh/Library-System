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
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public void addAuthor(Author author) {
        authorRepository.save(author);
    }

    @Override
    @Transactional
    public void addType(Type type) {
        typeRepository.save(type);
    }

    @Override
    @Transactional
    public void addBook(Book book) {
        bookRepository.save(book);
    }

    @Override
    @Transactional
    public List<String> addAuthors(List<Author> authors) {
        return authorRepository.addAuthors(authors);
    }

    @Override
    @Transactional
    public List<String> addBooks(List<Book> books) {
        return bookRepository.addBooks(books);
    }

    @Override
    @Transactional
    public List<String> addTypes(List<Type> types) {
        return typeRepository.addTypes(types);
    }

    @Override
    @Transactional
    public void deleteBook(Book book) {
        bookRepository.delete(book);
    }

    @Override
    @Transactional
    public void deleteAuthor(Author author) {
        authorRepository.delete(author);
    }

    @Override
    @Transactional
    public void deleteType(Type type) {
        typeRepository.delete(type);
    }
}
