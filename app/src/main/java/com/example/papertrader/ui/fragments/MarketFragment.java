package com.example.papertrader.ui.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.papertrader.R;
import com.example.papertrader.data.SharedViewModel;
import com.example.papertrader.ui.adapters.MarketStockListAdapter;

import org.json.JSONObject;

import java.util.ArrayList;

import objects.MarketStockObject;


public class MarketFragment extends Fragment {
    public MarketFragment(){


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        SharedViewModel model = new ViewModelProvider(this).get(SharedViewModel.class);
        model.getMarketStocks().observe(getViewLifecycleOwner(), marketStocks -> {
            String jsonString = marketStocks.toString();
            System.out.println(jsonString);

            ListView marketStocksListView = (ListView) view.findViewById(R.id.marketListView);

            view.findViewById(R.id.loadingPanel).setVisibility(View.GONE);

            ArrayList<MarketStockObject> marketStockObjects = new ArrayList<MarketStockObject>();

            for(JSONObject stockJson: marketStocks){
                marketStockObjects.add(new MarketStockObject(stockJson));

            }

            MarketStockListAdapter adapter = new MarketStockListAdapter(this.getContext(), R.layout.market_list_item, marketStockObjects);

        });



    }

        @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        return inflater.inflate(R.layout.fragment_market, container, false);
    }

}
