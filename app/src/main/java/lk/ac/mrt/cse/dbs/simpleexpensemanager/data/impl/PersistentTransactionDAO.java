package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.Database.DatabaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistentTransactionDAO implements TransactionDAO {
//    private ExpenseDB expenseDB;
    private DatabaseHelper databaseHelper;

    private static final String TABLE_NAME = "transaction_table";
    private static final String TRANSACTION_ACC_COL = "account_no";
    private static final String TRANSACTION_ID_COL = "id";
    private static final String TRANSACTION_DATE_COL = "date";
    private static final String TRANSACTION_TYPE_COL = "Type";
    private static final String TRANSACTION_AMOUNT_COL = "Amount";

    public PersistentTransactionDAO(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper ;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase sqLiteDatabase = this.databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        String sDate = date.toString();
        String sExpense = expenseToString(expenseType);
//        String sExpense = expenseType.toString();

        values.put(TRANSACTION_DATE_COL, sDate);
        values.put(TRANSACTION_ACC_COL, accountNo);
        values.put(TRANSACTION_TYPE_COL, sExpense);
        values.put(TRANSACTION_AMOUNT_COL, amount);

        sqLiteDatabase.insert(TABLE_NAME,null,values);
    }

    private Date toDate(String sDate){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        Date date= null;
        try {
            date = simpleDateFormat.parse(sDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    private String expenseToString(ExpenseType expenseType){
        if (expenseType == ExpenseType.EXPENSE){
            return "EXPENSE";
        }
        else{
            return "INCOME";
        }
    }

    private ExpenseType toExpenseType(String sExpense){
        if(sExpense.equals("EXPENSE")){
            return ExpenseType.EXPENSE;
        }else{
            return ExpenseType.INCOME;
        }
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        SQLiteDatabase sqLiteDatabase = this.databaseHelper.getReadableDatabase();

        Cursor cursorTransactions = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        ArrayList<Transaction> transactions = new ArrayList<>();

        if (cursorTransactions.moveToFirst()) {
            do {

                Date transactionDate = toDate(cursorTransactions.getString(1));
                String transactionAccountNo = cursorTransactions.getString(2);
                ExpenseType transactionExpenseType = toExpenseType(cursorTransactions.getString(4));
                double transactionBalance = cursorTransactions.getDouble(4);

                // on below line we are adding the data from cursor to our array list.
                transactions.add(new Transaction(transactionDate,
                        transactionAccountNo,
                        transactionExpenseType,
                        transactionBalance
                        ));
            } while (cursorTransactions.moveToNext());
        }
        cursorTransactions.close();

        return transactions;
    }


    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> transactions = getAllTransactionLogs();
        int size = transactions.size();
        if (size <= limit ){
            return transactions;
        }
        return transactions.subList(size - limit,size);
    }
}
