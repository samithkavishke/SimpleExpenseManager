package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.Database.DatabaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentAccountDAO implements AccountDAO {
    private DatabaseHelper dbHelper;
    private static final String TABLE_NAME = "accounts";
    private static final String ACC_NO_COL = "account_no";
    private static final String BANK_COL = "bank";
    private static final String ACC_HOLDER_COL = "account_holder";
    private static final String INITIAL_BALANCE_COL = "initial_balance";


    public PersistentAccountDAO(DatabaseHelper dbHelper){
        this.dbHelper = dbHelper;
    }

    @Override
    public List<String> getAccountNumbersList() {
        List<Account> accountList = getAccountsList();
        List<String> stringList = new ArrayList<>();

        for(Account account: accountList){
            stringList.add(account.getAccountNo());
        }
        return stringList;
    }

    @Override
    public List<Account> getAccountsList() {
        SQLiteDatabase db = this.dbHelper.getReadableDatabase();

        Cursor cursorAccounts = db.rawQuery("SELECT * FROM "+TABLE_NAME,null);

        ArrayList<Account> accountModalArrayList = new ArrayList<>();

        if(cursorAccounts.moveToFirst()) {
            do {
                accountModalArrayList.add(
                        new Account(cursorAccounts.getString(0),
                                cursorAccounts.getString(1),
                                cursorAccounts.getString(2),
                                cursorAccounts.getDouble(3)));
            } while (cursorAccounts.moveToNext());
        }
        cursorAccounts.close();
        return accountModalArrayList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        List<Account> accountArrayList = getAccountsList();
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
        SQLiteDatabase db = this.dbHelper.getWritableDatabase();

        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(BANK_COL, account.getBankName());
        values.put(ACC_NO_COL, account.getAccountNo());
        values.put(ACC_HOLDER_COL, account.getAccountHolderName());
        values.put(INITIAL_BALANCE_COL, account.getBalance());

        // after adding all values we are passing
        // content values to our table.
        db.insert(TABLE_NAME, null, values);

        // at last we are closing our
        // database after adding database.
        db.close();
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase sqLiteDatabase = this.dbHelper.getWritableDatabase();
        try{

            sqLiteDatabase.delete(TABLE_NAME, "name = ?", new String[]{accountNo});
            sqLiteDatabase.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        String msg = "Account " + accountNo + " is invalid.";
        throw new InvalidAccountException(msg);
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        SQLiteDatabase sqLiteDatabase = this.dbHelper.getWritableDatabase();
        Account account = getAccount(accountNo);
        ContentValues values = new ContentValues();
        double new_balance;
        if(expenseType == ExpenseType.EXPENSE){

            if (account.getBalance()<amount){
                String msg = "Account " + accountNo + " is invalid.";
                throw new InvalidAccountException(msg);
            }else{
                new_balance = account.getBalance()-amount;
                values.put(INITIAL_BALANCE_COL,new_balance);
                sqLiteDatabase.update(TABLE_NAME,values,ACC_NO_COL+" = ? ",new String[]{accountNo});
                sqLiteDatabase.close();
            }
        }else{
            new_balance = account.getBalance()+amount;
            values.put(INITIAL_BALANCE_COL,new_balance);
            sqLiteDatabase.update(TABLE_NAME,values,ACC_NO_COL+" = ? ",new String[]{accountNo});
            sqLiteDatabase.close();
        }

    }
}
