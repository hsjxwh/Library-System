package org.powernode.springboot.tool;

import jakarta.servlet.http.HttpServletRequest;
import org.powernode.springboot.service.database.service.redis.LoginTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DealWithRequestTool {
    //一分钟最多访问多少此
    private static final long minuteRequestTime=120;
    //十秒最多访问多少次
    private static final long tenSecondRequestTime=20;
    private static final Logger logger = LoggerFactory.getLogger(DealWithRequestTool.class);
    public static boolean checkFrequency(HttpServletRequest request, LoginTokenService loginTokenService,long id,String role,long currentTime) {
        //获取直接的真实ip
        String ip=request.getRemoteAddr();
        Long secondTime=loginTokenService.requestTime(ip,currentTime,60*1000),minuteTime=loginTokenService.requestTime(ip,currentTime,10*1000);
        //检查当前ip的访问频率，如果超标，则禁止访问
        if(minuteTime>minuteRequestTime||secondTime>tenSecondRequestTime){
            if(id<=0) {
                logger.info("id为{}的{}，使用{}ip访问的频率过快,一分钟内访问{}此，十秒内访问{}次", id, role, ip, minuteTime, secondTime);
            }
            else {
                logger.info("ip为{}的访客,一分钟内访问{}此，十秒内访问{}次", ip, minuteTime, secondTime);
            }
            return false;
        }
        loginTokenService.addIpRequest(ip,role,id,System.currentTimeMillis());
        return true;
    }


}
