package com.example.papertrader.ui.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.papertrader.R;
import com.example.papertrader.ui.adapters.MarketStockListAdapter;
import com.example.papertrader.ui.login.AuthHandler;

public class AccountFragment extends Fragment {

    private Button logout_button;
    private AuthHandler authHandler;


    private AccountFragment(){

    }

    private static AccountFragment instance;

    public static AccountFragment getInstance(){
        if(instance == null)
            instance = new AccountFragment();

        return instance;
    }

    public void deleteInstance(){
        instance = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){




        return inflater.inflate(R.layout.fragment_account, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        authHandler = new AuthHandler(this.getContext(), this.getActivity());

        logout_button = (Button) view.findViewById(R.id.button_main_logout);
        logout_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                authHandler.logout();

            }

        });


    }

}
