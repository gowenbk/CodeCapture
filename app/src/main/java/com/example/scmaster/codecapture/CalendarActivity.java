package com.example.scmaster.codecapture;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * Created by scmaster on 2017-12-29.
 */

public class CalendarActivity extends AppCompatActivity {

    MaterialCalendarView calendar;
    TableLayout tableLayout;
    private ArrayList<Code> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        calendar = findViewById(R.id.calendarView);
        tableLayout = findViewById(R.id.table);

        new Thread() {
            public void run() {
                list = getCalendarCode();
                ArrayList<String> codeType = new ArrayList<>();
                ArrayList<String> codeTitles = new ArrayList<>();
            }
        }.start();

        //일요일부터 시작
        calendar.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        //토요일, 일요일 꾸미기
        calendar.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator());

        //선택 시 색상
        calendar.setSelectionColor(Color.rgb(169,184,201));

        //빨간 점 찍기
        ApiSimulator apiSimulator = new ApiSimulator();
        apiSimulator.onPostExecute(apiSimulator.doInBackground());

        //클릭 이벤트
        calendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                tableLayout.removeAllViews();
                for (int tr = 0; tr < list.size(); tr++) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        CalendarDay codeDate = CalendarDay.from(format.parse(list.get(tr).getC_date()));
                        if (codeDate.equals(date)) {
                            TableRow row = new TableRow(CalendarActivity.this);
                            TextView t_type = new TextView(CalendarActivity.this);
                            TextView t_title = new TextView(CalendarActivity.this);
                            TableRow.LayoutParams param = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            param.leftMargin = 10;
                            param.rightMargin = 30;
                            t_type.setText(list.get(tr).getC_type());
                            t_type.setLayoutParams(param);
                            t_title.setText(list.get(tr).getC_title());
                            row.addView(t_type);
                            row.addView(t_title);
                            tableLayout.addView(row);
                        }
                    }catch(Exception e){}
                }
            }
        });
    }


    private ArrayList<Code> getCalendarCode(){
        ArrayList<Code> items = new ArrayList<>();

        try {
            URL url = new URL("http://10.10.16.129:8081/cc/callCalendar");
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
                    items.add(new Code(jo.getInt("c_num"), jo.getString("c_title"), jo.getString("c_code")
                    , jo.getString("c_type"), jo.getString("c_comment"), jo.getString("c_date")
                    , jo.getString("c_ref"), jo.getInt("c_star"), jo.getInt("f_num"), jo.getString("f_name"), null));
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

    //토요일 디자인
    public class SaturdayDecorator implements DayViewDecorator {

        private final Calendar calendar = Calendar.getInstance();

        public SaturdayDecorator() {
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            day.copyTo(calendar);
            int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
            return weekDay == Calendar.SATURDAY;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(Color.BLUE));
        }
    }

    //일요일 디자인
    class SundayDecorator implements DayViewDecorator {

        private final Calendar calendar = Calendar.getInstance();

        public SundayDecorator() {
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            day.copyTo(calendar);
            int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
            return weekDay == Calendar.SUNDAY;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(Color.RED));
        }
    }

    //이벤트
    public class EventDecorator implements DayViewDecorator {

        private final int color;
        private final HashSet<CalendarDay> dates;

        public EventDecorator(int color, Collection<CalendarDay> dates) {
            this.color = color;
            this.dates = new HashSet<>(dates);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return dates.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new DotSpan(5, color));
        }
    }

    //빨간 점 찍기
    private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>>{

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ArrayList<CalendarDay> dates = new ArrayList<>();
            for(Code c : list){
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date date = format.parse(c.getC_date());
                    dates.add(CalendarDay.from(date));
                }catch(ParseException e){
                    e.printStackTrace();
                }
            }

            return dates;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            if (isFinishing()) {
                return;
            }

            calendar.addDecorator(new EventDecorator(Color.RED, calendarDays));        }
    }
}
