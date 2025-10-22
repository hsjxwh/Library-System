package org.powernode.springboot.mapper.database;

import org.apache.ibatis.annotations.Param;
import org.powernode.springboot.bean.mysql.Score;

import java.util.List;

public interface ScoreMapper {
    int insertScore(long id);
    int deleteScore(long id);
    int updateScore(@Param("id") long id, @Param("score") int score);
    //获取某个用户的信用分
    int selectScore(long id);
    List<Score> selectAllScore();
}
