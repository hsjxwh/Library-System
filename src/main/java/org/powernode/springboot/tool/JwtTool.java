package org.powernode.springboot.tool;

import io.jsonwebtoken.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.powernode.springboot.exception.*;
import org.powernode.springboot.service.database.service.redis.LoginTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

public final class JwtTool {
    //一个登录的身份jwt能维持多长时间,3小时
    private static final long time=1000*60*60*3;
    //付款码维持时长
    private static final long payTime=1000*60*3;
    private static final String signWith="l!ibrary@aaa,./?";
    private static final String signRandom ="c,s?.r58f";
    //存储用户登录token是，对账号信息键进行加密的key
    private static final String signInRedis="da.'f.f";
    Logger logger= LoggerFactory.getLogger(JwtTool.class);
    private final static String[] loginApis={"/user/getAllLibraryStorage","/user/getCharts","/user/postRecommendToStore","/user/getUseInfo","/user/generateQrToken","/user/getBookRecord","/user/generateTestQrToken","/user/getOrdersRecord","/user/getPayQr"};

    //生成验证请求身份的jwtToken
    public static String getJwt(long id,String role,String csrfToken,long currentTime){
        String randomTokenHash=hmacSha256(csrfToken);
        String res=  Jwts.builder()
                //类型
                .setHeaderParam("typ","JWT")
                //算法
                .setHeaderParam("alg","HS256")
                .claim("id",id)
                .claim("role",role)
                .claim("csrfToken",randomTokenHash)
                .claim("time",currentTime)
                //设置过期时间
                .setExpiration(new Date(currentTime+time))
                //设置唯一标识
                .setId(UUID.randomUUID().toString())
                //用户标识
                .setSubject(String.valueOf(id))
                //签名
                .signWith(SignatureAlgorithm.HS256,signWith)
                .compact();
        System.out.println("Generated JWT: " + res);
        return res;
    }

    //生成用于websocket连接的jwtToken
    public static String getConnectQr(long id, String role, LocalDateTime time, long QrTime){
        String res=  Jwts.builder()
                //类型
                .setHeaderParam("typ","JWT")
                //算法
                .setHeaderParam("alg","HS256")
                .claim("id",id)
                .claim("role",role)
                .claim("time",time.toInstant(ZoneOffset.UTC).toEpochMilli() )
                //设置过期时间
                .setExpiration(new Date(System.currentTimeMillis()+QrTime))
                //设置唯一标识
                .setId(UUID.randomUUID().toString())
                //用户标识
                .setSubject(String.valueOf(id))
                //签名
                .signWith(SignatureAlgorithm.HS256,signWith)
                .compact();
        System.out.println("Generated JWT: " + res);
        return res;
    }

    //生成二维码的jwtToken
    public static String getConnectQr(long id, String role, LocalDateTime time, long QrTime, String device){
        String res=  Jwts.builder()
                //类型
                .setHeaderParam("typ","JWT")
                //算法
                .setHeaderParam("alg","HS256")
                .claim("id",id)
                .claim("role",role)
                .claim("device",device)
                .claim("time",time.toInstant(ZoneOffset.UTC).toEpochMilli() )
                //设置过期时间
                .setExpiration(new Date(System.currentTimeMillis()+QrTime))
                //设置唯一标识
                .setId(UUID.randomUUID().toString())
                //用户标识
                .setSubject(String.valueOf(id))
                //签名
                .signWith(SignatureAlgorithm.HS256,signWith)
                .compact();
        return res;
    }

    //获取付款吗token
    public static String getPayQr(long id,LocalDateTime time){
        String res=  Jwts.builder()
                //类型
                .setHeaderParam("typ","JWT")
                //算法
                .setHeaderParam("alg","HS256")
                .claim("id",id)
                .claim("role","user")
                .claim("time",time.toInstant(ZoneOffset.UTC).toEpochMilli() )
                //设置过期时间
                .setExpiration(new Date(System.currentTimeMillis()+payTime))
                //设置唯一标识
                .setId(UUID.randomUUID().toString())
                //用户标识
                .setSubject(String.valueOf(id))
                //签名
                .signWith(SignatureAlgorithm.HS256,signWith)
                .compact();
        return res;
    }

    //设置前端的cookie,并把生成的登录token返回
    public static String setCookie(HttpServletResponse response,Long id, String role,long currentTime){
        String csrfToken = UUID.randomUUID().toString();
        String jwt=getJwt(id,role,csrfToken,currentTime);
        //将jwt添加到httponly cookie
        response.addHeader("Set-Cookie",
                "jwtToken=" + jwt +
                    "; Path=/;" +  // 分号+空格分隔
                    "HttpOnly;" +  // 禁止JS访问
                    "Secure;" +    // HTTPS环境必需
                    "SameSite=None;" +  // 跨域请求允许携带
                    "Max-Age=" + time/1000);
        return csrfToken;
    }

