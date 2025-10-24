package org.powernode.springboot.repository;

import org.powernode.springboot.bean.neo4j.Book;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository  extends Neo4jRepository<Book, Long> {
    @Query("""
            UNWIND $books AS book
                MERGE (a:Author {name:book.author.name})
                MERGE (t:Type {type:book.type.type})
                MERGE (b:Book {title:book.title,id:book.id,idAndTitle:book.idAndTitle})
                MERGE (b)-[:WRITER]->(a)
                MERGE (b)-[:BELONG]->(t)
                RETURN b.title
                ;
            """)
    List<String> addBooks(List<Book> books);
    @Query("""
            UNWIND $book AS book
                MATCH (b:Book {title:idAndTitle:book.idAndTitle})-
                    [r:WRITER]->(a:Author {name:book.author.name})
                delete r;
            """)
    void deleteAuthorRelation(Book book);

    @Query("""
            UNWIND $book AS book
            MATCH (b:Book {idAndTitle:book.idAndTitle})-
                [r:BELONG]->(t:Type {type:book.type.type})
            delete r;
            """)
    void deleteTypeRelation(Book book);
}
