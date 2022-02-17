package com.example.papertrader.ui.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.papertrader.R;
import com.example.papertrader.api.ApiConnection;
import com.example.papertrader.api.ResponseCallBack;
import com.example.papertrader.data.SharedViewModel;
import com.example.papertrader.ui.adapters.MarketStockListAdapter;
import com.example.papertrader.ui.main.StockActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import objects.MarketStockObject;




public class MarketFragment extends Fragment {

    private ArrayList<MarketStockObject> marketStockObjects;

    public ListView marketStocksListView;


    private MarketFragment(){

    }

    private static MarketFragment instance;

    public static MarketFragment getInstance(){
        if(instance == null)
            instance = new MarketFragment();

        return instance;
    }

    public void deleteInstance(){
        instance = null;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            System.out.println("SAVED INSTANCE STATE ISN'T NULL");
            marketStockObjects = (ArrayList<MarketStockObject>) savedInstanceState.get("Stock  Objects");
        }
        else{
            System.out.println("SAVED INSTANCE STATE IS NULL");
        }
//        System.out.println("MARKET VIEW HAS BEEN CREATED!!!");



    }


    private void initSearchWidgets(){
        TextView searchText = getView().findViewById(R.id.searchMarketStocks);

        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayList<MarketStockObject> filteredStocks = new ArrayList<MarketStockObject>();
                s = s.toString().toLowerCase();
                for(MarketStockObject stock:marketStockObjects){
                    // A string to perform searching on so all the stock fields are scanned
                    String searchString = stock.getCompanyName() + " " + stock.getCountry() + " " + stock.getCurrency() + " " + stock.getExchange() + " " + stock.getIndustry() + " " + stock.getSector() + " " + stock.getTicker();
                    searchString = searchString.toLowerCase();
                    if(searchString.contains(s)){
                        filteredStocks.add(stock);
                    }

                }

                MarketStockListAdapter adapter = new MarketStockListAdapter(getContext(), R.layout.market_list_item, filteredStocks);
                marketStocksListView.setAdapter(adapter);


                marketStocksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        System.out.println("CLICKED!!!");
                        String ticker_selected = marketStockObjects.get(position).getTicker();

                        Intent stock_activity = new Intent(getActivity(), StockActivity.class);
                        Bundle b = new Bundle();
                        b.putString("Ticker", ticker_selected);
                        stock_activity.putExtras(b);
                        startActivity(stock_activity);
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }


    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putParcelableArrayList("Stock Objects", this.marketStockObjects);
        super.onSaveInstanceState(outState);
    }




    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        view.findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);

        marketStocksListView = view.findViewById(R.id.marketListView);

        if(marketStockObjects == null){
            SharedViewModel model = new ViewModelProvider(this).get(SharedViewModel.class);
            model.getMarketStocks().observe(getViewLifecycleOwner(), marketStocks -> {
                String jsonString = marketStocks.toString();
                System.out.println(jsonString);



                marketStockObjects = new ArrayList<MarketStockObject>();

                for(JSONObject stockJson: marketStocks)
                    marketStockObjects.add(new MarketStockObject(stockJson));

                MarketStockListAdapter adapter = new MarketStockListAdapter(this.getContext(), R.layout.market_list_item, marketStockObjects);
                marketStocksListView.setAdapter(adapter);
            });
        }
        else {
            MarketStockListAdapter adapter = new MarketStockListAdapter(this.getContext(), R.layout.market_list_item, marketStockObjects);
            marketStocksListView.setAdapter(adapter);
        }

        initSearchWidgets();

        view.findViewById(R.id.loadingPanel).setVisibility(View.GONE);


    }

        @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){



        return inflater.inflate(R.layout.fragment_market, container, false);
    }

}
