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

import java.util.ArrayList;

import objects.MarketStockObject;
import objects.PastTransactionObject;

public class PastTransactionsListAdapter extends ArrayAdapter<PastTransactionObject> {
    private final Context mContext;
    int mResource;

    public PastTransactionsListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<PastTransactionObject> pastTransactionObjects) {
        super(context, resource, pastTransactionObjects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        PastTransactionObject pastTransactionObject = getItem(position);
        String ticker = pastTransactionObject.getTicker();
        String type = pastTransactionObject.getType();
        String stock_amount = String.valueOf(pastTransactionObject.getStock_amount());
        String price_per_stock = pastTransactionObject.getPrice_per_stock();
        String total_value = pastTransactionObject.getTotal_value();



        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        // Set text and image views to correct information
        TextView tickerTextView = convertView.findViewById(R.id.transactionTicker);
        TextView typeTextView = convertView.findViewById(R.id.transactionType);
        TextView stockAmountTextView = convertView.findViewById(R.id.transactionNumberOfUnits);
        TextView pricePerStockTextView = convertView.findViewById(R.id.transactionPricePerStock);
        TextView totalValueTextView = convertView.findViewById(R.id.transactionTotalValue);



        tickerTextView.setText(ticker);
        typeTextView.setText(type);
        stockAmountTextView.setText(stock_amount);
        pricePerStockTextView.setText(String.valueOf(price_per_stock));
        totalValueTextView.setText(String.valueOf(total_value));

        return convertView;

    }
}
