package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.AccountDB;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentAccountDAO implements AccountDAO {
    private AccountDB accountDB;
    private ArrayList<Account> accountArrayList;

    public PersistentAccountDAO(Context context){
        accountDB = new AccountDB(context);
    }

    @Override
    public List<String> getAccountNumbersList() {
        ArrayList<String> accountNumberList =new ArrayList<>();
        accountArrayList = accountDB.readAccounts();
        for(Account account:accountArrayList){
            accountNumberList.add(account.getAccountNo());
        }
        return accountNumberList;
    }

    @Override
    public List<Account> getAccountsList() {
        return accountDB.readAccounts();
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        accountArrayList = accountDB.readAccounts();
        for(Account account:accountArrayList){
            if(account.getAccountNo().equals(accountNo)){
                return account;
            }
        }
        String msg = "Account " + accountNo + " is invalid.";
        throw new InvalidAccountException(msg);
    }

    @Override
    public void addAccount(Account account) {
        accountDB.addNewUser(account.getAccountNo(),account.getBankName(),account.getAccountHolderName(),String.valueOf(account.getBalance()));
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        accountArrayList = accountDB.readAccounts();
        for(Account account:accountArrayList){
            if(account.getAccountNo().equals(accountNo)){
                accountDB.removeUser(accountNo);
            }
        }
        String msg = "Account " + accountNo + " is invalid.";
        throw new InvalidAccountException(msg);
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        accountArrayList = accountDB.readAccounts();
        for(Account account:accountArrayList){
            if(account.getAccountNo().equals(accountNo)){
                switch (expenseType) {
                    case EXPENSE:
                        account.setBalance(account.getBalance() - amount);
                        break;
                    case INCOME:
                        account.setBalance(account.getBalance() + amount);
                        break;
                }
            }
        }
        String msg = "Account " + accountNo + " is invalid.";
        throw new InvalidAccountException(msg);
    }
}
