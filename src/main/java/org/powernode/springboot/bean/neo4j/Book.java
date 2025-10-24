package org.powernode.springboot.bean.neo4j;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.io.Serializable;
import java.util.List;

@Node("Book")
@Data
public class Book implements Serializable {
    private String title;
    @Id
    private Long id;
    private String idAndTitle;
    @Relationship(type="type",direction= Relationship.Direction.OUTGOING)
    Type type;
    @Relationship(type="author",direction= Relationship.Direction.OUTGOING)
    Author author;

    public Book(String title) {
        this.title = title;
    }
}
