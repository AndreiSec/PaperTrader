package com.example.papertrader.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.papertrader.R;
import com.example.papertrader.api.ApiConnection;
import com.example.papertrader.api.ResponseCallBack;
import com.example.papertrader.data.SharedViewModel;
import com.example.papertrader.ui.adapters.HoldingsStockListAdapter;
import com.example.papertrader.ui.adapters.StockGridAdapter;
import com.example.papertrader.ui.login.AuthHandler;
import com.google.android.gms.common.api.Api;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import objects.HoldingsStockObject;

public class StockActivity extends AppCompatActivity {

    private FloatingActionButton button_back;
    private ApiConnection apiConnection;
    private Button buy_button;
    private Button sell_button;
    private AuthHandler authHandler;
    private SharedViewModel model;
    private String ticker;
    private float user_balance;



    void perform_transaction(String type, int stock_amount){
        System.out.println(type + " " + stock_amount + " " + ticker + " for " + authHandler.getAuthToken());
        apiConnection.perform_transaction(new ResponseCallBack() {

            @Override
            public void getJsonResponse(JSONObject json) {
                String jsonString = json.toString();
                String message;
                try {
                    message = (String) json.get("message");
                    runOnUiThread(new Runnable(){
                        public void run(){
                            Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
                            toast.show();
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, authHandler.getAuthToken(), type, ticker, stock_amount);
    }


    void showTransactionDialog(String transactionType) {
        final Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setCancelable(true);

        dialog.setContentView(R.layout.perform_transaction_dialog);

        // Init views

        final TextView balanceTextView = dialog.findViewById(R.id.perform_transaction_balance);
        final EditText stockAmountEditText = dialog.findViewById(R.id.editText_number_to_transact);
        final Button performTransactionButton = dialog.findViewById(R.id.button_perform_transaction);

        model.get_user_balance(authHandler.getAuthToken()).observe(this, balance -> {
            user_balance = balance;
            balanceTextView.setText("$" + String.valueOf(user_balance));
        });


        if(transactionType.equals("buy")){
            performTransactionButton.setText("BUY");
            performTransactionButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(! stockAmountEditText.getText().toString().equals("")){
                        perform_transaction("buy", Integer.valueOf(String.valueOf(stockAmountEditText.getText())));
                    }

                }
            });
        }
        else{
            performTransactionButton.setText("SELL");
            performTransactionButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(! stockAmountEditText.getText().toString().equals("")){
                        perform_transaction("sell", Integer.valueOf(String.valueOf(stockAmountEditText.getText())));
                    }
                }
            });
        }


        dialog.show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        Bundle b = getIntent().getExtras();
        ticker = null;
        if(b != null)
            ticker = b.getString("Ticker");
            TextView textview_stock_ticker = (TextView) findViewById(R.id.stockStatTicker);
            textview_stock_ticker.setText(ticker);


        authHandler = new AuthHandler(this, this);
        model = new ViewModelProvider(this).get(SharedViewModel.class);
        model.getStockInfo(authHandler.getAuthToken(), ticker).observe(this, stock_stats -> {

            try {
                JSONObject owned_stock_info = stock_stats.getJSONObject("owned_stock_info");
                JSONObject stock_info = stock_stats.getJSONObject("stock_info");

                String stock_name = stock_info.getString("name");
                String logo_url = stock_info.getString("logo_link");
                TextView textview_stock_name = (TextView) findViewById(R.id.stockStatName);
                textview_stock_name.setText(stock_name);

                ImageView imageview_logo = (ImageView) findViewById(R.id.stockStatLogo);
                Picasso.get().load(logo_url).into(imageview_logo);


                Iterator<String> keys = stock_info.keys();

                ArrayList<String> stock_info_keys = new ArrayList<String>();
                ArrayList<String> stock_info_values = new ArrayList<String>();

                while(keys.hasNext()) {
                    String key = keys.next();

                    String[] black_listed_keys = {"name", "ticker", "logo_link"};

                    if(Arrays.asList(black_listed_keys).contains(key)){
                        // Iterate over black listed keys
                        continue;
                    }
                    stock_info_keys.add(key);

                    try {
                        stock_info_values.add(stock_info.getString(key));
                    } catch (JSONException e) {
                        stock_info_values.add("");
                    }

                }


                StockGridAdapter gridAdapter = new StockGridAdapter(this, stock_info_keys, stock_info_values);
                GridView stats_grid_view = (GridView) findViewById(R.id.stockStatsGridView);
                stats_grid_view.setAdapter(gridAdapter);


            } catch (JSONException e) {
                e.printStackTrace();
            }




        });


        apiConnection = ApiConnection.getInstance();


        buy_button = findViewById(R.id.button_stock_buy);
        buy_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showTransactionDialog("buy");
            }
        });

        sell_button = findViewById(R.id.button_stock_sell);
        sell_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showTransactionDialog("sell");
            }
        });


        button_back = findViewById(R.id.button_stock_screen_back);
        button_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish(); // Ends activity and returns to the previous
            }
        });
    }
}
