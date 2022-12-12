package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.Database.DatabaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;

public class PersistentExpenseManager extends ExpenseManager {

    private TransactionDAO persistentTransactionDAO ;
    private AccountDAO persistentAccountDAO;
    private DatabaseHelper databaseHelper;
    public PersistentExpenseManager(Context context) {
        super();
        this.databaseHelper = new DatabaseHelper(context);
        setup();

    }

    @Override
    public void setup() {
        /*** Begin generating dummy data for In-Memory implementation ***/

        persistentTransactionDAO = new PersistentTransactionDAO(this.databaseHelper);
        persistentAccountDAO = new PersistentAccountDAO(this.databaseHelper);
        setTransactionsDAO(persistentTransactionDAO);
        setAccountsDAO(persistentAccountDAO);

        // dummy data
//        Account dummyAcct1 = new Account("12345A", "Yoda Bank", "Anakin Skywalker", 10000.0);
//        Account dummyAcct2 = new Account("78945Z", "Clone BC", "Obi-Wan Kenobi", 80000.0);
//        getAccountsDAO().addAccount(dummyAcct1);
//        getAccountsDAO().addAccount(dummyAcct2);

        /*** End ***/
    }
}
