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
 * Created by SCMaster on 2018-01-04.
 */

public class RegisterActivity extends AppCompatActivity{
    private EditText id, name, pw;
    private String idValue, nameValue, pwValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        id = (EditText) findViewById(R.id.id);
        name = (EditText) findViewById(R.id.name);
        pw = (EditText) findViewById(R.id.password);

        Button register = findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Map<String, String> param = new HashMap<>();
                try {
                    idValue = id.getText().toString();
                    nameValue = name.getText().toString();
                    pwValue = pw.getText().toString();

                    param.put("email", idValue);
                    param.put("name", nameValue);
                    param.put("password", pwValue);

                    String result = new NetworkTask().execute(param).get();
                    if(result.equals("RegisterSuccess")){
                        //로그인 성공 시
                        Toast.makeText(RegisterActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                    } else if(result.equals("RegisterFail")){
                        //로그인 실패 시
                        Toast.makeText(RegisterActivity.this, "회원가입 실패", Toast.LENGTH_SHORT).show();
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
            HttpClient.Builder http = new HttpClient.Builder("GET", "http://10.10.12.25:8081/cc/join");

            // Parameter 를 전송한다.
            http.addAllParameters(maps[0]);
            //Http 요청 전송
            HttpClient post = http.create();
            post.request();

            // 응답 상태코드 가져오기
            int statusCode = post.getHttpStatusCode();

            // 응답 본문 가져오기
            String body = post.getBody();

            //바디바디
            return body;
        }

    }

}
