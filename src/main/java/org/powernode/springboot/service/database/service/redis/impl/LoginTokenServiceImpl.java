package org.powernode.springboot.service.database.service.redis.impl;

import org.powernode.springboot.bean.redis.BlackListAccount;
import org.powernode.springboot.bean.redis.OnlineAccount;
import org.powernode.springboot.service.database.service.redis.LoginTokenService;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LoginTokenServiceImpl implements LoginTokenService {
    private final RedisTemplate<String, Object> redisTemplate;
    //在线时长
    private final long onlineTime=1000*60*60*3;
    private final String onlineAccountKey="onlineAccount";
    private final String blacklistKey="blacklist";
    //一个请求最多的存放时间
    private final long requestTime=1000*60;

    LoginTokenServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void setTokenStartValidTime(String key, long time) {
        redisTemplate.opsForValue().set(key, time);
    }

    @Override
    public long getTokenStartValidTime(String key) {
        Long time= (Long)redisTemplate.opsForValue().get(key);
        if(time==null)
            time=-1L;
        return time;
    }

    @Override
    public void addOnlineCount(String role, long id, long time) {
        ZSetOperations<String,Object> zSetOperations=redisTemplate.opsForZSet();
        String value=role+" "+id;
        zSetOperations.add(onlineAccountKey,value,time+onlineTime);
    }

    @Override
    public void addIpRequest(String ip,String role,long id, long time) {
        ZSetOperations<String,Object> zSetOperations=redisTemplate.opsForZSet();
        zSetOperations.add("ip:"+ip,role+" "+id+" "+ UUID.randomUUID(),time);
    }

    @Override
    public List<OnlineAccount> getAllOnlineAccount(long currentTime) {
        Set<Object> set= redisTemplate.opsForZSet().reverseRangeByScore(onlineAccountKey,currentTime-onlineTime+1,Long.MAX_VALUE);
        List<OnlineAccount> onlineAccounts=new ArrayList<>();
        if (set != null) {
            for(Object o:set){
                String info=(String)o;
                String[] split = info.split(" ");
                OnlineAccount onlineAccount=new OnlineAccount(split[0],split[1]);
                onlineAccounts.add(onlineAccount);
            }
        }
        return onlineAccounts;
    }

    @Override
    public Long requestTime(String ip, long time, long duration) {
        String key ="ip:"+ip;
        return redisTemplate.opsForZSet().count(key,time-duration+1,time);
    }

    @Override
    public List<BlackListAccount> getBlackListAccount() {
        List<BlackListAccount> blackListAccounts=new ArrayList<>();
        Map<Object,Object> map=redisTemplate.opsForHash().entries(blacklistKey);
        if(map==null||map.isEmpty())
            return blackListAccounts;
        for(Map.Entry<Object,Object> entry:map.entrySet()){
            BlackListAccount blackListAccount;
            String ip=(String)entry.getKey();
            String value=(String)entry.getValue();
            if(value.equals("#")){
                blackListAccount=new BlackListAccount(ip);
            }
            else{
                String[] split = value.split(" ");
                blackListAccount=new BlackListAccount(ip,split[0],Long.parseLong(split[1]));
            }
            blackListAccounts.add(blackListAccount);
        }
        return blackListAccounts;
    }

    @Override
    public void addBlackListAccount(String ip,String role, long id) {
        redisTemplate.opsForHash().put(blacklistKey,ip,getBlacklistValue(role,id));
    }

    @Override
    public void removeBlackListAccount(String ip) {
        redisTemplate.opsForHash().delete(blacklistKey,ip);
    }

    @Override
    @Scheduled(fixedRate = 1000*60*60*3)
    public void clearOnlineCount() {
        long currentTime=System.currentTimeMillis();
        redisTemplate.opsForZSet().removeRangeByScore(onlineAccountKey,-1,currentTime-onlineTime);
    }

    @Override
    @Scheduled(fixedRate = 1000*60*60*12)
    public void clearIpRequest() {
       long currentTime=System.currentTimeMillis();
       Set<String> keys=findMyAppZSets();
       for (String key:keys) {
           redisTemplate.opsForZSet().removeRangeByScore(key,-1,currentTime-requestTime);
       }
    }

    @Override
    public boolean isInBlackList(String ip) {
        return redisTemplate.opsForHash().hasKey(blacklistKey,ip);
    }

    //找到所有存放了ip的有序集合
    private Set<String> findMyAppZSets(){
        Set<String> myZSets=new HashSet<>();
        Set<String> keys=redisTemplate.keys("ip:"+"*");
        for(String key:keys){
            if (redisTemplate.type(key) == DataType.ZSET) {
                myZSets.add(key);
            }
        }
        return myZSets;
    }

    private String getBlacklistValue(String role,long id) {
        if(id<0|| role.isEmpty()){
            return "#";
        }
        return role+" "+id;
    }
}
