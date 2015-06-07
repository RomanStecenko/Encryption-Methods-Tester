package ua.str.diploma.encryptionmethodstester;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "results.db";

    private static final String TABLE_NAME = "encryption_results";

    private static final String COLUMN_METHOD_KEY = "method_key";
    private static final String COLUMN_ENCRYPTION_TIME = "encryption_time";
    private static final String COLUMN_DECRYPTION_TIME = "decryption_time";
    private static final String COLUMN_FILE_SIZE = "file_size";
    private static final String COLUMN_TIMESTAMP_CREATION = "timestamp_creation";
    private static final String COLUMN_CPU_RATE = "cpu_rate";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_METHOD_KEY + " TEXT NOT NULL, " +
                COLUMN_ENCRYPTION_TIME + " INTEGER NOT NULL, " +
                COLUMN_DECRYPTION_TIME + " INTEGER, " +
                COLUMN_FILE_SIZE + " INTEGER, " +
                COLUMN_CPU_RATE + " INTEGER, " +
                COLUMN_TIMESTAMP_CREATION + " INTEGER NOT NULL);";

        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public int addEncryptionResult(Result result) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_METHOD_KEY, result.getMethod());
        values.put(COLUMN_ENCRYPTION_TIME, result.getEncryptionTime());
        values.put(COLUMN_DECRYPTION_TIME, result.getDecryptionTime());
        values.put(COLUMN_FILE_SIZE, result.getEncryptedFileSize());
        values.put(COLUMN_CPU_RATE, result.getCpuRate());
        values.put(COLUMN_TIMESTAMP_CREATION, result.getTimestamp());

        int newNoteId = (int) db.insert(TABLE_NAME, null, values);
        db.close();
        return newNoteId;
    }

    public Result getLastResult(){
        Result result = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_TIMESTAMP_CREATION + " DESC LIMIT 1";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            result = new Result();
            result.setId(cursor.getInt(0));
            result.setMethod(cursor.getString(1));
            result.setEncryptionTime(cursor.getLong(2));
            result.setDecryptionTime(cursor.getLong(3));
            result.setEncryptedFileSize(cursor.getLong(4));
            result.setCpuRate(cursor.getLong(5));
            result.setTimestamp(cursor.getLong(6));
        }
        db.close();
        return result;
    }

    public Result[] getLastResults(){
        Result[] results = new Result[6];
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_TIMESTAMP_CREATION + " DESC LIMIT 6";
        Cursor cursor = db.rawQuery(selectQuery, null);
        int i = 0;
        while (cursor.moveToNext()) {
            Result result = new Result();
            result.setId(cursor.getInt(0));
            result.setMethod(cursor.getString(1));
            result.setEncryptionTime(cursor.getLong(2));
            result.setDecryptionTime(cursor.getLong(3));
            result.setEncryptedFileSize(cursor.getLong(4));
            result.setCpuRate(cursor.getLong(5));
            result.setTimestamp(cursor.getLong(6));
            results[i] = result;
            i++;
        }
        db.close();
        return results;
    }
}
