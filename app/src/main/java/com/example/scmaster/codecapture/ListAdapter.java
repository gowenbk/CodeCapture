package com.example.scmaster.codecapture;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by SCMaster on 2018-01-08.
 */

public class ListAdapter extends BaseAdapter{
    LayoutInflater inflater = null;
    private ArrayList<CodeData> myList = null;
    private int listCount = 0;

    public ListAdapter(ArrayList<CodeData> list) {
        myList = list;
        listCount = list.size();
    }

    @Override
    public int getCount() {
        return listCount;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            final Context context = parent.getContext();
            if(inflater == null){
                inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }
        TextView codeTitle = (TextView) convertView.findViewById(R.id.codeTitle);
        TextView codeDate = (TextView) convertView.findViewById(R.id.codeDate);
        TextView codeComment = (TextView) convertView.findViewById(R.id.codeComment);
        TextView codeNum = (TextView) convertView.findViewById(R.id.codeNum);
        Button selectBtn = (Button) convertView.findViewById(R.id.btnSelect);

        codeTitle.setText(myList.get(position).codeTitle);
        codeDate.setText(myList.get(position).codeDate);
        codeComment.setText(myList.get(position).codeComment);
        codeNum.setText(myList.get(position).codeNum);
        codeNum.setVisibility(View.GONE);
        selectBtn.setOnClickListener(myList.get(position).onClickListener);

        convertView.setTag(""+position);

        return convertView;
    }
}

