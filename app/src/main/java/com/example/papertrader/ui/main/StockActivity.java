package com.example.papertrader.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.papertrader.R;
import com.example.papertrader.api.ApiConnection;
import com.example.papertrader.data.SharedViewModel;
import com.example.papertrader.ui.adapters.HoldingsStockListAdapter;
import com.example.papertrader.ui.adapters.StockGridAdapter;
import com.example.papertrader.ui.login.AuthHandler;
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
    private AuthHandler authHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        Bundle b = getIntent().getExtras();
        String ticker = null;
        if(b != null)
            ticker = b.getString("Ticker");
            TextView textview_stock_ticker = (TextView) findViewById(R.id.stockStatTicker);
            textview_stock_ticker.setText(ticker);


        authHandler = new AuthHandler(this, this);
        SharedViewModel model = new ViewModelProvider(this).get(SharedViewModel.class);
        model.getStockInfo(authHandler.getAuthToken(), ticker).observe(this, stock_stats -> {
            String jsonString = stock_stats.toString();
            System.out.println("JSON IN STOCK ACTIVITY: " + jsonString);

            try {
                JSONObject owned_stock_info = stock_stats.getJSONObject("owned_stock_info");
                JSONObject stock_info = stock_stats.getJSONObject("stock_info");

                String stock_name = stock_info.getString("name");
                String logo_url = stock_info.getString("logo_link");
                TextView textview_stock_name = (TextView) findViewById(R.id.stockStatName);
                textview_stock_name.setText(stock_name);

                ImageView imageview_logo = (ImageView) findViewById(R.id.stockStatLogo);
                Picasso.get().load(logo_url).into(imageview_logo);

                String stockInfoString = stock_stats.toString();
                System.out.println("JSON IN STOCK ACTIVITY: " + stockInfoString);

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

                    System.out.println("stock_info_keys:");
                System.out.println(stock_info_keys);

                StockGridAdapter gridAdapter = new StockGridAdapter(this, stock_info_keys, stock_info_values);
                GridView stats_grid_view = (GridView) findViewById(R.id.stockStatsGridView);
                System.out.println("GRID VIEW:");
                System.out.println(stats_grid_view);
                stats_grid_view.setAdapter(gridAdapter);


            } catch (JSONException e) {
                e.printStackTrace();
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
