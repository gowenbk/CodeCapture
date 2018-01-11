package com.example.scmaster.codecapture;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

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

/**
 * Created by SCMaster on 2018-01-08.
 */

public class CodedetailActivity extends AppCompatActivity {
    final String hostIP = "10.10.12.25:8081";
    private Code code;
    EditText detailTitle, detailCode, detailTag, detailComment, detailRef;
    TextView detailDate;
    Button detailSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailcode);

        code = (Code)getIntent().getSerializableExtra("codeDetail");

        detailTitle = (EditText) findViewById(R.id.detail_title);
        detailCode = (EditText) findViewById(R.id.detail_code);
        detailTag = (EditText) findViewById(R.id.detail_tag);
        detailComment = (EditText) findViewById(R.id.detail_comment);
        detailRef = (EditText) findViewById(R.id.detail_ref);
        detailDate = (TextView) findViewById(R.id.detail_date);

        Spinner detailFolder = (Spinner) findViewById(R.id.detail_folder);
        RadioGroup detailLanguage = (RadioGroup) findViewById(R.id.detail_language);
        ToggleButton detailStar = (ToggleButton) findViewById(R.id.detail_star);


        detailTitle.setText(code.getC_title());
        detailCode.setText(code.getC_code());
        detailTag.setText(code.getTag().toString());
        detailComment.setText(code.getC_comment());
        detailRef.setText(code.getC_ref());
        detailDate.setText(code.getC_date());

    }
    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 1){
                //저장해서 서버에로 보내는 것
            }
        }
    };

    private Boolean saveDetailCode(){
        try {
            Code code = new Code();
            code.setC_title(detailTitle.getText().toString());
            code.setC_comment(detailComment.getText().toString());
            code.setC_code(detailCode.getText().toString());
            code.setC_ref(detailRef.getText().toString());
            //태그 제외

            URL url = new URL("http://"+hostIP+"/cc/codeUpdateMobile");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.connect();

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }
}
