package org.powernode.springboot.websocket.interceptor;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.powernode.springboot.tool.JwtTool;
import org.powernode.springboot.websocket.handler.ManagerWebSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

//握手拦截器
@Component
public class AuthHandshakeInterceptor implements HandshakeInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(AuthHandshakeInterceptor.class);
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if(request instanceof ServletServerHttpRequest servletServerHttpRequest){
            HttpServletRequest httpServletRequest=servletServerHttpRequest.getServletRequest();
            String connectToken=httpServletRequest.getParameter("connectToken");
            //发送连接请求的设备
            String device=httpServletRequest.getParameter("device");
            logger.info("设备{}正在发送连接请求",device);
            if(device==null||device.isEmpty()||connectToken==null||connectToken.isEmpty()){
                return false;
            }
            Claims claims= JwtTool.checkJwt(connectToken);
            if(claims!=null){
                long id=Long.parseLong(String.valueOf(claims.get("id")));
                String tokenDevice=(String)claims.get("device");
                logger.info("解析出来的token的设备是{}",tokenDevice);
                String role=(String)claims.get("role");
                logger.info("握手阶段检测出当前管理员id为{}，设备为{}，权限为{}",id,tokenDevice,role);
                //只有pc端能够生成这个二维码，因此tokenDevice一定要是pc
                //而且只有管理员能够连接，因此role必须是管理员
                if("manager".equals(role)&&tokenDevice.equals("pc")){
                    if(ManagerWebSocketHandler.containKey(id)){
                        if(ManagerWebSocketHandler.getConnectToken(id).equals(connectToken)) {
                            logger.info("检出当前管理员存在过连接，使用的token相同,id为{}，设备为{}，token为{}，权限为{}的管理员成功加入会话",id,device,tokenDevice,role);
                            setInfo(attributes,connectToken,device,role,id);
                            System.out.println("pass AuthHandshakeInterceptor");
                            return true;
                        }
                        logger.info("检出当前管理员存在过连接，使用的token不相同");
                    }
                    else{
                        setInfo(attributes,connectToken,device,role,id);
                        logger.info("id为{}，设备为{}，token为{}，权限为{}的管理员成功加入会话,是此管理员这次连接的第一个连接者",id,device,tokenDevice,role);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //将信息存入attributes方便后期使用
    private void setInfo(Map<String, Object> attributes,String connectToken,String device,String role,long id){
        attributes.put("device",device);
        attributes.put("role",role);
        attributes.put("id",id);
        attributes.put("connectToken",connectToken);
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
