package org.powernode.springboot.bean.mysql;

public class Score {
    long id;
    long score;

    public Score(long id, long score) {
        this.id = id;
        this.score = score;
    }

    public Score() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "Score{" +
                "id=" + id +
                ", score=" + score +
                '}';
    }
}
