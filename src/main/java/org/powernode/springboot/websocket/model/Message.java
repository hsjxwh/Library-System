package org.powernode.springboot.websocket.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Message {
    //扫描出来的用户的编号
    String id;
    //返回的信息
    String message;
    //自定义的状态码，前端可根据这个状态码得知当前消息的状态
    int status;
    int serviceType;
    String purpose;
    String payType;
    double recharge;

    public Message(long id, int status) {
        this.id = String.valueOf(id);
        this.status = status;
    }

    public Message(String message, int status) {
        this.message = message;
        this.status=status;
    }

    public Message(long id, String message, int status) {
        this.id = String.valueOf(id);
        this.message = message;
        this.status = status;
    }

    public Message() {
    }
}
