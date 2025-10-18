package org.powernode.springboot.bean.database;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlackListAccount {
    String ip;
    String role;
    long id;
    public BlackListAccount(String ip){
        this.ip=ip;
    }
}
