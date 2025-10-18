package org.powernode.springboot.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.powernode.springboot.bean.database.*;
import org.powernode.springboot.bean.vo.ImportBooksByExcelRes;
import org.powernode.springboot.bean.vo.ManagerShowOrders;
import org.powernode.springboot.bean.vo.RenewMessage;
import org.powernode.springboot.bean.vo.ShowBook;
import org.powernode.springboot.exception.RequestTooMuchTime;
import org.powernode.springboot.service.database.service.mysql.*;
import org.powernode.springboot.service.database.service.redis.LoginTokenService;
import org.powernode.springboot.service.excel.BooksExcelService;
import org.powernode.springboot.tool.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/manager")
public class ManagerController {
    private static final Logger logger = LoggerFactory.getLogger(ManagerController.class);
    private final ManagerService managerService;
    private final BooksExcelService booksExcelService;
    private final BooksService booksService;
    private final ProcessBookService processBookService;
    private final PurchaseBooksService purchaseBooksService;
    private final BookService bookService;
    private final ScoreService scoreService;
    private final OrdersService ordersService;
    //一此jwtToken的有效时长
    private final long websocketConnectToken=1000*60*8;
    private final UserService userService;
    private final LoginTokenService loginTokenService;

    ManagerController(ManagerService managerService, BooksExcelService booksExcelService, BooksService booksService, ProcessBookService processBookService, PurchaseBooksService purchaseBooksService, BookService bookService, ScoreService scoreService, OrdersService ordersService, UserService userService, LoginTokenService loginTokenService) {
        this.managerService = managerService;
        this.booksExcelService = booksExcelService;
        this.booksService = booksService;
        this.processBookService = processBookService;
        this.purchaseBooksService = purchaseBooksService;
        this.bookService = bookService;
        this.scoreService = scoreService;
        this.ordersService = ordersService;
        this.userService = userService;
        this.loginTokenService = loginTokenService;

    }

    @PostMapping("/checkManagerPassword")
    ResponseEntity<?> checkPassword(@RequestParam long id, @RequestParam String password, HttpServletResponse response) {
        logger.info("管理员{}正在登录中",id);
        long currentTime=System.currentTimeMillis();
        if(managerService.checkPassword(id,password)){
            JwtTool.setCookie(response,id,"manager",currentTime);
            logger.info("将管理员{}假如张倩网站在线用户人数列表",id);
            loginTokenService.addOnlineCount("manager",id,currentTime);
            logger.info("管理员{}登录成功",id);
            return ResponseEntity.status(200).body("登录成功");
        }
        logger.info("管理员{}登录失败",id);
        return ResponseEntity.status(HttpStatusCode.valueOf(401)).body("账号/密码错误");
    }

    @PostMapping("/checkIsManager")
    ResponseEntity<String> checkIsManager(HttpServletRequest request, HttpServletResponse response) {
        logger.info("正在验证是否有管理员{}的权限", TokenContext.getCurrentId());
        if(DealWithRequestTool.checkFrequency(request,loginTokenService,TokenContext.getCurrentId(),"manager",System.currentTimeMillis())) {
            JwtTool.findJwt(request,request.getCookies(),"manager",loginTokenService,false);
            return ResponseEntity.status(200).body("拥有权限");
        }
        else
            throw new RequestTooMuchTime("当前账号访问频率过快");
    }

