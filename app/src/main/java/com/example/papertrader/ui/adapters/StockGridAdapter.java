package com.example.papertrader.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.papertrader.R;

import java.util.ArrayList;

public class StockGridAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    ArrayList<String> stat_names;
    ArrayList<String> stat_values;

    public StockGridAdapter(Context appContext, ArrayList<String> stat_names, ArrayList<String> stat_values){
        this.stat_names = stat_names;
        this.stat_values = stat_values;

        this.context = appContext;

    }

    @Override
    public int getCount() {
        return stat_names.size();
    }

    @Override
    public Object getItem(int position) {
        return stat_names.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        System.out.println("SETTING: " + stat_names.get(position));
//        convertView = inflater.inflate(R.layout.stats_grid_item, null); // inflate the layout
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.stats_grid_item, null);
            TextView statName = (TextView) convertView.findViewById(R.id.stockStatName);
            TextView statValue = (TextView) convertView.findViewById(R.id.stockStatValue);

            statName.setText(stat_names.get(position));
            statValue.setText((stat_values.get(position)));
        }


        return convertView;
    }
}
