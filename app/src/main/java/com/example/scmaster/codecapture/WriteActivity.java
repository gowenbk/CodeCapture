package com.example.scmaster.codecapture;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;

/**
 * Created by scmaster on 2017-12-29.
 */

public class WriteActivity extends AppCompatActivity {

    private EditText c_title;
    private Spinner c_folder;
    private RadioGroup c_language;
    private EditText c_comment;
    private EditText c_tag;
    private EditText c_memo;
    private EditText c_ref;
    private TextView c_date;
    private ToggleButton c_star;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        //변수 초기화
        c_title = findViewById(R.id.c_title);
        c_folder = findViewById(R.id.c_folder);
        c_language = findViewById(R.id.c_language);
        c_comment = findViewById(R.id.c_comment);
        c_tag = findViewById(R.id.c_tag);
        c_memo = findViewById(R.id.c_memo);
        c_ref = findViewById(R.id.c_ref);
        c_date = findViewById(R.id.c_date);
        c_star = findViewById(R.id.c_star);

        //폴더명 셋팅
        ArrayList<String> entries =  new ArrayList<>();
//        ArrayAdapter<String> arrAdapt = new ArrayAdapter<String>(this, R.layout.list_item, entries);
//        arrAdapt.add("List Item C");
//        arrAdapt.notifyDataSetChanged();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        c_folder.setAdapter(adapter);

        //날짜 셋팅
        final Calendar caldate = Calendar.getInstance();
        c_date.setText(caldate.get(Calendar.YEAR)+"-"+(caldate.get(Calendar.MONTH)+1)+"-"+caldate.get(Calendar.DAY_OF_MONTH));
        c_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(WriteActivity.this, listener, caldate.get(Calendar.YEAR), caldate.get(Calendar.MONTH), caldate.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });

        //토글버튼
        c_star.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(c_star.isChecked())
                    return;
            }
        });


    }

    //메인으로
    public void goBack(View view){

    }

    //날짜 선택 시
    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            c_date.setText(year+"-"+monthOfYear+"-"+dayOfMonth);
        }
    };

    //서버 쪽으로 보내기
    public class NetworkTask extends AsyncTask<Map<String, String>, Integer, String> {

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
