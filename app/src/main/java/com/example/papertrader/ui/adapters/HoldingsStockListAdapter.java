package com.example.papertrader.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.papertrader.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

import objects.HoldingsStockObject;

public class HoldingsStockListAdapter extends ArrayAdapter<HoldingsStockObject> {
    private static final String TAG = "HoldingsStockListAdapter";

    private final Context mContext;
    int mResource;

    public HoldingsStockListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<HoldingsStockObject> holdingsStockObject) {
        super(context, resource, holdingsStockObject);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        HoldingsStockObject holdingsStockObject = getItem(position);
        String ticker = holdingsStockObject.getTicker();
        String companyName = holdingsStockObject.getCompanyName();

        String stockPrice = String.format("%.2f", holdingsStockObject.getAverage_price_per_stock());
        String stockTotalValue = String.format("%.2f", holdingsStockObject.getTotal_value());
        String numberOfUnitsOwned = Integer.toString(holdingsStockObject.getAmount_owned());

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        // Set text and image views to correct information
        TextView tickerTextView = convertView.findViewById(R.id.companyTicker);
        TextView companyNameTextView = convertView.findViewById(R.id.companyName);;
        TextView stockPriceTextView = convertView.findViewById(R.id.stockPrice);
        TextView stockTotalValueTextView = convertView.findViewById(R.id.stockCurrentValue);
        TextView numberOfUnitsOwnedTextView = convertView.findViewById(R.id.numberOfUnitsOwned);


        tickerTextView.setText(ticker);
        companyNameTextView.setText(companyName);
        stockPriceTextView.setText(stockPrice);
        stockTotalValueTextView.setText(stockTotalValue);
        numberOfUnitsOwnedTextView.setText(numberOfUnitsOwned);

        return convertView;

    }

}
