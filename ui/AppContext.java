package ui;

import repository.BankRepository;
import repository.SqlBankRepository;
import service.AccountService;

public class AppContext {

    private static final BankRepository repository =
            new SqlBankRepository();

    private static final AccountService accountService =
            new AccountService(repository);

    public static AccountService getAccountService() {
        return accountService;
    }
}