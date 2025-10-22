package org.powernode.springboot.bean.redis;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginToken implements Serializable {
    //不同身份以及其id组成的哈希值
    String id;
    //当前用户的有效token
    String token;
}
