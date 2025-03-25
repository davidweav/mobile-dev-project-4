package edu.uga.cs.project4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.opencsv.CSVReader;

import java.io.InputStream;
import java.io.InputStreamReader;

public class DatabasePopulation {

    private SQLiteDatabase db;

    // Constructor to initialize DatabaseHelper
    public DatabasePopulation(Context context) {
        db = DatabaseHelper.getInstance(context).getWritableDatabase();
    }

    // Method to populate the database with data from CSV
    public void populateFromCSV(Context context) {

        // Check if the countries table already has data
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_COUNTRIES, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();

        // If the table is empty, proceed with the CSV import
        if (count == 0) {

            try {
                InputStream in_s = context.getAssets().open("country_continent.csv");
                CSVReader reader = new CSVReader(new InputStreamReader(in_s));
                String[] nextRow;

                // Read each row of the CSV and insert it into the database
                while ((nextRow = reader.readNext()) != null) {
                    String countryName = nextRow[0]; // Assuming first column is country name
                    String continent = nextRow[1];   // Assuming second column is continent

                    // Insert into the database
                    ContentValues values = new ContentValues();
                    values.put(DatabaseHelper.COLUMN_COUNTRY_NAME, countryName);
                    values.put(DatabaseHelper.COLUMN_CONTINENT, continent);
                    db.insert(DatabaseHelper.TABLE_COUNTRIES, null, values);

                    Log.d("CSV", "Inserting country: " + countryName + ", continent: " + continent);
                }
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.d("CSV", "Countries table already populated. Skipping CSV import.");
        }

    }

    // Close the database when done
    public void close() {
        db.close();
    }
}
