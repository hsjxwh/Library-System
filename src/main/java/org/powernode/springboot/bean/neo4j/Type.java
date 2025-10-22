package org.powernode.springboot.bean.neo4j;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Version;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Node("Type")
public class Type {
    @Id
    @GeneratedValue
    private Long id;
    private String type;
    @Version
    private Long version;

    public Type(String type) {
        this.type = type;
    }
}
