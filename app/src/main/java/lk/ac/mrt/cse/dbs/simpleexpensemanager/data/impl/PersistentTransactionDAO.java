package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DbHelp;



/**
 * This is an In-Memory implementation of TransactionDAO interface. This is not a persistent storage. All the
 * transaction logs are stored in a LinkedList in memory.
 */
public class PersistentTransactionDAO implements TransactionDAO {
    private DbHelp helper;
//    private final List<Transaction> transactions;

    public PersistentTransactionDAO(DbHelp helper) {
        this.helper = helper;
    }


    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put(helper.ACC_NO, accountNo);
        content.put(helper.EXP_TYPE, expenseType.name()); // Bank Name
        content.put(helper.EXP_AMT, amount); // Holder Name
        content.put(helper.DATE, new SimpleDateFormat("yyyy-MM-dd").format(date));

        db.insert(helper.TRANSACTIONS, null, content);
        db.close();
        Log.d("Came Here", content.toString());
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> transactionList = new ArrayList<>();
        SQLiteDatabase db = this.helper.getReadableDatabase();
        Cursor c = db.query(helper.TRANSACTIONS,
                null,
                null,
                null,
                null,
                null,
                null
        );

        if (c.moveToFirst()){
            do{Date date;
                try{
                    date = new SimpleDateFormat("yyyy-MM-dd").parse(c.getString(1));
                    Transaction transaction = new Transaction(
                            date,
                            c.getString(2),
                            ExpenseType.valueOf(c.getString(3)),
                            Double.parseDouble(c.getString(4))
                    );
                    transactionList.add(transaction);
                }catch(ParseException e) {
                    e.printStackTrace();
                }
            } while (c.moveToNext());
        }
        c.close();
        return transactionList;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> transactionList = new ArrayList<>();

        SQLiteDatabase db = this.helper.getReadableDatabase();
        Cursor c = db.query(helper.TRANSACTIONS,
                null,
                null,
                null,
                null,
                null,
                null,
                limit + "");

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Date date;

                try {
                    date = new SimpleDateFormat("yyyy-MM-dd").parse(c.getString(1));
                    Transaction transaction = new Transaction(
                            date,
                            c.getString(2),
                            ExpenseType.valueOf(c.getString(3)),
                            Double.parseDouble(c.getString(4))
                    );
                    // Adding account to list
                    transactionList.add(transaction);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } while (c.moveToNext());
        }

        c.close();
        // return list
        return transactionList;
    }
}


