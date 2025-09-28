package org.powernode.springboot.service.database.service.impl;

import org.powernode.springboot.annotation.TransactionFail;
import org.powernode.springboot.bean.vo.ShowBook;
import org.powernode.springboot.exception.InsufficientCreditError;
import org.powernode.springboot.exception.RenewManyTimeError;
import org.powernode.springboot.mapper.database.*;
import org.powernode.springboot.service.database.service.BookService;
import org.powernode.springboot.service.database.service.BorrowTimeService;
import org.powernode.springboot.service.database.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private BooksMapper booksMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ScoreMapper scoreMapper;
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private BorrowTimeService borrowTimeService;
    @Override
    @Transactional
    @TransactionFail
    public int insertBook(long bookId,long userId,long managerId1,LocalDateTime time,LocalDateTime expectedReturnTime) {
        checkBorrowLegal(userId);
        if(bookMapper.insertBook(bookId,userId,managerId1,time,expectedReturnTime)<=0)
            return -1;
        int res= booksMapper.updateState(bookId,"已借出");
        if(res<=0)
            return -1;
        int borrowTime=userMapper.getBorrow(userId);
        int serviceType=userMapper.getServiceType(userId);
        if(!(serviceType==1&&borrowTime<10)&&!(serviceType==2&&borrowTime<20)){


        }
        res=userMapper.updateBorrow(userId,borrowTime+1);
        if(res<=0)
            return -1;
        else
            return borrowTimeService.updateBorrow(bookId);
    }

    @Override
    @Transactional
    @TransactionFail
    public int deleteBook(long id) {
        if(!hasThisBook(id))
            return 1;
        int res=bookMapper.deleteBook(id);
        if(bookMapper.hasOrders(id)>0){
            res=ordersService.deleteOrders(bookMapper.getBookOrderId(id));
        }
        return res;
    }

    @Override
    @Transactional
    @TransactionFail
    public int deleteAllBooksBook(long bookId) {
        List<Integer> book=bookMapper.selectAllBooksBook(bookId);
        if(book.isEmpty())
            return 1;
        for(Integer i:book){
            if(deleteBook(i)<=0)
                return -1;
        }
        return 1;
    }

    @Override
    @Transactional
    @TransactionFail
    //expectedReturnTime实时续借后的到期时间
    public int updateBook(long id,long bookId,long userId, LocalDateTime returnTime,LocalDateTime expectedReturnTime, double deposit,long managerId) {
        if (bookMapper.updateBook(id, returnTime, expectedReturnTime,deposit,managerId)<=0)
            return -1;
        //没有续借后的书籍应该归还日期，说明是还书
        if(expectedReturnTime==null){
            int res= booksMapper.updateState(bookId,"在馆");
            if(deposit>0.00){
                res=ordersService.insertOrders(userId,managerId,deposit,-1,"逾期还书，扣除押金",returnTime);
            }
            return res;
        }
        //否则就是续借
        else{
            checkBorrowLegal(userId);
            checkRenewLegal(userId);
            return bookMapper.updateReturnTime(id,bookMapper.getRenewTime(id)+1);
        }
    }

    @Override
    @Transactional
    public boolean hasThisBook(long id) {
        return bookMapper.hasThisBook(id)>0;
    }

    @Override
    @Transactional
    public long getBooksBookNum(long id) {
        return bookMapper.getBooksBookNum(id);
    }

    @Override
    @Transactional
    public List<ShowBook> selectAllBook() {
        return bookMapper.selectAllBook();
    }

    @Override
    @Transactional
    public List<ShowBook> selectSomeoneBook(long userId) {
        return bookMapper.selectSomeoneBook(userId);
    }

    @Override
    @Transactional
    public List<ShowBook> selectAllBook(String name, String author, long id) {
        return bookMapper.selectAllBookRecord(name, author, id);
    }

    @Override
    @Transactional
    public boolean hasOrder(long id) {
        return bookMapper.hasOrders(id)>0;
    }

    @Override
    @Transactional
    public long getBookOrderId(long id) {
        return bookMapper.getBookOrderId(id);
    }

    @Transactional
    void checkBorrowLegal(long userId){
        int score=scoreMapper.selectScore(userId);
        if(score<60)
            throw new InsufficientCreditError("用户"+userId+"信用分不足60:"+score);
        double balance=userMapper.getBalance(userId);
        if(balance<20)
            throw new BalanceNotEnoughError("用户0"+userId+"余额不足20，请充值后继续进行服务，当前余额:"+balance);
    }

    @Transactional
    void checkRenewLegal(long id){
        int renewTime=bookMapper.getRenewTime(id);
        if(renewTime>=2)
            throw new RenewManyTimeError("在一次借阅记录中，单本书只能续借两次，想继续借只能先还了再借书.");
    }
}
