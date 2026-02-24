package util;

import service.AccountService;

public class TransferTask implements Runnable{
    
    private AccountService service;
    private String from;
    private String to;
    private double amount;

    public TransferTask(AccountService service, String from, String to, double amount){
        this.service = service;
        this.from = from;
        this.amount = amount;
    }

    @Override
    public void run(){
        try{
            service.transfer(from, to, amount);
            System.out.println(Thread.currentThread().getName()
                    + "transfer success");
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
