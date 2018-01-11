package com.example.scmaster.codecapture;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by scmaster on 2018-01-10.
 */
public class ShareListAdapter extends BaseAdapter {
    LayoutInflater inflater = null;
    private ArrayList<ShareData> myList = null;
    private int listCount = 0;

    public ShareListAdapter(ArrayList<ShareData> list) {

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
            convertView = inflater.inflate(R.layout.share_list_view, parent, false);
        }
        TextView shareTitle = (TextView) convertView.findViewById(R.id.shareTitle);
        TextView shareDate = (TextView) convertView.findViewById(R.id.shareDate);
        TextView shareSender = (TextView) convertView.findViewById(R.id.shareSender);
        TextView shareNum = (TextView) convertView.findViewById(R.id.shareNum);
        Button btn_receive = (Button) convertView.findViewById(R.id.btn_receive);
        Button btn_reject = (Button) convertView.findViewById(R.id.btn_reject);

        System.out.println("==========> "+position);
        shareTitle.setText(myList.get(position).shareTitle);
        shareDate.setText(myList.get(position).shareDate);
        shareSender.setText(myList.get(position).shareSender);
        shareNum.setText(myList.get(position).shareNum);
        shareNum.setVisibility(View.GONE);
        btn_receive.setOnClickListener(myList.get(position).onClickListener);
        btn_reject.setOnClickListener(myList.get(position).onClickListener);

        convertView.setTag(""+position);

        return convertView;
    }
}
