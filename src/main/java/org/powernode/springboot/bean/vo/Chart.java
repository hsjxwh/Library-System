package org.powernode.springboot.bean.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//排行榜的表示类
public class Chart {
    String name;
    long time;

    public Chart() {
    }

    public Chart(String name, long time) {
        this.name = name;
        this.time = time;
    }
}
