package org.powernode.springboot.repository;

import org.powernode.springboot.bean.neo4j.Book;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CalculateSimilarityRepository extends Neo4jRepository<Book, Long> {
    @Query("""
            merge (u:Account {id:$userId})-
                [r:BORROW]->(b:Book {id:$bookId})
            on create set
                r.time=1
            on match set
                r.time=r.time+1
            """)
    void addBookRecord(long userId,long bookId);
    @Query("""
            merge (u:Account {id:$userId})-
                [r:RENEW]->(b:Book {id:$bookId})
            on create set
                r.time=1
            on match set
                r.time=r.time+1
            """)
    void addRenewRecord(long userId,long renewId);
    @Query("""
            merge (u:Account {id:$userId})-
                [r:CLICK]->(b：Book:Book {id:$bookId})
            on create set
                r.time=1
            on match set
                r.time=r.time+1
            """)
    void addClickRecord(long userId,long clickId);
}
