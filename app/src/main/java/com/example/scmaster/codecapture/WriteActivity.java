package com.example.scmaster.codecapture;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.gson.Gson;

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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by scmaster on 2017-12-29.
 */

public class WriteActivity extends AppCompatActivity {

    private EditText title;
    private Spinner folder;
    private RadioGroup language;
    private EditText code;
    private EditText comment;
    private EditText tag;
    private EditText ref;
    private TextView date;
    private ToggleButton star;

    private int[] folderIndex;
    private EditText c_title;
    private Spinner c_folder;
    private RadioGroup c_language;
    private EditText c_comment;
    private EditText c_tag;
    private EditText c_ref;
    private TextView c_date;
    private ToggleButton c_star;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        //변수 초기화
        title = findViewById(R.id.c_title);
        folder = findViewById(R.id.c_folder);
        language = findViewById(R.id.c_language);
        code = findViewById(R.id.c_code);
        tag = findViewById(R.id.c_tag);
        comment = findViewById(R.id.c_comment);
        ref = findViewById(R.id.c_ref);
        date = findViewById(R.id.c_date);
        star = findViewById(R.id.c_star);

        //폴더명 초기화
        new Thread() {
            public void run() {
                ArrayList<Folder> list = getFolder();
                folderIndex = new int[list.size()];
                ArrayList<String> folderNames = new ArrayList<>();
                int index = 0;
                for(Folder f : list){
                    folderIndex[index] = f.getF_num();
                    folderNames.add(f.getF_name());
                    index++;
                }
                Message message = handler.obtainMessage();
                message.what = 1;
                message.obj = folderNames;
                handler.sendMessage(message);
            }
        }.start();
        c_title = findViewById(R.id.c_title);
        c_folder = findViewById(R.id.c_folder);
        c_language = findViewById(R.id.c_language);
        c_tag = findViewById(R.id.c_tag);
        c_comment = findViewById(R.id.c_comment);
        c_ref = findViewById(R.id.c_ref);
        c_date = findViewById(R.id.c_date);
        c_star = findViewById(R.id.c_star);

        //폴더명 셋팅
        ArrayList<String> entries =  new ArrayList<>();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        c_folder.setAdapter(adapter);

        //날짜 셋팅
        final Calendar caldate = Calendar.getInstance();
        date.setText(caldate.get(Calendar.YEAR)+"-"+(caldate.get(Calendar.MONTH)+1)+"-"+caldate.get(Calendar.DAY_OF_MONTH));
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(WriteActivity.this, listener, caldate.get(Calendar.YEAR), caldate.get(Calendar.MONTH), caldate.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });

        //토글버튼
        star.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(star.isChecked())
                    return;
            }
        });


    }

    //코드 작성하기
    public void writeCode(View view){
        //요소 값 가져오기
        String c_title = title.getText().toString();
        int f_num = folderIndex[(int)folder.getSelectedItemId()];
        String f_name = folder.getSelectedItem().toString();
        RadioButton selectedRadio = findViewById(language.getCheckedRadioButtonId());
        String c_type = selectedRadio.getText().toString();
        String c_code = code.getText().toString();
        String c_comment = comment.getText().toString();
        String tags = tag.getText().toString();
        String c_ref = ref.getText().toString();
        String c_date = date.getText().toString();
        int c_star = 0;
        if(star.isChecked())
            c_star = 1;

        //Gson 객체로 묶어서 보내기
        Gson gson = new Gson();
        Code code = new Code(0, c_title, c_code, c_type, c_comment, c_date, c_ref, c_star, f_num, f_name, null);
        String codeStr = gson.toJson(code);
        Map<String, String> param = new HashMap<>();
        param.put("codeStr", codeStr);
        param.put("tags", tags);
        new NetworkTaskWrite().execute(param);
    }

    //메인으로
    public void goBack(View view){

        //아직 작성 안 함
    }

    //날짜 선택 시
    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            date.setText(year+"-"+monthOfYear+"-"+dayOfMonth);
        }
    };

    private ArrayList<Folder> getFolder(){
        ArrayList<Folder> items = new ArrayList<>();

        try {
            URL url = new URL("http://10.10.16.129:8081/cc/getFolder");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            BufferedReader bufferedReader =
                    new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String next;
            while ((next = bufferedReader.readLine()) != null){
                JSONArray ja = new JSONArray(next);

                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = (JSONObject) ja.get(i);
                    items.add(new Folder(jo.getInt("f_num"), jo.getString("f_name"), jo.getString("f_email")));
                }
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
        return items;
    }

    //폴더명 세팅하기
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1) {
                ArrayList<String> folderNames = (ArrayList<String>) msg.obj;
                ArrayAdapter<String> arrAdapt = new ArrayAdapter<>(WriteActivity.this, R.layout.support_simple_spinner_dropdown_item, folderNames);
                folder.setAdapter(arrAdapt);
            }
        }
    };

    //서버 쪽으로 보내기 (코드 작성하기)
    public class NetworkTaskWrite extends AsyncTask<Map<String, String>, Integer, String> {

        @Override
        protected String doInBackground(Map<String, String>... maps) { // 내가 전송하고 싶은 파라미터

            // Http 요청 준비 작업
            HttpClient.Builder http = new HttpClient.Builder("POST", "http://10.10.16.129:8081/cc/codeWrite");

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
