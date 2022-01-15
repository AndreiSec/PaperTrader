package com.example.papertrader.data;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.papertrader.R;
import com.example.papertrader.api.ApiConnection;
import com.example.papertrader.api.ResponseCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SharedViewModel extends ViewModel {

    private MutableLiveData<List<JSONObject>> market_stocks = null;
    private ApiConnection apiConnection = ApiConnection.getInstance();

    public LiveData<List<JSONObject>> getMarketStocks() {
        if (market_stocks == null) {
            market_stocks = new MutableLiveData<List<JSONObject>>();
            loadMarketStocks();
        }
        return market_stocks;
    }

    private void loadMarketStocks() {
        // Get all stock info from database

        apiConnection.get_all_stock_info(new ResponseCallBack() {

            @Override
            public void getJsonResponse(JSONObject json) {
//                String jsonString = json.toString();
//                System.out.println(jsonString);

                List<JSONObject> temp_stock_list = new ArrayList<JSONObject>();

                JSONArray jArray = null;
                try {
                    jArray = (JSONArray)json.get("stocks");

                    if (jArray != null) {
                        for (int i=0;i<jArray.length();i++){
                            temp_stock_list.add((JSONObject) jArray.get(i));
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }



                market_stocks.postValue(temp_stock_list);

            }
        });
    }



}

