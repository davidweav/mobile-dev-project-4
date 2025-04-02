package edu.uga.cs.project4;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class QuestionAdapter extends FragmentStateAdapter {

    private final Question[] questions;

    public QuestionAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, Question[] questions) {
        super(fragmentManager, lifecycle);
        this.questions = questions;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Return a new instance of QuestionFragment for each question

        if (position < questions.length) {
            return QuestionFragment.newInstance(questions[position], position + 1);
        }
        else {
            return new Fragment();
        }
    }

    @Override
    public int getItemCount() {
        return questions.length + 1;
    }
}
