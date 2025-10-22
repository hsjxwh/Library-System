package org.powernode.springboot.repository;

import org.powernode.springboot.bean.neo4j.Author;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends Neo4jRepository<Author,Long>{
    @Query("MATCH (n:Author) WHERE n.name =$name RETURN n")
    Optional<Author> findByName(String name);
    @Query("""
            UNWIND $authors AS author
                    WITH author
                    WHERE author.name IS NOT NULL
                    MERGE (u:Author {name:author.name})
                    ON CREATE SET
                        u.name = author.name,
                        u.wasCreated = true
                    ON MATCH SET
                        u.wasCreated = false
                    WITH u
                        WHERE u.wasCreated = false
                    RETURN
                        u.name
                        ;
            """)
    //返回的是当前信息列表中已经存在的作者的名字
    List<String> addAuthors(List<Author> authors);
}
