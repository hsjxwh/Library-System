package org.powernode.springboot.service.database.service.neo4j.impl;

import org.powernode.springboot.exception.ErrorRequestParamError;
import org.powernode.springboot.repository.AccountRepository;
import org.powernode.springboot.repository.BookRepository;
import org.powernode.springboot.repository.CalculateSimilarityRepository;
import org.powernode.springboot.service.database.service.neo4j.RecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RecommendServiceImpl implements RecommendService {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CalculateSimilarityRepository calculateSimilarityRepository;
    @Override
    @Transactional
    public void addBookRecord(long userId, long bookId) {
        if(!bookRepository.existsById(bookId)||!accountRepository.existsById(userId)) {
            throw new ErrorRequestParamError("没有这个编号的数据，或者没有这个编号的用户");
        }
        calculateSimilarityRepository.addBookRecord(userId, bookId);
    }

    @Override
    @Transactional
    public void addRenewRecord(long userId, long bookId) {
        if(!bookRepository.existsById(bookId)||!accountRepository.existsById(userId)) {
            throw new ErrorRequestParamError("没有这个编号的数据，或者没有这个编号的用户");
        }
        calculateSimilarityRepository.addRenewRecord(userId, bookId);
    }
}
