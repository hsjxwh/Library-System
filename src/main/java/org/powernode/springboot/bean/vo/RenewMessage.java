package org.powernode.springboot.bean.vo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class RenewMessage {
    LocalDateTime expireTime;
    String info;

    public RenewMessage(LocalDateTime expireTime, String info) {
        this.expireTime = expireTime;
        this.info = info;
    }

    public RenewMessage() {
    }
}
