package org.powernode.springboot.tool;

import jakarta.servlet.http.HttpServletRequest;
import org.powernode.springboot.service.database.service.redis.LoginTokenService;
import org.slf4j.Logger;

public final class DealWithRequestTool {
    public static void checkFrequency(HttpServletRequest request, LoginTokenService loginTokenService,Logger logger,long id,String role){
        String ip=request.getRemoteAddr();
        
    }


}
