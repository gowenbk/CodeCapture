package com.example.scmaster.codecapture;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by scmaster on 2018-01-10.
 */

public class ShareConfirmActivity extends AppCompatActivity implements View.OnClickListener{

    ListView listItemView;
    ShareListAdapter adapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shareconfirm);
        listItemView = findViewById(R.id.shareListView);
        init();
    }

    public void init(){
        new Thread(){
            public void run(){
                ArrayList<Share> list = getShareList();
                Message msg = handler.obtainMessage();
                msg.what = 1;
                msg.obj = list;
                handler.sendMessage(msg);
            }
        }.start();
    }

    //공유목록 세팅하기
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1) {
                ArrayList<Share> list = (ArrayList<Share>)msg.obj;
                ArrayList<ShareData> newList = new ArrayList<>();

                for(Share s : list){
                    System.out.println(s.toString());
                    ShareData data = new ShareData();
                    data.shareTitle = s.getC_title();
                    data.shareDate = s.getS_date();
                    data.shareSender = s.getS_sender();
                    data.shareNum = s.getS_num()+"";
                    data.onClickListener = ShareConfirmActivity.this;
                    newList.add(data);
                }
                adapter =  new ShareListAdapter(newList);
                listItemView.setAdapter(adapter);
            }
        }
    };

    //해당버튼 눌렀을때 share_num에 맞춰서 넘어가기
    @Override
    public void onClick(View v){
        View parentView = (View)v.getParent();
        TextView shareNum= parentView.findViewById(R.id.shareNum);
        //String position = (String)parentView.getTag();
        String s_num = shareNum.getText().toString();
        Map<String, String> param = new HashMap<>();
        param.put("s_num", s_num);
        String result = "";
        try {
            switch (v.getId()) {
                case R.id.btn_receive:
                    result = new NetworkTaskReceive().execute(param).get();
                    break;
                case R.id.btn_reject:
                    result = new NetworkTaskReject().execute(param).get();
                    break;
            }
        }catch(Exception e){}

        Toast.makeText(ShareConfirmActivity.this, result, Toast.LENGTH_SHORT);
        LinearLayout shareEach = findViewById(R.id.shareEach);
        shareEach.removeAllViews();
        init();
    }

    private ArrayList<Share> getShareList(){
        ArrayList<Share> items = new ArrayList<>();

        try {
            URL url = new URL("http://10.10.16.129:8081/cc/getShare");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // gets the server json data
            BufferedReader bufferedReader =
                    new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String next;
            while ((next = bufferedReader.readLine()) != null){
                JSONArray ja = new JSONArray(next);

                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = (JSONObject) ja.get(i);
                    items.add(new Share(jo.getInt("s_num"), jo.getString("s_sender"),
                            jo.getString("s_receiver"), jo.getInt("s_context"),
                            jo.getString("s_date"), jo.getString("c_title")));
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return items;
    }

    //서버 쪽으로 보내기 (코드 작성하기)
    public class NetworkTaskReject extends AsyncTask<Map<String, String>, Integer, String> {

        @Override
        protected String doInBackground(Map<String, String>... maps) { // 내가 전송하고 싶은 파라미터

            // Http 요청 준비 작업
            HttpClient.Builder http = new HttpClient.Builder("POST", "http://10.10.16.129:8081/cc/deleteShare");

            // Parameter 를 전송한다.
            http.addAllParameters(maps[0]);

            //Http 요청 전송
            HttpClient post = http.create();
            post.request();

            // 응답 상태코드 가져오기
            int statusCode = post.getHttpStatusCode();

            // 응답 본문 가져오기
            String body = post.getBody();

            return body;
        }

    }

    //서버 쪽으로 보내기 (코드 작성하기)
    public class NetworkTaskReceive extends AsyncTask<Map<String, String>, Integer, String> {

        @Override
        protected String doInBackground(Map<String, String>... maps) { // 내가 전송하고 싶은 파라미터

            // Http 요청 준비 작업
            HttpClient.Builder http = new HttpClient.Builder("POST", "http://10.10.16.129:8081/cc/saveShare");

            // Parameter 를 전송한다.
            http.addAllParameters(maps[0]);

            //Http 요청 전송
            HttpClient post = http.create();
            post.request();

            // 응답 상태코드 가져오기
            int statusCode = post.getHttpStatusCode();

            // 응답 본문 가져오기
            String body = post.getBody();

            return body;
        }

    }
}
