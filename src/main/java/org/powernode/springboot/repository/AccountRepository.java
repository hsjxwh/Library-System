package org.powernode.springboot.repository;

import org.powernode.springboot.bean.neo4j.Account;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface AccountRepository extends Neo4jRepository<Account, Long> {

}
