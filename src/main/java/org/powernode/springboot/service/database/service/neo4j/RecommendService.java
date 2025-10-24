package org.powernode.springboot.service.database.service.neo4j;

public interface RecommendService {
    void addBookRecord(long userId,long bookId);
    void addRenewRecord(long userId,long bookId);
}
