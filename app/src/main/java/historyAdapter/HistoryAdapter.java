/* Copyright (C) 2016 Siu-Kei Muk, Snigdha Brahma
 * All rights reserved.
 *
 * This software may be modified and distributed under the terms
 * of the BSD license.  See the LICENSE file for details.
 */

package historyAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import edu.anu.comp6442.assignment2.R;

/**
 * COMP6442 - Software Construction
 * Assignment 2 - Calculator
 * Authors: Siu Kei, Muk & Snigdha Brahma
 *
 * This class defines an adapter in used in the history list.
 */
public class HistoryAdapter extends ArrayAdapter<Map<String,String>>{
    Context context;
    LayoutInflater inflater;
    int resourceId;
    List<Map<String,String>> list;
    String[] keys;

    public HistoryAdapter(Context context, int resourceId, List<Map<String,String>> list, String[] keys) {
        super(context,resourceId,list);

        this.context = context;
        this.resourceId = resourceId;
        this.list = list;
        this.keys = keys;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(resourceId,parent,false);
            holder = new ViewHolder();
            holder.exp_text = (TextView) convertView.findViewById(R.id.exp_field);
            holder.value_text = (TextView) convertView.findViewById(R.id.value_field);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.exp_text.setText(list.get(position).get(keys[0]));
        holder.value_text.setText("=" + list.get(position).get(keys[1]));
        return convertView;
    }

    private class ViewHolder{
        TextView exp_text;
        TextView value_text;
    }

    @Override
    public void remove(Map<String, String> object) {
        list.remove(object);
        notifyDataSetChanged();
    }
}
