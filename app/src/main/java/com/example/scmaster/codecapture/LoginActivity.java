package com.example.scmaster.codecapture;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by scmaster on 2017-12-28.
 */

public class LoginActivity extends AppCompatActivity{

    private EditText mail;
    private EditText pw;
    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mail = findViewById(R.id.email);
        pw = findViewById(R.id.pw);

        Button login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                email = mail.getText().toString();
                password = pw.getText().toString();
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                try {
                    String result = new NetworkTask().execute(params).get();
                    if(result.equals("true")){
                        //로그인 성공 시
                    }
                }catch(Exception e){

                }
            }
        });

    }

    public class NetworkTask extends AsyncTask<Map<String, String>, Integer, String> {

        @Override
        protected String doInBackground(Map<String, String>... maps) { // 내가 전송하고 싶은 파라미터

            // Http 요청 준비 작업
            HttpClient.Builder http = new HttpClient.Builder("GET", "http://10.10.16.129:8081/cc/login");

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
