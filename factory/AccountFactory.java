package factory;

import model.*;

public class AccountFactory {
    
    public static Account createAccount(String type, String accNo, String name, double balance){

        if(type.equalsIgnoreCase("SAVINGS"))
            return new SavingsAccount(accNo, name, balance);

        if(type.equalsIgnoreCase("CURRENT"))
            return new CurrentAccount(accNo, name, balance);

        throw new IllegalArgumentException("Invalid type");
    }
}
