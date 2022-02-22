package com.example.papertrader.ui.adapters;

import android.content.Context;
import android.content.res.Resources;
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
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.stats_grid_item, null);
            TextView statName = (TextView) convertView.findViewById(R.id.stockStatName);
            TextView statValue = (TextView) convertView.findViewById(R.id.stockStatValue);

            String string_name_key = "_" + stat_names.get(position);
            int name_resId = context.getResources().getIdentifier(string_name_key, "string", context.getPackageName());
            String name = context.getString(name_resId);
            statName.setText(name);
            String stat_value = stat_values.get(position);

            if(stat_value.equals("null")) stat_value = "N/A";
            statValue.setText(stat_value);
        }


        return convertView;
    }
}
