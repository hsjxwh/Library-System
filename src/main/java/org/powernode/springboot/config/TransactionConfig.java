package org.powernode.springboot.config;

import org.powernode.springboot.aspect.TransactionAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableAspectJAutoProxy
public class TransactionConfig {
    public TransactionAspect transactionAspect() {
        return new TransactionAspect();
    }
}
