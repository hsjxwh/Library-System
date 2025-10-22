package org.powernode.springboot.bean.neo4j;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Version;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Node("Account")
public class Account implements Serializable {
    @Id
    private Long id;
    @Version
    private Long version;
}
