package com.example.papertrader.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.papertrader.api.ApiConnection;
import com.example.papertrader.api.ResponseCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SharedViewModel extends ViewModel {

    private MutableLiveData<List<JSONObject>> market_stocks = null;
    private MutableLiveData<List<JSONObject>> holdings_stocks = null;
    private MutableLiveData<JSONObject> stock_info = null;
    private MutableLiveData<List<JSONObject>> past_transactions = null;
    private MutableLiveData<Float> user_balance = null;
    private final ApiConnection apiConnection = ApiConnection.getInstance();

    public LiveData<Float> get_user_balance(String authToken) {
        if (user_balance == null) {
            user_balance = new MutableLiveData<Float>();

        }
        loadUserBalance(authToken);
        return user_balance;
    }

    private void loadUserBalance(String authToken) {
        // Get past transactions associated with the auth token
        apiConnection.get_user_balance(new ResponseCallBack() {

            @Override
            public void getJsonResponse(JSONObject json) {
                String jsonString = json.toString();
                float balance = 0;
                try {
                    balance = (float) (double) json.get("user_balance");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                user_balance.postValue(balance);


            }
        }, authToken);
    }


    public LiveData<List<JSONObject>> get_past_transactions(String authToken) {

        if (past_transactions == null) {
            past_transactions = new MutableLiveData<List<JSONObject>>();

        }
        loadPastTransactions(authToken);
        return past_transactions;
    }

    private void loadPastTransactions(String authToken) {
        // Get past transactions associated with the auth token
        apiConnection.get_past_transactions(new ResponseCallBack() {

            @Override
            public void getJsonResponse(JSONObject json) {
                String jsonString = json.toString();

                List<JSONObject> temp_transactions_list = new ArrayList<JSONObject>();

                JSONArray jArray = null;
                try {
                    jArray = (JSONArray)json.get("past_transactions");

                    if (jArray != null) {
                        for (int i=0;i<jArray.length();i++){
                            temp_transactions_list.add((JSONObject) jArray.get(i));
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }



                past_transactions.postValue(temp_transactions_list);


            }
        }, authToken);
    }

    public MutableLiveData<JSONObject> getStockInfo(String authToken, String ticker) {

        if (stock_info == null) {
            stock_info = new MutableLiveData<JSONObject>();

        }
        loadStockInfo(authToken, ticker);
        return stock_info;
    }

    private void loadStockInfo(String authToken, String ticker) {
        // Get stock info and the owned stocks (if any) of the ticker and authToken
        System.out.println("AUTH TOKEN: " + authToken);
        System.out.println("TICKER: " + ticker);
        apiConnection.get_stock_stats(new ResponseCallBack() {

            @Override
            public void getJsonResponse(JSONObject json) {
                String jsonString = json.toString();
                System.out.println("RESPONSE:");
                System.out.println(jsonString);


                stock_info.postValue(json);

            }
        }, authToken, ticker);
    }


    public LiveData<List<JSONObject>> getHoldingsStocks(String authToken) {
        if (holdings_stocks == null) {
            holdings_stocks = new MutableLiveData<List<JSONObject>>();

        }
        loadHoldingStocks(authToken);
        return holdings_stocks;
    }

    private void loadHoldingStocks(String authToken) {
        // Get all held user stocks from backend

        apiConnection.get_owned_stocks(new ResponseCallBack() {

            @Override
            public void getJsonResponse(JSONObject json) {
                String jsonString = json.toString();
                System.out.println("RESPONSE:");
                System.out.println(jsonString);

                List<JSONObject> temp_stock_list = new ArrayList<JSONObject>();

                JSONArray jArray = null;
                try {
                    jArray = (JSONArray)json.get("stocks_owned");

                    if (jArray != null) {
                        for (int i=0;i<jArray.length();i++){
                            temp_stock_list.add((JSONObject) jArray.get(i));
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }



                holdings_stocks.postValue(temp_stock_list);

            }
        }, authToken);
    }



    public LiveData<List<JSONObject>> getMarketStocks() {
        if (market_stocks == null) {
            market_stocks = new MutableLiveData<List<JSONObject>>();

        }
        loadMarketStocks();
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

