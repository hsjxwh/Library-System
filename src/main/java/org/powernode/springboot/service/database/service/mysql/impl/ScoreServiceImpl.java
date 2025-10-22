package org.powernode.springboot.service.database.service.mysql.impl;

import org.powernode.springboot.annotation.TransactionFail;
import org.powernode.springboot.bean.mysql.Score;
import org.powernode.springboot.mapper.database.ScoreMapper;
import org.powernode.springboot.service.database.service.mysql.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class ScoreServiceImpl implements ScoreService {
    @Autowired
    private ScoreMapper scoreMapper;
    @Override
    @Transactional
    @TransactionFail
    public int insertScore(long id) {
        return scoreMapper.insertScore(id);
    }

    @Override
    @Transactional
    @TransactionFail
    public int deleteScore(long id) {
        return scoreMapper.deleteScore(id);
    }

    @Override
    @Transactional
    @TransactionFail
    public int updateScore(long id,int score) {
        int sourceScore = selectScore(id);
        return scoreMapper.updateScore(id,  score+sourceScore);
    }

    @Override
    @Transactional
    public int selectScore(long id) {
        return scoreMapper.selectScore(id);
    }

    @Override
    @Transactional
    public List<Score> selectAllScore() {
        return scoreMapper.selectAllScore();
    }
}
