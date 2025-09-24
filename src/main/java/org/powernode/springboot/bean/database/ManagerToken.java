package org.powernode.springboot.bean.database;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ManagerToken implements Serializable {
    long id;
    String csrfToken;
    String connectToken;
}
