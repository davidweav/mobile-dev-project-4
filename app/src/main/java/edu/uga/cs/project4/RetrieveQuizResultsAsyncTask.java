package edu.uga.cs.project4;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class RetrieveQuizResultsAsyncTask extends AsyncTask<Void, Void, List<Result>> {

    private Context context;
    private OnResultsRetrievedListener listener;

    public interface OnResultsRetrievedListener {
        void onResultsRetrieved(List<Result> results);
    }

    public RetrieveQuizResultsAsyncTask(Context context, OnResultsRetrievedListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected List<Result> doInBackground(Void... voids) {
        List<Result> results = new ArrayList<>();
        
        try {
            SQLiteDatabase db = DatabaseHelper.getInstance(context).getReadableDatabase();
            
            String[] columns = {
                DatabaseHelper.COLUMN_ID,
                DatabaseHelper.COLUMN_QUIZ_DATE,
                DatabaseHelper.COLUMN_QUIZ_RESULT
            };
            
            // Add debug logging
            Log.d("ResultsDebug", "Querying results table: " + DatabaseHelper.TABLE_RESULTS);
            
            // Query the results table, ordered by date descending (newest first)
            Cursor cursor = db.query(
                DatabaseHelper.TABLE_RESULTS,
                columns,
                null,
                null,
                null,
                null,
                DatabaseHelper.COLUMN_QUIZ_DATE + " DESC"  // Order by date descending
            );
            
            // Log how many results were found
            Log.d("ResultsDebug", "Number of results: " + cursor.getCount());
            
            // Iterate through the cursor and add each result to the list
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_QUIZ_DATE));
                float score = cursor.getFloat(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_QUIZ_RESULT));
                
                // Log each result for debugging
                Log.d("ResultsDebug", "Result found - ID: " + id + ", Date: " + date + ", Score: " + score);
                
                Result result = new Result(id, date, score);
                results.add(result);
            }
            
            cursor.close();
            db.close();
            
        } catch (Exception e) {
            Log.e("RetrieveResultsTask", "Error retrieving quiz results", e);
        }
        
        return results;
    }

    @Override
    protected void onPostExecute(List<Result> results) {
        if (listener != null) {
            listener.onResultsRetrieved(results);
        }
    }
}