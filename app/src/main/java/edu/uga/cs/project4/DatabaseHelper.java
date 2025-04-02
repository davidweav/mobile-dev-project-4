package edu.uga.cs.project4;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database name and version
    private static final String DATABASE_NAME = "countries.db";
    private static final int DATABASE_VERSION = 1;

    // Table name and columns
    public static final String TABLE_COUNTRIES = "countries";

    public static final String TABLE_RESULTS = "results";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_COUNTRY_NAME = "country_name";
    public static final String COLUMN_CONTINENT = "continent";

    public static final String COLUMN_QUIZ_DATE = "quiz_date";
    public static final String COLUMN_QUIZ_RESULT = "quiz_result";

    // Create country table SQL query
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_COUNTRIES + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_COUNTRY_NAME + " TEXT NOT NULL, " +
                    COLUMN_CONTINENT + " TEXT NOT NULL);";

    // Create results table SQL query
    private static final String TABLE_CREATE2 =
            "CREATE TABLE " + TABLE_RESULTS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_QUIZ_DATE + " TEXT NOT NULL, " +
                    COLUMN_QUIZ_RESULT + " REAL NOT NULL);";

    // Constructor
    public DatabaseHelper(Context context) {
        super(context, context.getDatabasePath(DATABASE_NAME).getAbsolutePath(), null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
        db.execSQL(TABLE_CREATE2);
        Log.i("DB", "Created DB");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop and recreate the table if the database version is upgraded
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COUNTRIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESULTS);
        onCreate(db);
    }

    // Singleton pattern to ensure only one instance of the DatabaseHelper
    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }
}
