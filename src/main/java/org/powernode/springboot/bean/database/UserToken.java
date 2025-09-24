package org.powernode.springboot.bean.database;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UserToken implements Serializable {
    long id;
    //三小时有效
    String csrfToken;

    public UserToken(long id, String csrfToken) {
        this.id = id;
        this.csrfToken = csrfToken;
    }

    public UserToken() {
    }
}
