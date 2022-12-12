package lk.ac.mrt.cse.dbs.simpleexpensemanager.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "200525R";
    private static final int DB_VERSION = 1;

    private static final String TABLE_ACCOUNT = "accounts";
    private static final String ACC_NO_COL = "account_no";
    private static final String BANK_COL = "bank";
    private static final String ACC_HOLDER_COL = "account_holder";
    private static final String INITIAL_BALANCE_COL = "initial_balance";

    private static final String TABLE_TRANSACTION = "10";
    private static final String TRANSACTION_ACC_COL = "account_no";
    private static final String TRANSACTION_ID_COL = "id";
    private static final String TRANSACTION_DATE_COL = "date";
    private static final String TRANSACTION_TYPE_COL = "Type";
    private static final String TRANSACTION_AMOUNT_COL = "Amount";


    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query1 = "CREATE TABLE " + TABLE_ACCOUNT + " ("
                      + ACC_NO_COL + " TEXT PRIMARY KEY, "
                      + BANK_COL + " TEXT, "
                      + ACC_HOLDER_COL + " TEXT,"
                      + INITIAL_BALANCE_COL + " REAL);";

        String query2 = "CREATE TABLE " + TABLE_TRANSACTION + " ("
                        + TRANSACTION_ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + TRANSACTION_DATE_COL + " TEXT,"
                        + TRANSACTION_ACC_COL + " TEXT,"
                        + TRANSACTION_TYPE_COL + " TEXT,"
                        + TRANSACTION_AMOUNT_COL + " REAL)";


        sqLiteDatabase.execSQL(query1);
        sqLiteDatabase.execSQL(query2);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTION);
        onCreate(db);
    }


}
