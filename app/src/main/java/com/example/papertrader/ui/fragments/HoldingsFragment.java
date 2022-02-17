package com.example.papertrader.ui.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.papertrader.R;
import com.example.papertrader.data.SharedViewModel;
import com.example.papertrader.ui.adapters.HoldingsStockListAdapter;
import com.example.papertrader.ui.adapters.MarketStockListAdapter;
import com.example.papertrader.ui.login.AuthHandler;
import com.example.papertrader.ui.main.MainActivity;
import com.example.papertrader.ui.main.StockActivity;
import com.google.android.material.transition.Hold;

import org.json.JSONObject;

import java.util.ArrayList;

import objects.HoldingsStockObject;
import objects.MarketStockObject;

public class HoldingsFragment extends Fragment {
    private ArrayList<HoldingsStockObject> holdingStockObjects;
    private AuthHandler authHandler;

    public ListView holdingsStocksListView;

    public HoldingsFragment(){

    }




    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        view.findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
        authHandler = new AuthHandler(this.getContext(), this.getActivity());

        holdingsStocksListView = view.findViewById(R.id.holdingsListView);

            SharedViewModel model = new ViewModelProvider(this).get(SharedViewModel.class);
            model.getHoldingsStocks(authHandler.getAuthToken()).observe(getViewLifecycleOwner(), holdingStocks -> {
                String jsonString = holdingStocks.toString();
                System.out.println(jsonString);


                holdingStockObjects = new ArrayList<HoldingsStockObject>();

                for(JSONObject stockJson: holdingStocks)
                    holdingStockObjects.add(new HoldingsStockObject(stockJson));

                HoldingsStockListAdapter adapter = new HoldingsStockListAdapter(this.getContext(), R.layout.holdings_list_item, holdingStockObjects);
                holdingsStocksListView.setAdapter(adapter);
                view.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
            });


        holdingsStocksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String ticker_selected = holdingStockObjects.get(position).getTicker();

                Intent stock_activity = new Intent(getActivity(), StockActivity.class);
                Bundle b = new Bundle();
                b.putString("Ticker", ticker_selected);
                stock_activity.putExtras(b);
                startActivity(stock_activity);
            }
        });



    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        return inflater.inflate(R.layout.fragment_holdings, container, false);
    }

}
