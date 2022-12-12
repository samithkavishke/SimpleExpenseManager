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

    private static final String TABLE_NAME = "transaction";
    private static final String TRANSACTION_ACC_COL = "account_no";
    private static final String TRANSACTION_ID_COL = "id";
    private static final String TRANSACTION_DATE_COL = "date";
    private static final String TRANSACTION_TYPE_COL = "Type";
    private static final String TRANSACTION_AMOUNT_COL = "Amount";

    public PersistentTransactionDAO(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper ;
//        expenseDB = new ExpenseDB(context);
//        transactions = expenseDB.readTransaction();
    }



    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase sqLiteDatabase = this.databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        String sDate = date.toString();
        String sExpense = expenseType.toString();

        values.put(TRANSACTION_DATE_COL, sDate);
        values.put(TRANSACTION_ACC_COL, accountNo);
        values.put(TRANSACTION_TYPE_COL, sExpense);
        values.put(TRANSACTION_AMOUNT_COL, amount);
//        Transaction transaction = new Transaction(date,accountNo,expenseType,amount);
//        transactions.add(transaction);
//        String string_year = String.valueOf(date.getYear());
//        String string_month = String.valueOf(date.getMonth());
//        String string_day = String.valueOf(date.getDate());
//
//        String string_date = string_year+"-"+string_month+"-"+string_day;
//
//        expenseDB.addNewTransaction(string_date,accountNo, String.valueOf(expenseType),String.valueOf(amount));
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

    private ExpenseType toExpenseType(String sExpense){
        return new ExpenseType();
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        SQLiteDatabase sqLiteDatabase = this.databaseHelper.getReadableDatabase();

        // on below line we are creating a cursor with query to read data from database.
        Cursor cursorTransactions = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        // on below line we are creating a new array list.
        ArrayList<Transaction> transactionModalArrayList = new ArrayList<>();

        // moving our cursor to first position.
        if (cursorTransactions.moveToFirst()) {
            do {

                Date transactionDate = toDate(cursorTransactions.getString(1));

                // on below line we are adding the data from cursor to our array list.
                transactionModalArrayList.add(new Transaction(transactionDate,
                        cursorTransactions.getString(4),
                        cursorTransactions.getString(2),
                        (double)Integer.parseInt(cursorTransactions.getString(3))));
            } while (cursorTransactions.moveToNext());
            // moving our cursor to next.
        }
        // at last closing our cursor
        // and returning our array list.
        cursorTransactions.close();

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
