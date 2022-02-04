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

import objects.MarketStockObject;

public class MarketStockListAdapter extends ArrayAdapter<MarketStockObject> {

    private static final String TAG = "MarketStockListAdapter";

    private final Context mContext;
    int mResource;

    public MarketStockListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<MarketStockObject> marketStockObjects) {
        super(context, resource, marketStockObjects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        MarketStockObject marketStockObject = getItem(position);
        String ticker = marketStockObject.getTicker();
        String companyName = marketStockObject.getCompanyName();
        String sector = marketStockObject.getSector();
        String exchange = marketStockObject.getExchange();
        String country = marketStockObject.getCountry();
        String currency = marketStockObject.getCurrency();
        String industry = marketStockObject.getIndustry();
        String  logoUrl = marketStockObject.getLogoUrl();



        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        // Set text and image views to correct information
        TextView tickerTextView = convertView.findViewById(R.id.companyTicker);
        TextView companyNameTextView = convertView.findViewById(R.id.companyName);
        TextView sectorTextView = convertView.findViewById(R.id.companySector);
        TextView exchangeTextView = convertView.findViewById(R.id.exchange);
        TextView countryTextView = convertView.findViewById(R.id.companyCountry);
        TextView currencyTextView = convertView.findViewById(R.id.companyCurrency);
//        TextView industryTextView = (TextView) convertView.findViewById(R.id.);
        ImageView logoUrlImageView = convertView.findViewById(R.id.companyLogo);


        tickerTextView.setText(ticker);
        companyNameTextView.setText(companyName);
        sectorTextView.setText(sector);
        exchangeTextView.setText(exchange);
        countryTextView.setText(country);
        currencyTextView.setText(currency);
        Picasso.get().load(logoUrl).into(logoUrlImageView);

        return convertView;

    }
}
