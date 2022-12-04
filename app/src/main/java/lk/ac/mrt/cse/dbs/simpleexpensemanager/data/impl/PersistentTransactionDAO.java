package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;

import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.ExpenseDB;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistentTransactionDAO implements TransactionDAO {
    private ExpenseDB expenseDB;

    private final List<Transaction> transactions;

    public PersistentTransactionDAO(Context context) {
        expenseDB = new ExpenseDB(context);
        transactions = expenseDB.readTransaction();
    }



    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        Transaction transaction = new Transaction(date,accountNo,expenseType,amount);
        transactions.add(transaction);
        String string_year = String.valueOf(date.getYear());
        String string_month = String.valueOf(date.getMonth());
        String string_day = String.valueOf(date.getDate());

        String string_date = string_year+"-"+string_month+"-"+string_day;

        expenseDB.addNewTransaction(string_date,accountNo, String.valueOf(expenseType),String.valueOf(amount));
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        return transactions;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        int size = transactions.size();
        if (size <= limit ){
            return transactions;
        }
        return transactions.subList(size - limit,size);
    }
}
