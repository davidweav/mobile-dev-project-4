package edu.uga.cs.project4;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ResultsHistoryFragment extends Fragment implements RetrieveQuizResultsAsyncTask.OnResultsRetrievedListener {

    private RecyclerView recyclerView;
    private TextView emptyStateTextView;
    private ProgressBar loadingProgressBar;
    private Button newQuizButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_results_history, container, false);
        
        // Initialize views
        recyclerView = view.findViewById(R.id.resultsRecyclerView);
        emptyStateTextView = view.findViewById(R.id.emptyStateTextView);
        loadingProgressBar = view.findViewById(R.id.loadingProgressBar);
        newQuizButton = view.findViewById(R.id.newQuizButton);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new ResultsAdapter(new ArrayList<>()));

        // Set up Button click listener
        newQuizButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), QuizActivity.class);
            startActivity(intent);
            if (getActivity() != null) {
                getActivity().finish();
            }
        });

        // Load results
        loadResults();
        
        return view;
    }

    private void loadResults() {
        // Show loading state
        loadingProgressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        emptyStateTextView.setVisibility(View.GONE);

        // Execute the AsyncTask to retrieve results
        new RetrieveQuizResultsAsyncTask(getContext(), this).execute();
    }

    @Override
    public void onResultsRetrieved(List<Result> results) {
        // Hide loading indicator
        loadingProgressBar.setVisibility(View.GONE);

        if (results.isEmpty()) {
            // Show empty state message
            emptyStateTextView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            // Show results in RecyclerView
            recyclerView.setVisibility(View.VISIBLE);
            emptyStateTextView.setVisibility(View.GONE);
            recyclerView.setAdapter(new ResultsAdapter(results));
        }
    }
}