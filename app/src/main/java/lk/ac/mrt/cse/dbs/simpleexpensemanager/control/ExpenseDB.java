package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class ExpenseDB extends SQLiteOpenHelper {

    // creating a constant variables for our database.
    // below variable is for our database name.
    private static final String DB_NAME = "expenseManager";

    // below int is our database version
    private static final int DB_VERSION = 1;

    // below variable is for our table name.
    private static final String TABLE_NAME = "Expenses";

    // below variable is for our id column.
    private static final String ID_COL = "id";

    private static final String DATE_COL = "Date";

    private static final String ACC_NO_COL = "Account No.";

    private static final String TYPE_COL = "Type";

    private static final String AMOUNT_COL = "Amount";


    // creating a constructor for our database handler.
    public ExpenseDB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // below method is for creating a database by running a sqlite query
    @Override
    public void onCreate(SQLiteDatabase db) {
        // on below line we are creating
        // an sqlite query and we are
        // setting our column names
        // along with their data types.
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DATE_COL + " TEXT,"
                + ACC_NO_COL + " TEXT,"
                + TYPE_COL + " TEXT,"
                + AMOUNT_COL + " TEXT)";

        // at last we are calling a exec sql
        // method to execute above sql query
        db.execSQL(query);
    }

    // this method is use to add new course to our sqlite database.
    public void addNewTransaction(String transactionDate, String transactionAccountNumber, String transactionType, String transactionAmount) {

        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(DATE_COL, transactionDate);
        values.put(ACC_NO_COL, transactionAccountNumber);
        values.put(TYPE_COL, transactionType);
        values.put(AMOUNT_COL, transactionAmount);

        // after adding all values we are passing
        // content values to our table.
        db.insert(TABLE_NAME, null, values);

        // at last we are closing our
        // database after adding database.
        db.close();
    }

    public ArrayList<Transaction> readTransaction() {
        // on below line we are creating a
        // database for reading our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are creating a cursor with query to read data from database.
        Cursor cursorTransactions = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        // on below line we are creating a new array list.
        ArrayList<Transaction> transactionModalArrayList = new ArrayList<>();

        // moving our cursor to first position.
        if (cursorTransactions.moveToFirst()) {
            do {
                Date date = new Date();
                String[] stringDate = cursorTransactions.getString(1).split("-");

                date.setDate(Integer.parseInt(stringDate[0]));
                date.setMonth(Integer.parseInt(stringDate[1]));
                date.setYear(Integer.parseInt(stringDate[2]));

                ExpenseType expenseType ;
                if (cursorTransactions.getString(3) == "EXPENSE"){
                    expenseType = ExpenseType.EXPENSE;
                }else{
                    expenseType = ExpenseType.INCOME;
                }

                // on below line we are adding the data from cursor to our array list.
                transactionModalArrayList.add(new Transaction(date,
                        cursorTransactions.getString(4),
                        expenseType,
                        (double)Integer.parseInt(cursorTransactions.getString(3))));
            } while (cursorTransactions.moveToNext());
            // moving our cursor to next.
        }
        // at last closing our cursor
        // and returning our array list.
        cursorTransactions.close();
        return transactionModalArrayList;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
