package org.powernode.springboot.service.database.service.redis;

import org.powernode.springboot.bean.database.LoginToken;

public interface LoginTokenService {
    void setTokenStartValidTime(String key, long time);
    long getTokenStartValidTime(String key);
}