    //专门检查jwt是否合法的
    public static Claims checkJwt(String jwt){
        if(jwt==null){
            return null;
        }
        JwtParser parser = Jwts.parser();
        return parser.setSigningKey(signWith).parseClaimsJws(jwt).getBody();
    }

    //过滤器专门检查jwt是否合法的
    public static Claims checkJwt(LoginTokenService service,String jwt,String ip){
        if(jwt==null){
            return null;
        }
        JwtParser parser = Jwts.parser();
        try{
            return parser.setSigningKey(signWith).parseClaimsJws(jwt).getBody();
        } catch (ExpiredJwtException | SignatureException e){
            service.addIpRequest(ip,"",-1,System.currentTimeMillis());
            throw e;
        }
    }


    public static long getId(String jwt){
        Claims claims=checkJwt(jwt);
        if(jwt==null||jwt.isEmpty()||claims==null){
            throw new NotLoggedInException("未登录，请登录后进行操作");
        }
        return Long.parseLong(claims.get("id").toString());
    }

    //在cookie中找到jwt并进行检查,返回角色
    public static String findJwt(HttpServletRequest request, Cookie[] cookies, String wantRole, LoginTokenService loginTokenService,boolean fromFilter){
        if(cookies==null)
            throw new NotLoggedInException("未登录，请登录后进行操作");
        String jwt=null;
        long currentTime=System.currentTimeMillis();
        //遍历 Cookie，找到存储 JWT 的那个
        for(Cookie cookie:cookies){
            if("jwtToken".equals(cookie.getName())){
                jwt = cookie.getValue();
                break;
            }
        }
        Claims claims;
        String uri = request.getRequestURI();
        boolean check=fromFilter&&Arrays.asList(loginApis).contains(uri);
        if(check) {
            if(DealWithRequestTool.checkFrequency(request,loginTokenService,-1,"",currentTime))
                claims = checkJwt(loginTokenService, jwt, request.getRemoteAddr());
            else
                throw new RequestTooMuchTime("访问频率过快");
        }
        else
            claims=checkJwt(jwt);
        if(jwt==null||jwt.isEmpty()||claims==null){
            throw new NotLoggedInException("未登录，请登录后进行操作");
        }
        //获取用户的信息
        String role=claims.get("role").toString();
        long id=Long.parseLong(claims.get("id").toString());
        long getTime=Long.parseLong(claims.get("time").toString());
        String key=hashLoginInfo(role,id);
        if(!check&&!DealWithRequestTool.checkFrequency(request,loginTokenService,id,role,currentTime)){
            throw new RequestTooMuchTime("访问频率过快");
        }
        long startValidTime=loginTokenService.getTokenStartValidTime(key);
        //token的创建时间必须再指定的有效时间之后
        if(getTime<startValidTime){
            throw new IllegalLoginTokenError("当前登录已经失效");
        }
        //设置权限
        JwtTool.setAuthentication(id,role);
        checkCsrf(request,claims);
        TokenContext.setCurrentId(id);
        if(wantRole!=null&&wantRole.equals("manager")){
            if(!role.equals("manager"))
                throw new HaveNotAdminAuthority("没有管理员权限");
        }
        return role;
    }


    //检查csrf结果是否
    public static void checkCsrf(HttpServletRequest request, Claims claims){
        String csrfToken=claims.get("csrfToken").toString();
        String csrfTokenHeader = request.getHeader("X-CSRF-TOKEN");
        if(csrfTokenHeader==null||csrfToken==null||csrfTokenHeader.isEmpty()||csrfToken.isEmpty()||!csrfToken.equals(hmacSha256(csrfTokenHeader))){
            throw new WrongCsrfError("权限错误，请重新登录");
        }
    }

    //设置认证令牌
    public static void setAuthentication(long id,String authority){
        //创建权限令牌
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority("ROLE_"+authority);
        //创建身份证
        Authentication authentication=new UsernamePasswordAuthenticationToken(id,null, Collections.singletonList(simpleGrantedAuthority));
        SecurityContext securityContext=SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    //加密
    public static String hmacSha256(String data) {
        try {
            Mac hmac=Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec=new SecretKeySpec(signRandom.getBytes(StandardCharsets.UTF_8),"HmacSHA256");
            hmac.init(secretKeySpec);
            byte[] hash=hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    //对一给角色的身份和id进行获取哈希值
    public static String hashLoginInfo(String role,long id){
        try {
            Mac hmac=Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec=new SecretKeySpec(signInRedis.getBytes(StandardCharsets.UTF_8),"HmacSHA256");
            hmac.init(secretKeySpec);
            String combine=role+"|"+id;
            byte[] hash=hmac.doFinal(combine.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
}
