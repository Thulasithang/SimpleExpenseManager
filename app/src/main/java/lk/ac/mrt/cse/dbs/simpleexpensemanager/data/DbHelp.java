package lk.ac.mrt.cse.dbs.simpleexpensemanager.data;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class DbHelp extends SQLiteOpenHelper{
    private static final String DB_NAME = "200186C";
    private static final int VERSION = 1;
    public static final String TABLE_NAME = "accounts";
    public static final String TRANSACTIONS = "transactions";

    public static final String ACC_NO = "AccountNo";
    public static final String ACC_HOLDER_NAME = "AccountHolderName";
    public static final String BANK_NAME = "BankName";
    public static final String BALANCE = "Balance";
    public static final String TRANS_ID = "ID";
    public static final String EXP_AMT = "Amount";
    public static final String EXP_TYPE = "ExpenseType";
    public static final String DATE = "Date";


    public DbHelp(@Nullable Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + "("
                + ACC_NO + " TEXT PRIMARY KEY," + BANK_NAME + " TEXT,"
                + ACC_HOLDER_NAME + " TEXT," + BALANCE + " REAL" + ")";
        db.execSQL(createTable);

        String createTransaction = "CREATE TABLE " + TRANSACTIONS + "("
                + TRANS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + DATE + " DATETIME," + ACC_NO + " TEXT,"
                + EXP_TYPE + " TEXT," + EXP_AMT + " REAL," + "FOREIGN KEY(" + ACC_NO +
                ") REFERENCES "+ TABLE_NAME +"(" + ACC_NO + ") )";
        db.execSQL(createTransaction);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS '"+ TABLE_NAME + "'");
        db.execSQL("DROP TABLE IF EXISTS '"+ TRANSACTIONS + "'");
        onCreate(db);
    }
}

// idea taken from https://stackoverflow.com/questions/53582128/database-helper-class

