package org.powernode.springboot.bean.database;

import lombok.Data;

@Data
public class BorrowTime {
    long id;
    long time=0;

    public BorrowTime(long id, long time) {
        this.id = id;
        this.time = time;
    }

    public BorrowTime() {
    }
}
