package org.powernode.springboot.controller;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletResponse;
import org.powernode.springboot.bean.database.ProcessBook;
import org.powernode.springboot.bean.vo.BooksStorage;
import org.powernode.springboot.bean.vo.Chart;
import org.powernode.springboot.bean.vo.ShowBook;
import org.powernode.springboot.bean.vo.UserInfo;
import org.powernode.springboot.service.database.service.BookService;
import org.powernode.springboot.service.database.service.BooksService;
import org.powernode.springboot.service.database.service.ProcessBookService;
import org.powernode.springboot.service.database.service.UserService;
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
    //一个用户认证二维码的有效时长
    private static final long QRTime=1000*60*3;
    UserController(UserService userService, BooksService booksService, ProcessBookService processBookService, BookService bookService) {
        this.userService = userService;
        this.booksService = booksService;
        this.processBookService = processBookService;
        this.bookService = bookService;
    }
    //验证账号是否正确
    @PostMapping("/checkUserPassword")
    ResponseEntity<?> checkPassword(@RequestParam long id, @RequestParam String password, HttpServletResponse response){
        logger.info("用户{}正在登录中。。", id);
        if(userService.checkPassword(id,password)){
            JwtTool.setCookie(response,id,"user");
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
        if(processBookService.insertRecord(processBook)>0)
            return ResponseEntity.status(HttpStatus.OK).body("提交成功");
        else
            return ResponseEntity.status(HttpStatus.valueOf(500)).body("传输失败，请联系管理员1074702558@qq.com");
    }

    @GetMapping("/getUseInfo")
    ResponseEntity<UserInfo> getUseInfo(){
        return ResponseEntity.status(200).body(userService.getUserInfo(TokenContext.getCurrentId()));
    }

    @GetMapping("/generateQrToken")
    ResponseEntity<String> generateQrToken(){
        LocalDateTime time = LocalDateTime.now();
        return ResponseEntity.status(200).body(JwtTool.getQR(TokenContext.getCurrentId(),"user",time,QRTime));
    }

    @GetMapping("/generateTestQrToken")
    ResponseEntity<String> generateQrToken(long id){
        LocalDateTime time = LocalDateTime.now();
        return ResponseEntity.status(200).body(JwtTool.getQR(id,"user",time,QRTime));
    }

    @GetMapping("/getBookRecord")
    ResponseEntity<List<ShowBook>> getBookRecord(){
        return ResponseEntity.status(200).body(bookService.selectSomeoneBook(TokenContext.getCurrentId()));
    }
}
