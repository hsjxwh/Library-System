package org.powernode.springboot.websocket.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.powernode.springboot.service.database.service.mysql.OrdersService;
import org.powernode.springboot.service.database.service.mysql.UserService;
import org.powernode.springboot.tool.JwtTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.powernode.springboot.websocket.model.Message;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ManagerWebSocketHandler extends TextWebSocketHandler {
    private enum MessageType {
        TRANS_ID(1),
        DANGER_CLOSE(2),
        SAFE_CLOSE(3),
        SUCCESS_LOGIN(4),
        PAY_CODE(5),
        RECHARGE_CODE(6),
        CHANGE_PHONE_STATUS_TO_START_SERVICE(10),
        CHANGE_PHONE_STATUS_TO_RECHARGE(11);
        private final int code;
        MessageType(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }
    private static final Logger logger = LoggerFactory.getLogger(ManagerWebSocketHandler.class);
    //存储会话
    private static final ConcurrentHashMap<Long, Map<String,WebSocketSession>> sessions=new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Long,String> tokenMap=new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;
    private final OrdersService ordersService;
    private final UserService userService;
    public ManagerWebSocketHandler(OrdersService ordersService,ObjectMapper objectMapper,UserService userService) {
        this.ordersService=ordersService;
        this.objectMapper=objectMapper;
        this.userService=userService;
    }
    @Override
    //连接建立是调用
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        String connectToken=(String)session.getAttributes().get("connectToken");
        String device=(String)session.getAttributes().get("device");
        String role=(String)session.getAttributes().get("role");
        long id=Long.parseLong(session.getAttributes().get("id").toString());
        Claims claims=JwtTool.checkJwt(connectToken);
        String tokenDevice=claims.get("device").toString();
        String tokenRole=claims.get("role").toString();
        long tokenId=Long.parseLong(String.valueOf(id));
        logger.info("token是{}，设备是{},权限是{}，编号是{}的管理员成功建立连接，现在开始再次验证信息," +
                "检测出token解析出来的设备是{}，权限是{}，编号是{}",connectToken,device,role,id,tokenDevice,tokenRole,tokenId);
        //检查连接者id是否和他提供的token解析出来的id一样,以及只有pc端能够生成token
        if(tokenMap.containsKey(id)&&!tokenMap.get(id).equals(connectToken)
                //检查解析出来的结果是否和握手协议是解析出来的内容一样
                ||!claims.get("device").equals("pc")
                ||!claims.get("role").equals("manager")
                ||!claims.get("role").equals(role)
                ||!(id==tokenId)
                ){
            logger.info("编号为{}的管理员的连接被非法用户加入",id);
            closeAllSession(session,id,"非法用户加入当前连接",MessageType.DANGER_CLOSE.getCode());
            return;
        }
        tokenMap.computeIfAbsent(id,k->connectToken);
        Map<String,WebSocketSession> devicesSession=sessions.computeIfAbsent(id,k->new HashMap<String,WebSocketSession>());
        if(devicesSession.containsKey(device)){
            logger.info("编号为{}的管理员的{}设备已经在连接中，非法入侵",id,device);
            closeAllSession(session,id,"当前连接中,"+(device.equals("pc")?"电脑":"手机"+"已经连接过，现有设备冒充，关闭所有来连接"),MessageType.DANGER_CLOSE.getCode());
            return;
        }
        devicesSession.put(device,session);
        sendMessage(session,id,device,new Message("您已成功连接到服务器",MessageType.SUCCESS_LOGIN.getCode()));
        if(device.equals("phone")){
            sendMessage(sessions.get(id).get("pc"),id,device,new Message("手机端成功连接",MessageType.SUCCESS_LOGIN.getCode()));
        }
        logger.info("编号为{}的管理员已经使用{}建立连接，session编号是{}", id,device,session.getId());
    }

    //处理服务端接收到的信息
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String device=session.getAttributes().get("device").toString();
        long id=Long.parseLong(session.getAttributes().get("id").toString());
        logger.info("{}的{}发来信息",id,device);
        String token=message.getPayload();
        if(device.equals("phone")||device.equals("pc")){
            handleMessage(session,id,device,token);
        }
        else{
            logger.info("有不明设备试图冒充id为{}的管理员发送信息,关闭所有连接",id);
            closeAllSession(session,id,"有不明设备试图加入此连接，请重新登录，并联系管理员",MessageType.DANGER_CLOSE.getCode());
        }
    }

    //当客户端返回对象类型的消息时的处理方法
    private void handleMessage(WebSocketSession session,long id,String device,String token){
        Map<String,WebSocketSession> devicesSession=sessions.get(id);
        try {
            Message getMessage=objectMapper.readValue(token,Message.class);
            int status=getMessage.getStatus();
            if(status==MessageType.SAFE_CLOSE.getCode()){
                logger.info("编号为{}的管理员在{}端发起停止连接请求",id,device);
                if(devicesSession!=null){
                    logger.info("编号为{}的管理员的{}客户端关闭连接，状态码为{}",id,device,status);
                    closeAllSession(session,id,id+"管理员的"+device+"客户端已正常断开连接，关闭所有当前连接中的设备",status);
                }
            }
            //只有手机端能够发送识别到的付款吗，开通服务
            else if(status==MessageType.PAY_CODE.getCode()&&device.equals("phone")){
                logger.info("编号为{}的管理员在{}端发送了扫描到的付款吗信息，检测是否合法,当前应对的使用户开通借阅服务功能",id,device);
                treatActivateService(getMessage,id,device);
            }
            //只有手机端能够发送识别到的付款吗，使手机能获取扫码后发送请求需要的信息
            else if(status==MessageType.CHANGE_PHONE_STATUS_TO_START_SERVICE.getCode()&&device.equals("pc")){
                sendMessage(sessions.get(id).get("phone"),id,device,getMessage);
                logger.info("服务器讲编号为{}的管理员的手机端改变状态以成功扫描用户的付款码,开通服务",id);
            }
            //只有电脑端能发送信息该改变手机端的状态
            else if(status==MessageType.CHANGE_PHONE_STATUS_TO_RECHARGE.getCode()&&device.equals("pc")){
                sendMessage(sessions.get(id).get("phone"),id,device,getMessage);
                logger.info("服务器讲编号为{}的管理员的手机端改变状态以成功扫描用户的付款码，进行充值",id);
            }
            //只有手机端能够发送识别到的付款码，开启充值服务
            else if(status==MessageType.RECHARGE_CODE.getCode()&&device.equals("phone")){
                logger.info("编号为{}的管理员在{}端发送了扫描到的付款吗信息，检测是否合法，当前应对的使用户充值服务",id,device);
                treatRechargeService(getMessage,id,device);
            }
            //只能手机端发送SUCCESS_TRANS
            else if(device.equals("phone")&&MessageType.TRANS_ID.getCode()==status){
                long userId=JwtTool.getId(getMessage.getId());
                sendMessage(sessions.get(id).get("pc"),id,device,new Message(userId,status));
                logger.info("编号为{}的管理员已成功用手机向电脑发送用户的身份信息",id);
            }
            else{
                //客户端能够发送的消息只有安全关闭连接，成功登录和危险关闭连接都是由服务器检测，危险关连会发送请求到连接上的其他服务端和客户端，而成功传输只支持纯文本模式
                logger.info("检查发现，编号为{}的管理员的{}客户端发来的信息的状态码不符合要求，关闭所有来连接",id,device);
                closeAllSession(session,id,"编号为"+id+"的管理员收到的信息不符合要求，有设备伪冒，关闭连接",MessageType.DANGER_CLOSE.getCode());
            }
        } catch (IOException e) {
            logger.info("检查发现，编号为{}的管理员的{}客户端发来的信息不符合要求，关闭所有来连接",id,device);
            //当发送的不是message类型的时候
            closeAllSession(session,id,"编号为"+id+"的管理员收到的信息不符合要求，有设备伪冒，关闭连接",MessageType.DANGER_CLOSE.getCode());
            throw new RuntimeException(e);
        }
    }

    //连接关闭时调用
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        String device=session.getAttributes().get("device").toString();
        long id=Long.parseLong(session.getAttributes().get("id").toString());
        Map<String,WebSocketSession> devicesSession=sessions.get(id);
        //清除map信息
        if(devicesSession!=null){
            devicesSession.remove(device);
            if(devicesSession.isEmpty()){
                sessions.remove(id);
                tokenMap.remove(id);
            }
        }
        logger.info("id为{}的管理员的{}设备已断开连接",id,device);
    }

    private void sendMessage(WebSocketSession session,long id,String device, Message message){
        if(session!=null&&session.isOpen()){
            try {
                String res=objectMapper.writeValueAsString(message);
                session.sendMessage(new TextMessage(res));
                logger.info("id为{}的管理员用{}发送信息成功",id,device);
            } catch (IOException e) {
                logger.info("id为{}的管理员用{}发送信息失败",id,device);
                throw new RuntimeException(e);
            }
        }
    }

    //关闭所有当前管理员建立的连接
    private void closeAllSession(WebSocketSession session,long id,String message,int status){
        try {
            Map<String,WebSocketSession> map=sessions.get(id);
            for(Map.Entry<String,WebSocketSession> set:map.entrySet()){
                String device=set.getKey();
                WebSocketSession webSocketSession=set.getValue();
                logger.info("编号为{}的管理员，令服务器给{}端发送信息：{}",id,device,message);
                //让客户端关闭连接
                sendMessage(webSocketSession,id,device,new Message(message,status));
                //关闭服务器端的连接
                webSocketSession.close();
            }
            sessions.remove(id);
            session.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void treatRechargeService(Message getMessage,long id,String device){
        try {
            String message=getMessage.getMessage();
            Claims claims=JwtTool.checkJwt(message);
            long userId=Long.parseLong(claims.get("id").toString());
            String purpose=getMessage.getPurpose();
            LocalDateTime time=LocalDateTime.now();
            double balance=getMessage.getRecharge();
            logger.info("编号为{}的管理员正在处理来自编号为{}用户的目的为充值，金额大小为{}的订单",id,userId,balance);
            if(ordersService.insertOrders(userId,id,balance,3,purpose,time,-1)>0){
                logger.info("编号为{}的管理员正在处理编号为{}用户的目的为充值，金额大小为{}的订单成功",id,userId,balance);
                sendMessage(sessions.get(id).get("pc"),id,device,new Message("充值成功,用户编码为"+userId+"金额为"+balance+"元",MessageType.RECHARGE_CODE.getCode()));
            }
            else{
                logger.info("编号为{}的管理员处理来自编号为{}用户的目的为充值，金额大小为{}的订单失败",id,userId,balance);
                sendMessage(sessions.get(id).get("pc"),id,device,new Message("充值失败,用户编码为"+userId+"金额为"+balance+"元",MessageType.RECHARGE_CODE.getCode()));
            }
        }catch (ExpiredJwtException e) {
            logger.info("编号为{}的管理员在处理充值的订单时，发现当前的付款吗已经过期",id);
        }
    }

    //处理开通服务业务
    private void treatActivateService(Message getMessage,long id,String device){
        try {
            String message=getMessage.getMessage();
            Claims claims=JwtTool.checkJwt(message);
            long userId=Long.parseLong(claims.get("id").toString());
            double money=Double.parseDouble(claims.get("money").toString());
            String purpose=getMessage.getPurpose();
            LocalDateTime time=LocalDateTime.now();
            logger.info("编号为{}的管理员正在处理来自编号为{}用户的目的为开通借阅服务，金额大小为{}的订单",id,userId,money);
            int serviceType= getMessage.getServiceType();
            //非法服务类型
            if(serviceType!=1&&serviceType!=2){
                logger.info("编号为{}的管理员在处理来自编号为{}用户的目的为{}，金额大小为{}的订单时，发现其开通服务的类型存在异常",id,userId,purpose,money);
                return;
            }
            if(ordersService.insertOrders(userId,id,money,serviceType,purpose,time,-1)>0) {
                logger.info("编号为{}的管理员处理来自编号为{}用户的目的为{}，金额大小为{}的订单成功",id,userId,purpose,money);
                sendMessage(sessions.get(id).get("pc"),id,device,new Message("订单创建成功，用户编码为"+userId+"金额为"+money+"元",MessageType.PAY_CODE.getCode()));
            }
            else {
                logger.info("编号为{}的管理员处理来自编号为{}用户的目的为{}，金额大小为{}的订单失败",id,userId,purpose,money);
                sendMessage(sessions.get(id).get("pc"),id,device,new Message("订单创建失败，用户编码为"+userId+"金额为"+money+"元",MessageType.PAY_CODE.getCode()));
            }
        } catch (ExpiredJwtException e) {
            logger.info("编号为{}的管理员在处理开启服务的订单时，发现当前的付款吗已经过期",id);
        }
    }

    //是否已经有此管理员的连接
    public static boolean containKey(long id){
        return tokenMap.containsKey(id)||sessions.containsKey(id);
    }

    //获取管理员的加入websocket连接的token
    public static String getConnectToken(long id){
        return tokenMap.get(id);
    }
}
