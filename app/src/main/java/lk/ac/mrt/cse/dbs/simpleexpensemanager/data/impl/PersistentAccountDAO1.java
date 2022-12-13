package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DbHelp;
/**
 * This is an In-Memory implementation of the AccountDAO interface. This is not a persistent storage. A HashMap is
 * used to store the account details temporarily in the memory.
 */
public class PersistentAccountDAO1 implements AccountDAO {
    private DbHelp helper;
//    private final Map<String, Account> accounts;

    public PersistentAccountDAO1(DbHelp helper) {
//        this.accounts = new HashMap<>();
        this.helper=helper;
    }

    @Override
    public List<String> getAccountNumbersList() {
        List<String> accNumbersList = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT AccountNo FROM accounts ", null);
        if (c.moveToFirst()){
            do{
                accNumbersList.add(c.getString(0));
            } while (c.moveToNext());
        }
        c.close();
//            single column result to list : https://stackoverflow.com/questions/58115340/fast-way-to-get-sqlite-single-column-result-to-list
        return accNumbersList;
    }
    @Override
    public List<Account> getAccountsList() {
        List<Account> accList = new ArrayList<>();
        SQLiteDatabase db = this.helper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM accounts", null);
        if (c.moveToFirst()){
            do{
                Account acc = new Account(
                        c.getString(0),
                        c.getString(1),
                        c.getString(2),
                        Double.parseDouble(c.getString(3))
                );
                accList.add(acc);
            } while (c.moveToNext());
        }
        c.close();
        return accList;

    }
    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = this.helper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM accounts WHERE AccountNo = ? ", new String [] {accountNo});

        if (c != null){
            c.moveToFirst();
            Account account = new Account(
                    c.getString(0),
                    c.getString(1),
                    c.getString(2),
                    Double.parseDouble(c.getString(3))
            );
            c.close();
            return account;

        }
        String msg = "Account " + accountNo + " is invalid.";
        throw new InvalidAccountException(msg);
    }

    @Override
    public void addAccount(Account account) {
        SQLiteDatabase db = this.helper.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put(helper.ACC_NO, account.getAccountNo());
        content.put(helper.BANK_NAME, account.getBankName());
        content.put(helper.ACC_HOLDER_NAME, account.getAccountHolderName());
        content.put(helper.BALANCE, account.getBalance());

        db.insert(helper.TABLE_NAME, null, content);
        db.close();
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = this.helper.getWritableDatabase();
        db.delete(helper.TABLE_NAME, helper.ACC_NO + "=?", new String[]{accountNo});
        db.close();
//        deletion of a row : https://www.geeksforgeeks.org/how-to-delete-data-in-sqlite-database-in-android/
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        Account acc = this.getAccount(accountNo);
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues content = new ContentValues();
        switch (expenseType) {
            case EXPENSE:
                content.put(helper.BALANCE, acc.getBalance() - amount);
                break;
            case INCOME:
                content.put(helper.BALANCE, acc.getBalance() + amount);
                break;
        }
        // updating row
        db.update(helper.TABLE_NAME, content, helper.ACC_NO + " = ?", new String[] { accountNo });
    }
}


