package com.example.papertrader.ui.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.papertrader.R;
import com.example.papertrader.data.SharedViewModel;
import com.example.papertrader.ui.adapters.HoldingsStockListAdapter;
import com.example.papertrader.ui.adapters.PastTransactionsListAdapter;
import com.example.papertrader.ui.login.AuthHandler;

import org.json.JSONObject;

import java.util.ArrayList;

import objects.HoldingsStockObject;
import objects.PastTransactionObject;

public class AccountFragment extends Fragment {


    private ArrayList<PastTransactionObject> pastTransactionObjects;
    public ListView pastTransactionsListView;
    private SwipeRefreshLayout refreshLayout;
    private Button logout_button;
    SharedViewModel model;
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

        pastTransactionsListView = view.findViewById(R.id.pastTransactionsListView);

        model = new ViewModelProvider(this).get(SharedViewModel.class);
        model.get_past_transactions(authHandler.getAuthToken()).observe(getViewLifecycleOwner(), transactionStocks -> {
            String jsonString = transactionStocks.toString();
            System.out.println("TRANSACTIONS: " + jsonString);


            pastTransactionObjects = new ArrayList<PastTransactionObject>();

            for(JSONObject transactionJSON: transactionStocks)
                pastTransactionObjects.add(new PastTransactionObject(transactionJSON));

            PastTransactionsListAdapter adapter = new PastTransactionsListAdapter(this.getContext(), R.layout.past_transactions_list_item, pastTransactionObjects);
            pastTransactionsListView.setAdapter(adapter);
        });




        logout_button = view.findViewById(R.id.button_main_logout);
        logout_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                authHandler.logout();

            }

        });


        refreshLayout = view.findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                model.get_past_transactions(authHandler.getAuthToken()).observe(getViewLifecycleOwner(), transactionStocks -> {
                    String jsonString = transactionStocks.toString();
                    System.out.println("TRANSACTIONS: " + jsonString);


                    pastTransactionObjects = new ArrayList<PastTransactionObject>();

                    for(JSONObject transactionJSON: transactionStocks)
                        pastTransactionObjects.add(new PastTransactionObject(transactionJSON));

                    PastTransactionsListAdapter adapter = new PastTransactionsListAdapter(getContext(), R.layout.past_transactions_list_item, pastTransactionObjects);
                    pastTransactionsListView.setAdapter(adapter);
                    refreshLayout.setRefreshing(false);
                });
            }
        });

    }

}
