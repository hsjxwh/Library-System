package org.powernode.springboot.service.database.service.redis;

import org.powernode.springboot.bean.database.BlackListAccount;
import org.powernode.springboot.bean.database.LoginToken;
import org.powernode.springboot.bean.database.OnlineAccount;

import java.util.List;

public interface LoginTokenService {
    void setTokenStartValidTime(String key, long time);
    long getTokenStartValidTime(String key);
    void addOnlineCount(String role,long id,long time);
    void addIpRequest(String ip,String role,long id,long time);
    List<OnlineAccount> getAllOnlineAccount(long currentTime);
    //统计区间time内的请求次数
    Long requestTime(String ip,long time,long duration);
    List<BlackListAccount>  getBlackListAccount();
    void addBlackListAccount(String ip,String role,long id);
    void removeBlackListAccount(String ip);
    void clearOnlineCount();
    void clearIpRequest();
    boolean isInBlackList(String ip);
}
