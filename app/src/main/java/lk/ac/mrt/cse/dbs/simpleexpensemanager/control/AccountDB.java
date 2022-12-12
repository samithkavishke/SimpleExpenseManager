package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;

public class AccountDB extends SQLiteOpenHelper {

    // creating a constant variables for our database.
    // below variable is for our database name.
    private static final String DB_NAME = "expenseManager";

    // below int is our database version
    private static final int DB_VERSION = 1;


    // below variable is for our table name.
    private static final String TABLE_NAME = "Accounts";

    private static final String ACC_NO_COL = "Account No.";

    private static final String BANK_COL = "Bank";

    private static final String ACC_HOLDER_COL = "Account Holder";

    private static final String INITIAL_BALANCE_COL = "Initial Balance";


    // creating a constructor for our database handler.
    public AccountDB(Context context) {
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
//                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ACC_NO_COL + " TEXT PRIMARY KEY ,"
                + BANK_COL + " TEXT,"
                + ACC_HOLDER_COL + " TEXT,"
                + INITIAL_BALANCE_COL + " TEXT)";

        // at last we are calling a exec sql
        // method to execute above sql query
        db.execSQL(query);
    }

    public void removeUser(String userAccountNo){
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "DELETE FROM  WHERE ACC_NO_COL = "+userAccountNo;
        db.execSQL(query);
    }
    // this method is use to add new course to our sqlite database.
    public void addNewUser(String userAccountNo, String userBank, String userAccountHolder, String userInitialBalance) {

        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(BANK_COL, userBank);
        values.put(ACC_NO_COL, userAccountNo);
        values.put(ACC_HOLDER_COL, userAccountHolder);
        values.put(INITIAL_BALANCE_COL, userInitialBalance);

        // after adding all values we are passing
        // content values to our table.
        db.insert(TABLE_NAME, null, values);

        // at last we are closing our
        // database after adding database.
        db.close();
    }

    public ArrayList<Account> readAccounts(){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursorAccounts = db.rawQuery("SELECT * FROM "+TABLE_NAME,null);

        ArrayList<Account> accountModalArrayList = new ArrayList<>();

        if(cursorAccounts.moveToFirst()) {
            do {
                accountModalArrayList.add(new Account(cursorAccounts.getString(1),
                        cursorAccounts.getString(2),
                        cursorAccounts.getString(3),
                        Double.parseDouble(cursorAccounts.getString(4))));
            } while (cursorAccounts.moveToNext());
        }
        cursorAccounts.close();
        return accountModalArrayList;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
