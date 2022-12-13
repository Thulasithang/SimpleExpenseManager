package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentAccountDAO1;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DbHelp;

public class PersistentExpenseManager extends ExpenseManager{
    private Context context;

    public PersistentExpenseManager(Context context){
        this.context = context;
        setup();
    }

    @Override
    public void setup() {
        DbHelp helper = new DbHelp(this.context);
        TransactionDAO persistentTransactionDAO = new PersistentTransactionDAO(helper);
        setTransactionsDAO(persistentTransactionDAO);
        AccountDAO persistentAccountDAO1 = new PersistentAccountDAO1(helper);
        setAccountsDAO(persistentAccountDAO1);
    }
}
