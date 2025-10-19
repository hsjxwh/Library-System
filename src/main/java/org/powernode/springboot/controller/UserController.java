package org.powernode.springboot.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.powernode.springboot.bean.database.ProcessBook;
import org.powernode.springboot.bean.database.User;
import org.powernode.springboot.bean.vo.*;
import org.powernode.springboot.service.database.service.mysql.*;
import org.powernode.springboot.service.database.service.redis.LoginTokenService;
import org.powernode.springboot.service.database.service.redis.RegisterService;
import org.powernode.springboot.service.mail.LoginService;
import org.powernode.springboot.tool.JwtTool;
import org.powernode.springboot.tool.TokenContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final BooksService booksService;
    private final ProcessBookService processBookService;
    private final BookService bookService;
    private final OrdersService ordersService;
    private final LoginTokenService loginTokenService;
    private final LoginService loginService;
    private final RegisterService registerService;
    //一个用户认证二维码的有效时长
    private static final long QRTime=1000*60*3;
    UserController(UserService userService, BooksService booksService, ProcessBookService processBookService, BookService bookService, OrdersService ordersService, LoginTokenService loginTokenService, LoginService loginService, RegisterService registerService) {
        this.userService = userService;
        this.booksService = booksService;
        this.processBookService = processBookService;
        this.bookService = bookService;
        this.ordersService = ordersService;
        this.loginTokenService = loginTokenService;
        this.loginService = loginService;
        this.registerService = registerService;
    }
    //验证账号是否正确
    @PostMapping("/checkUserPassword")
    ResponseEntity<?> checkPassword(@RequestParam long id, @RequestParam String password, HttpServletResponse response){
        logger.info("用户{}正在登录中。。", id);
        long currentTime=System.currentTimeMillis();
        if(userService.checkPassword(id,password)){
            JwtTool.setCookie(response,id,"user",currentTime);
            logger.info("将用户{}假如张倩网站在线用户人数列表",id);
            loginTokenService.addOnlineCount("user",id,currentTime);
            logger.info("用户{}登录成功", id);
            return ResponseEntity.status(200).body("登录成功");
        }
        logger.info("用户{}登录失败",id);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("账号/密码错误");
    }

    @GetMapping("/getAllLibraryStorage")
    ResponseEntity<List<BooksStorage>> getAllStorage(@RequestParam String name,@RequestParam String author,@RequestParam String type,@RequestParam String hasBorrow,@RequestParam(required = false) String[] head){
        logger.info("用户{}正在获取馆藏数据", TokenContext.getCurrentId());
        List<BooksStorage> obj=booksService.selectAllBooksInCondition(name,author,type,hasBorrow,head);
        return ResponseEntity.status(HttpStatus.valueOf(200)).body(obj);
    }

    @GetMapping("/getCharts")
    ResponseEntity<List<Chart>> getCharts(){
        logger.info("用户{}正在获取排行榜的数据", TokenContext.getCurrentId());
        List<Chart> res=booksService.getCharts();
        return ResponseEntity.status(HttpStatus.valueOf(200)).body(res);
    }

    @PostMapping("/postRecommendToStore")
    ResponseEntity<String> postRecommendToStore(@RequestParam String name,@RequestParam String author,@RequestParam String type,@RequestParam String description){
        long id=TokenContext.getCurrentId();
        logger.info("用户{}正在提交推荐书籍:{}的{}",id,author,name);
        ProcessBook processBook=new ProcessBook(name,author,id,description,type, LocalDateTime.now());
        if(processBookService.insertRecord(processBook)>0) {
            logger.info("用户{}提交推荐书籍:{}的{}成功",id,author,name);
            return ResponseEntity.status(HttpStatus.OK).body("提交成功");
        }
        else {
            logger.info("用户{}提交推荐书籍:{}的{}失败",id,author,name);
            return ResponseEntity.status(HttpStatus.valueOf(500)).body("传输失败，请联系管理员1074702558@qq.com");
        }
    }

    @GetMapping("/getUseInfo")
    ResponseEntity<UserInfo> getUseInfo(){
        long id=TokenContext.getCurrentId();
        logger.info("编号为{}的用户请求获取他的信息",id);
        return ResponseEntity.status(200).body(userService.getUserInfo(id));
    }

    @GetMapping("/generateQrToken")
    ResponseEntity<String> generateQrToken(){
        LocalDateTime time = LocalDateTime.now();
        long id=TokenContext.getCurrentId();
        logger.info("编号为{}的用户请求获取他的身份码",id);
        return ResponseEntity.status(200).body(JwtTool.getConnectQr(id,"user",time,QRTime));
    }

    @GetMapping("/generateTestQrToken")
    ResponseEntity<String> generateQrToken(long id){
        LocalDateTime time = LocalDateTime.now();
        logger.info("生成编号为{}的用户的测试省份码",id);
        return ResponseEntity.status(200).body(JwtTool.getConnectQr(id,"user",time,QRTime));
    }

    @GetMapping("/getBookRecord")
    ResponseEntity<List<ShowBook>> getBookRecord(){
        long id=TokenContext.getCurrentId();
        logger.info("编号为{}的用户请求获取他的借阅记录",id);
        return ResponseEntity.status(200).body(bookService.selectSomeoneBook(id));
    }

    @GetMapping("/getOrdersRecord")
    ResponseEntity<List<UserShowOrders>> getOrdersRecord(){
        long id=TokenContext.getCurrentId();
        logger.info("编号为{}的用户请求获取他的订单列表",id);
        return ResponseEntity.status(200).body(ordersService.userGetAllOrders(id));
    }

    //测试支付的程序
    @GetMapping("/getPayQr")
    ResponseEntity<String> getPayQrToken(long id,double money,String purpose){
        LocalDateTime time = LocalDateTime.now();
        logger.info("编号为{}的用户请求获取他的支付码，支付金额为{}元",id,money);
        return ResponseEntity.status(200).body(JwtTool.getPayQr(id,time,money));
    }

    @GetMapping("/getRegisterVerification")
    ResponseEntity<String> getRegisterVerification(String email){
        if(loginService.sendVerification(email))
            return ResponseEntity.status(200).body("发送验证码成功，三分钟内有效");
        else
            return ResponseEntity.status(429).body("验证码已发送过，若无请检查邮箱号是否正确，验证码失效后方可继续发送");
    }

    @PostMapping("/register")
    ResponseEntity<Long> register(@RequestParam String email,@RequestParam String token,@RequestParam String name,@RequestParam String password){
        if(registerService.checkVerifyCode(email,token)){
            User user=new User(name,password,email);
            userService.insertUser(user);
            return ResponseEntity.status(200).body(user.getId());
        }
        else
            return ResponseEntity.status(401).body(-1L);
    }

    @PostMapping("/quit")
    ResponseEntity<String> forceSomeoneQuit(){
        long currentTime=System.currentTimeMillis();
        long id=TokenContext.getCurrentId();
        logger.info("编号为{}的用户请求下线",id);
        String key=JwtTool.hashLoginInfo("user",id);
        try {
            loginTokenService.setTokenStartValidTime(key,currentTime);
            return ResponseEntity.status(200).body("成功下线");
        }catch (Exception e){
            return ResponseEntity.status(500).body("操作异常");
        }
    }
}
