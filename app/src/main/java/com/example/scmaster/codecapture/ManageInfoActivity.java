package com.example.scmaster.codecapture;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
 * Created by SCMaster on 2018-01-04.
 */

public class ManageInfoActivity extends AppCompatActivity {

    private TextView username;
    private TextView useremail;
    private String name;
    private String email;
    private EditText question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manageinfo);

        username = findViewById(R.id.username);
        useremail = findViewById(R.id.useremail);
        question = findViewById(R.id.messagebox);

        //이름, 이메일 세팅하기
        new Thread() {
            public void run() {
                getMemberInfo();
                Message message = handler.obtainMessage();
                message.what = 1;
                handler.sendMessage(message);
            }
        }.start();

        ImageButton editBtn = findViewById(R.id.editprofile);
        editBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                show();
            }
        });

        Button sendQNA = findViewById(R.id.btn_send);
        sendQNA.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String qtext = question.getText().toString();
                Map<String, String> param = new HashMap<>();
                param.put("qtext", qtext);
                try {
                    Toast.makeText(ManageInfoActivity.this, qtext, Toast.LENGTH_SHORT);
                    String result = new NetworkTaskQNA().execute(param).get();
                    Toast.makeText(ManageInfoActivity.this, result, Toast.LENGTH_SHORT);
                    Message message = handler.obtainMessage();
                    message.what = 3;
                    handler.sendMessage(message);
                }catch(Exception e){}
            }
        });

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //이름, 이메일 세팅
            if (msg.what == 1) {
                username.setText(name);
                useremail.setText(email);
            }else if(msg.what ==2){
              //이름 수정
                username.setText((String)msg.obj);
            }else{
                question.setText("");
            }
        }
    };

    private void getMemberInfo() {
        try {
            URL url = new URL("http://10.10.16.129:8081/cc/callProfile");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            BufferedReader bufferedReader =
                    new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String next;
            while ((next = bufferedReader.readLine()) != null) {
                JSONObject ja = new JSONObject(next);
                name = ja.getString("name");
                email = ja.getString("email");
            }
            bufferedReader.close();
            urlConnection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void show() {
        final EditText edittext = new EditText(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Your Profile");
        builder.setMessage("You can change your name");
        builder.setView(edittext);
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String newName = edittext.getText().toString();
                        Map<String, String> param = new HashMap<>();
                        param.put("name", newName);
                        try {
                            String result = new NetworkTask().execute(param).get();
                            if(result.equals("true")){
                                Message message = handler.obtainMessage();
                                message.what = 2;
                                message.obj = newName;
                                handler.sendMessage(message);
                            }
                        } catch (Exception e) {

                        }
                    }
                });
        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();
    }

    //서버 쪽으로 보내기 (코드 작성하기)
    public class NetworkTask extends AsyncTask<Map<String, String>, Integer, String> {

        @Override
        protected String doInBackground(Map<String, String>... maps) { // 내가 전송하고 싶은 파라미터

            // Http 요청 준비 작업
            HttpClient.Builder http = new HttpClient.Builder("GET", "http://10.10.16.129:8081/cc/updateProfile");

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
    public class NetworkTaskQNA extends AsyncTask<Map<String, String>, Integer, String> {

        @Override
        protected String doInBackground(Map<String, String>... maps) { // 내가 전송하고 싶은 파라미터

            // Http 요청 준비 작업
            HttpClient.Builder http = new HttpClient.Builder("GET", "http://10.10.16.129:8081/cc/sendQNA");

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
