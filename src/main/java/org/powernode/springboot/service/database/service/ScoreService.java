package org.powernode.springboot.service.database.service;

import org.apache.ibatis.annotations.Param;
import org.powernode.springboot.bean.database.Score;

import java.util.List;

public interface ScoreService {
    int insertScore(long id);
    int deleteScore(long id);
    int updateScore(long id,int score);
    int selectScore(long id);
    List<Score> selectAllScore();
}
