package com.example.disnap.ui.history;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.disnap.R;
import com.example.disnap.ui.base.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends BaseFragment {

    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        findViews(view);
        initViews(view);
        initListener(view);
        return view;
    }

    @Override
    public void findViews(View view) {

    }

    @Override
    public void initViews(View view) {

    }

    @Override
    public void initListener(View view) {

    }
}