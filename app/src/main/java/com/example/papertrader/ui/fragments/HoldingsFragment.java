package com.example.papertrader.ui.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.papertrader.R;

public class HoldingsFragment extends Fragment {


    private HoldingsFragment(){

    }

    private static HoldingsFragment instance;

    public static HoldingsFragment getInstance(){
        if(instance == null)
            instance = new HoldingsFragment();

        return instance;
    }

    public void deleteInstance(){
        instance = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_holdings, container, false);
    }

}
