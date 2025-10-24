package org.powernode.springboot.websocket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.powernode.springboot.exception.LackOfInfo;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceInfo {
    private String purpose="";
    private double balance=0.0;
    private int serviceType=0;

    public ServiceInfo(String purpose, double balance) {
        this.purpose = purpose;
        this.balance = balance;
    }

    public void clearInfo(){
        purpose="";
        balance=0.0;
        serviceType=-1;
    }

    public void checkPayInfo(){
        if(purpose.isEmpty()){
            throw new LackOfInfo("缺少目的信息");
        }
        else if(balance==0.0){
            throw new LackOfInfo("缺少交易金额信息");
        }
    }

    public void checkActivateServiceInfo(){
        if(serviceType<=0){
            throw new LackOfInfo("缺少要开通的服务的信息");
        }
        checkPayInfo();
    }

}
