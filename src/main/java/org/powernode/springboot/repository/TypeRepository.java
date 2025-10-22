package org.powernode.springboot.repository;

import org.powernode.springboot.bean.neo4j.Type;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;

public interface TypeRepository extends Neo4jRepository<Type,Long> {
    @Query("""
            UNWIND $types as type
                WITH type
                WHERE type.type is not null
                MERGE (n:Type {type:type.name})
                ON CREATE SET
                    n.type=type
                    n.wasCreated = true
                ON MATCH SET
                    n.wasCreated = false
                WITH n
                    WHERE n.wasCreated = false
                RETURN n.type
            """)
    List<String> addTypes(List<Type> types);
}
