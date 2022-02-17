package com.example.papertrader.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.papertrader.R;

public class StockGridAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    String[] stat_names;
    String[] stat_values;

    public StockGridAdapter(Context appContext, String[] stat_names, String[] stat_values){
        this.stat_names = stat_names;
        this.stat_values = stat_values;

        this.context = appContext;

    }

    @Override
    public int getCount() {
        return stat_names.length;
    }

    @Override
    public Object getItem(int position) {
        return stat_names[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.activity_stock, null); // inflate the layout

        TextView statName = (TextView) convertView.findViewById(R.id.stockStatName);
        TextView statValue = (TextView) convertView.findViewById(R.id.stockStatValue);

        statName.setText(stat_names[position]);
        statValue.setText((stat_values[position]));

        return convertView;
    }
}
