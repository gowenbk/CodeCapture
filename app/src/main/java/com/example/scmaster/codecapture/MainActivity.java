package com.example.scmaster.codecapture;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

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
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    final String hostIP = "10.10.12.25:8081";

    ListView listItemView;
//    ArrayAdapter<String> adapter;
    ListAdapter adapter;
    static final String[] CODE_LIST = {"code1", "code2", "code3", "code4", "code5", "code6", "code7", "code8", "code9", "code10"};


    private Code getDetailCode(String codeNum){
        ArrayList<String> items = new ArrayList<String>();
        Code code = null;

        try {
            URL url = new URL("http://"+hostIP+"/cc/codeDetailMobile"+"?c_num="+codeNum);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.connect();

            // gets the server json data
            BufferedReader bufferedReader =
                    new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String next;
            while ((next = bufferedReader.readLine()) != null){
                JSONObject ja = new JSONObject(next);

                code = new Code();
                code.setC_comment(ja.getString("c_comment"));
                code.setC_code(ja.getString("c_code"));
                code.setC_date(ja.getString("c_date"));
                code.setC_num(Integer.parseInt(ja.getString("c_num")));
                code.setC_title(ja.getString("c_title"));
                code.setC_type(ja.getString("c_type"));
                code.setC_ref(ja.getString("c_ref"));
                code.setC_star(Integer.parseInt(ja.getString("c_star")));
                code.setF_name(ja.getString("f_name"));
                code.setF_num(Integer.parseInt(ja.getString("f_num")));

                String s1 = ja.getString("tag");
                String replace = s1.replace("[","");
                String replace1 = replace.replace("]","");
                ArrayList<String> myList = new ArrayList<String>(Arrays.asList(replace1.split(",")));
                code.setTag(myList);

            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return code;
    }
    private ArrayList<String> getList(){
        ArrayList<String> items = new ArrayList<String>();

        try {
            URL url = new URL("http://"+hostIP+"/cc/codegalleryMobile");
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
                    items.add(jo.getString("c_title"));
                    items.add(jo.getString("c_comment"));
                    items.add(jo.getString("c_date"));
                    items.add(jo.getString("c_num"));
//                    items.add(jo.getString("c_code"));
                }
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("##############"+items);
        return items;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //ListView
        listItemView = (ListView)findViewById(R.id.listView1);

        //Circle Float Action Button
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
        ImageView itemIconShare = new ImageView(this);
        itemIconShare.setImageResource(R.drawable.fba_sharebtn);
        SubActionButton shareBtn = itemBuilder.setContentView(itemIconShare).build();
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "연결버튼 눌렀다", Toast.LENGTH_SHORT).show();
                new Thread(){
                    public void run(){
                        ArrayList<String> list = getList();
                        Message msg = handler.obtainMessage();
                        msg.what = 1;
                        msg.obj = list;
                        handler.sendMessage(msg);
                    }
                }.start();
            }
        });

        ImageView itemIconEdit = new ImageView(this);
        itemIconEdit.setImageResource(R.drawable.fba_editbtn);
        SubActionButton editBtn = itemBuilder.setContentView(itemIconEdit).build();

        ImageView itemIconDelete = new ImageView(this);
        itemIconDelete.setImageResource(R.drawable.fba_deletebtn);
        SubActionButton deleteBtn = itemBuilder.setContentView(itemIconDelete).build();

        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(shareBtn)
                .addSubActionView(editBtn)
                .addSubActionView(deleteBtn)
                .attachTo(fab)
                .build();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_add) {
            // Handle the camera action
        } else if (id == R.id.nav_calendar) {

        } else if (id == R.id.nav_folder1) {

        } else if (id == R.id.nav_folder2) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 1){
                ArrayList<String> list = (ArrayList<String>)msg.obj;

                ArrayList<CodeData> newList = new ArrayList<>();

                for(int i=0; i<list.size();i+=4){
                    CodeData data = new CodeData();
                    data.codeTitle = list.get(i);
                    data.codeComment = list.get(i+1);
                    data.codeDate = list.get(i+2);
                    data.codeNum = list.get(i+3);
                    data.onClickListener = MainActivity.this;
                    newList.add(data);
                }
                adapter =  new ListAdapter(newList);
                //목록갱신하기
//                adapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_2, android.R.id.text1, list);
                listItemView.setAdapter(adapter);
            }
            if(msg.what == 2){
                //그다음으로 넘어가기
                System.out.println("##메시지 2번 받음");
                System.out.println((Code)msg.obj);

                Intent intent = new Intent(MainActivity.this, CodedetailActivity.class);
//                intent.putExtra("codeDetail", (Code)msg.obj);
                startActivity(intent);
            }
        }
    };

    //해당버튼 눌렀을때 code_num에 맞춰서 넘어가기
    @Override
    public void onClick(View v){
        View parentView = (View)v.getParent();
        TextView codeNum= parentView.findViewById(R.id.codeNum);
        String position = (String)parentView.getTag();

        Toast.makeText(MainActivity.this, codeNum.getText().toString(), Toast.LENGTH_SHORT).show();

        final String c_num = codeNum.getText().toString();
        new Thread(){
            public void run(){
                Code code = getDetailCode(c_num);
                Message msg = handler.obtainMessage();
                msg.what = 2;
                msg.obj = code;
                handler.sendMessage(msg);
            }
        }.start();
    }
}
