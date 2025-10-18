package org.powernode.springboot.fliter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.powernode.springboot.controller.ManagerController;
import org.powernode.springboot.exception.AuthorityError;
import org.powernode.springboot.exception.NotLoggedInException;
import org.powernode.springboot.exception.RequestTooMuchTime;
import org.powernode.springboot.fliter.treatError.TreatError;
import org.powernode.springboot.service.database.service.redis.LoginTokenService;
import org.powernode.springboot.tool.DealWithRequestTool;
import org.powernode.springboot.tool.JwtTool;
import org.powernode.springboot.tool.TokenContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
//检测jwt的过滤器
@Component
public class JwtFilter extends OncePerRequestFilter {
    @Value("${jwt.whitelist}")
    List<String> excludedPaths;
    @Value("${websocket.path}")
    String websocketPath;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();
    private final LoginTokenService loginTokenService;
    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
    public JwtFilter(LoginTokenService loginTokenService) {
        this.loginTokenService = loginTokenService;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String ip=request.getRemoteAddr();
        if(loginTokenService.isInBlackList(ip))
            return;
        String url= request.getServletPath();
        //对于不需要jwt的请求，直接放行
        for(String excludedPath : excludedPaths) {
            if(antPathMatcher.match(excludedPath, url)) {
                System.out.println("命中白名单，直接放行：" + excludedPath);
                filterChain.doFilter(request, response);
                return;
            }
        }
        try {
            Cookie[] cookies = request.getCookies();
            JwtTool.findJwt(request,cookies,null,loginTokenService,true);
            filterChain.doFilter(request, response);
        }
        //由于全局处理异常值处理控制器的异常，因此需要额外写
        catch (ExpiredJwtException e) {
            TreatError.handleJwtException(response,AuthorityError.treatExpiredJwtException());
        } catch (SignatureException e) {
            TreatError.handleJwtException(response,AuthorityError.treatSignatureException());
        }catch (NotLoggedInException e){
            TreatError.handleJwtException(response,AuthorityError.treatNotLoggedInException());
        }finally {
            //以免使用完后返回线程池，又被其他请求使用的时候，影响器数据正确性
            TokenContext.clear();
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.startsWith(websocketPath);
    }
}
