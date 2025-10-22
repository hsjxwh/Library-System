package org.powernode.springboot.bean.neo4j;

import lombok.Data;
import org.springframework.data.annotation.Version;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node("Author")
@Data
public class Author {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @Version
    private Long version;

    public Author(String name) {
        this.name = name;
    }

}