    //讲管理员上传的excel表格导入到数据库中
    @PostMapping("/submitMaterialsToAddBooks")
    ResponseEntity<ImportBooksByExcelRes> submitMaterials(@RequestParam("file") MultipartFile file){
        logger.info("管理员{}正在提交excel表格来增加馆藏",TokenContext.getCurrentId());
        //检查文件是否为空，是否是excel文件，是否大小超过10MB
        FileTool.isFileNotEmpty(file);
        FileTool.isExcelFile(file);
        FileTool.checkSize(file);
        ImportBooksByExcelRes res;
        try(InputStream inputStream=file.getInputStream()){
            res=booksExcelService.importBooksFromExcel(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.status(200).body(res);
    }

    //管理员按照书本的单位讲新书籍放入数据库
    @PostMapping("/submitToAddBook")
    ResponseEntity<String> submitBook(String name,String author,String type,double price,String head,String description){
        logger.info("管理员{}正在增加新书籍,作者是{},书名是{}",TokenContext.getCurrentId(),author,name);
        Books books = new Books(0,price,type,description,"在馆",author,name,head);
        if(booksService.insertBook(books)>0){
            logger.info("管理员{}增加新书籍成功,作者是{},书名是{}",TokenContext.getCurrentId(),author,name);
            return ResponseEntity.status(200).body("加入成功");
        }
        else {
            logger.info("管理员{}增加新书籍失败,作者是{},书名是{}",TokenContext.getCurrentId(),author,name);
            return ResponseEntity.status(500).body("加入失败，请联系管理员");
        }
    }

    @DeleteMapping("/deleteBook")
    ResponseEntity<String> deleteBook(@RequestParam String name,@RequestParam String author){
        logger.info("管理员{}正在删除书名为{}、作者为{}的书籍",TokenContext.getCurrentId(),name,author);
        if(booksService.deleteBook(name,author)>0){
            logger.info("管理员{}删除书名为{}、作者为{}的书籍成功",TokenContext.getCurrentId(),name,author);
            return ResponseEntity.status(200).body("删除成功");
        }
        else{
            logger.info("管理员{}删除书名为{}、作者为{}的书籍失败",TokenContext.getCurrentId(),name,author);
            return ResponseEntity.status(500).body("删除失败，请联系管理员");
        }
    }

    //传送模板excel文件给管理员
    @GetMapping("/downloadTemplateFile")
    ResponseEntity<byte[]> downloadTemplateFile(){
        try {
            logger.info("管理员{}在获取增加书籍的模板excel文件",TokenContext.getCurrentId());
            Resource resource=new ClassPathResource("templates/template.xlsx");
            if(!resource.exists()){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            Path path= Paths.get(resource.getURI());
            //获取文件的二进制数据，字节数组
            byte[] bytes = Files.readAllBytes(path);
            HttpHeaders headers = new HttpHeaders();
            String filename =  java.net.URLEncoder.encode("图书导入数据库的excel模板文件.xlsx", "UTF-8");
            headers.add("Content-Disposition", "attachment; filename*=UTF-8''" + filename);
            headers.add("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/getAllRecommendWaitToProcess")
    public ResponseEntity<List<ProcessBook>> getAllRecommendWaitToProcess(){
        logger.info("管理员{}增在获取等待审核的读者荐书列表",TokenContext.getCurrentId());
        List<ProcessBook> res=processBookService.selectAllRecords();
        return ResponseEntity.status(200).body(res);
    }

    @DeleteMapping("/deleteRecommendToStore")
    ResponseEntity<String> deleteRecommendToStore(@RequestParam long id, @RequestParam LocalDateTime time){
        logger.info("管理员{}正在删除id为{}在{}提交的读者荐书",TokenContext.getCurrentId(),id,time);
        if(processBookService.deleteRecord(id,time)>0){
            logger.info("管理员{}删除id为{}在{}提交的读者荐书成功",TokenContext.getCurrentId(),id,time);
            return ResponseEntity.status(200).body("删除成功");
        }
        else {
            logger.info("管理员{}删除id为{}在{}提交的读者荐书失败",TokenContext.getCurrentId(),id,time);
            return ResponseEntity.status(500).body("删除失败，请联系管理员");
        }
    }

    @PostMapping("/addPurchaseBook")
    ResponseEntity<String> insertPurchaseBook(@RequestParam String name,@RequestParam String author,@RequestParam String type,@RequestParam String head,@RequestParam String description){
        PurchaseBooks purchaseBooks=new PurchaseBooks(name,author,description,head,type);
        logger.info("管理员{}正在增加书籍到待购买书籍列表，书名为{},作者为{}",TokenContext.getCurrentId(),name,author);
        if(purchaseBooksService.insertRecord(purchaseBooks)>0){
            logger.info("管理员{}增加书籍到待购买书籍列表成功，书名为{},作者为{}",TokenContext.getCurrentId(),name,author);
            return ResponseEntity.status(200).body("加入采购列表成功");
        }
        else {
            logger.info("管理员{}增加书籍到待购买书籍列表失败，书名为{},作者为{}",TokenContext.getCurrentId(),name,author);
            return ResponseEntity.status(500).body("删除失败，请联系管理员");
        }
    }

    @GetMapping("/getAllPurchaseBooks")
    ResponseEntity<List<PurchaseBooks>> getAllPurchaseBooks(){
        logger.info("管理员{}正在获取待购买书籍列表",TokenContext.getCurrentId());
        return ResponseEntity.status(200).body(purchaseBooksService.selectAllRecords());
    }

    @DeleteMapping("/deletePurchaseBook")
    ResponseEntity<String> deletePurchaseBook(@RequestParam String author,@RequestParam String name){
        logger.info("管理员{}正在删除待采购列表的书籍，书名为{},作者为{}",TokenContext.getCurrentId(),name,author);
        if(purchaseBooksService.deleteRecord(author,name)>0){
            logger.info("管理员{}删除待采购列表的书籍成功，书名为{},作者为{}",TokenContext.getCurrentId(),name,author);
            return ResponseEntity.status(200).body("操作成功");
        }
        logger.info("管理员{}删除待采购列表的书籍失败，书名为{},作者为{}",TokenContext.getCurrentId(),name,author);
        return ResponseEntity.status(500).body("操作失败");
    }

    private void logStart(String name,String author,long id){
        logger.info("正在获取书名为"+
                name==null?"?":name.isEmpty()?"?":name+
                "，作者为"+
                author==null?"?":author.isEmpty()?"?":author+
                "读者编号为"+
                (id==-1?"?":String.valueOf(id))+
                "的借阅记录"
        );
    }

    @GetMapping("/getAllBookRecord")
    ResponseEntity<List<ShowBook>> getAllBookRecord(String name, String author, long id){
        logStart(name,author,id);
        List<ShowBook> res=bookService.selectAllBook(name,author,id);
        return ResponseEntity.status(200).body(res);
    }

    @PostMapping("/addBookRecord")
    ResponseEntity<String> addBookRecord(long bookId,long id){
        logger.info("管理员{}正在插入读者编号为{}的读者借阅图书编号为{}的书籍的记录",TokenContext.getCurrentId(),id,bookId);
        LocalDateTime now=LocalDateTime.now();
        LocalDateTime after30Days = now.plusDays(30);
        if(bookService.insertBook(bookId,id,TokenContext.getCurrentId(),now,after30Days)>0){
            logger.info("管理员{}插入读者编号为{}的读者借阅图书编号为{}的书籍的记录成功",TokenContext.getCurrentId(),id,bookId);
            return ResponseEntity.status(200).body("加入记录成功");
        }
        else{
            logger.info("管理员{}插入读者编号为{}的读者借阅图书编号为{}的书籍的记录失败",TokenContext.getCurrentId(),id,bookId);
            return ResponseEntity.status(500).body("操作失败");
        }
    }

    @PostMapping("/renewBook")
    ResponseEntity<RenewMessage> renewBook(long id, long userId, long bookId, String name, String author, LocalDateTime expectedReturnTime){
        long managerId=TokenContext.getCurrentId();
        logger.info("管理管{}正在给用户{}续借{}的{}",managerId,userId,author,name);
        expectedReturnTime=expectedReturnTime.plusDays(30);
        if(bookService.updateBook(id,bookId,userId,null,expectedReturnTime,0,managerId)>0){
            logger.info("管理管{}给用户{}续借{}的{}成功",managerId,userId,author,name);
            return ResponseEntity.status(200).body(new RenewMessage(expectedReturnTime,"续借成功"));
        }
        else{
            logger.info("管理管{}给用户{}续借{}的{}失败",managerId,userId,author,name);
            return ResponseEntity.status(500).body(new RenewMessage(null,"操作失败"));
        }
    }

    @GetMapping("/generateQrToken")
    ResponseEntity<String> generateQrToken(String device){
        logger.info("编号为{}的管理员正在获取websocket连接的token",TokenContext.getCurrentId());
        LocalDateTime time = LocalDateTime.now();
        if(!device.equals("pc")){
            return ResponseEntity.status(400).body("只有电脑端能够获取websocket连接所需要的token");
        }
        return ResponseEntity.status(200).body(JwtTool.getConnectQr(TokenContext.getCurrentId(),"manager",time,websocketConnectToken,device));
    }

    @PostMapping("/returnBook")
    ResponseEntity<String> returnBook(long id,long userId,int bookId,String name,String author,LocalDateTime expectedReturnTime){
        double refund;
        LocalDateTime now=LocalDateTime.now();
        if(TimeTool.needDeductDeposit(expectedReturnTime,now)){
            int betweenDays=TimeTool.daysBetween(now,expectedReturnTime);
            refund=-betweenDays*1*(1+0.01*betweenDays);
        }
        else
            refund=0.0;
        long managerId=TokenContext.getCurrentId();
        logger.info("管理管{}正在给用户{}归还{}的{}",managerId,userId,author,name);
        if(bookService.updateBook(id,bookId,userId,now,null,refund,managerId)>0){
            logger.info("管理管{}给用户{}归还{}的{}成功",managerId,userId,author,name);
            return ResponseEntity.status(200).body("归还成功");
        }
        else{
            logger.info("管理管{}给用户{}归还{}的{}失败",managerId,userId,author,name);
            return ResponseEntity.status(500).body("操作失败");
        }
    }

    @GetMapping("/getAllOrders")
    ResponseEntity<List<ManagerShowOrders>> getOrdersRecord(){
        logger.info("编号为{}的管理员请求获取所有订单记录",TokenContext.getCurrentId());
        return ResponseEntity.status(200).body(ordersService.getAllOrders());
    }

    @GetMapping("/getSomeoneOrders")
    ResponseEntity<List<ManagerShowOrders>> getSomeoneOrders(long userId){
        logger.info("编号为{}的管理员请求获取编号为{}的用户的所有订单记录",TokenContext.getCurrentId(),userId);
        return ResponseEntity.status(200).body(ordersService.managerGetSomeoneOrders(userId));
    }

    @GetMapping("/getSpecialOrder")
    ResponseEntity<List<ManagerShowOrders>> getSpecialOrder(long id){
        logger.info("编号为{}的管理员请求获取编号为{}的订单记录",TokenContext.getCurrentId(),id);
        ManagerShowOrders managerShowOrders=ordersService.managerGetOrdersByOrder(id);
        return ResponseEntity.status(200).body(managerShowOrders==null?new ArrayList<>():List.of(managerShowOrders));
    }

    @PostMapping("/updateUserBalance")
    ResponseEntity<String> updateUserBalance(long id,double balance){
        logger.info("编号为{}的管理员正在处理id为{}的用户的订单，导致余额{}{}元", TokenContext.getCurrentId(), id, balance > 0 ? "加" : "减", balance);
        if(userService.updateBalance(id,balance)>0){
            logger.info("编号为{}的管理员成功处理id为{}的用户的订单，导致余额{}{}元", TokenContext.getCurrentId(), id, balance > 0 ? "加" : "减", balance);
            return ResponseEntity.status(200).body("处理成功，编号为"+id+"的用户余额"+(balance > 0 ? "加" : "减")+balance+"元");
        }
        else{
            logger.info("编号为{}的管理员处理id为{}的用户的订单失败，余额不变", TokenContext.getCurrentId(), id);
            return ResponseEntity.status(500).body("处理失败，编号为"+id+"的用户余额不变");
        }
    }

    @GetMapping("/getAllOnlineAccounts")
    ResponseEntity<List<OnlineAccount>> getAllOnlineAccount(){
        long currentTime=System.currentTimeMillis();
        logger.info("编号为{}的管理员正在获取当前网站所有在线账号",TokenContext.getCurrentId());
        List<OnlineAccount> accounts=loginTokenService.getAllOnlineAccount(currentTime);
        return ResponseEntity.status(200).body(accounts);
    }

    @PostMapping("/forceSomeoneQuit")
    ResponseEntity<String> forceSomeoneQuit(long id,String role){
        long currentTime=System.currentTimeMillis();
        logger.info("编号为{}的管理员正在强制账号为{}的{}下线",TokenContext.getCurrentId(),id,role);
        String key=JwtTool.hashLoginInfo(role,id);
        try {
           loginTokenService.setTokenStartValidTime(key,currentTime);
           return ResponseEntity.status(200).body("成功强制某用户下线");
        }catch (Exception e){
           return ResponseEntity.status(500).body("操作异常");
        }
    }

    @GetMapping("/getBlackList")
    ResponseEntity<List<BlackListAccount>> getBlackList(){
        logger.info("编号为{}的管理员正在获取当前网站的黑名单ip",TokenContext.getCurrentId());
        List<BlackListAccount> blackListAccounts=loginTokenService.getBlackListAccount();
        return ResponseEntity.status(200).body(blackListAccounts);
    }

    @PostMapping("/pullOutTheBlacklist")
    ResponseEntity<String> pullOutTheBlacklist(String ip){
        logger.info("编号为{}的管理员正在将ip为{}的账号移出当前网站的黑名单ip",TokenContext.getCurrentId(),ip);
        try {
            loginTokenService.removeBlackListAccount(ip);
            return ResponseEntity.status(200).body("成功拉出黑名单");
        }catch (Exception e){
            return ResponseEntity.status(500).body("拉出黑名单失败");
        }
    }


}
