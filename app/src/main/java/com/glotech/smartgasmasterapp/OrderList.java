package com.glotech.smartgasmasterapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.glotech.smartgasmasterapp.ui.login.LoginActivity;
import glotech.smartgasmasterapp.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import glotech.smartgasmasterapp.R;

public class OrderList extends AppCompatActivity {

    private Button unfinished,finished,backButton;
    private ListView orderlist;
    public int Worker_Id;
    InputStream is = null;
    String line,result = "";
    String[] dataId,order_Id;
    ArrayList<OrderListItem> data;
    public static String static_order_id;
    //為了記錄這筆訂單有哪些sensor_Id 來更新資料庫中iot的原桶重Gas_Original_Weight
    public static ArrayList<String> sensor_Id_Array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        //Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        sensor_Id_Array = new ArrayList<>();
        sensor_Id_Array.clear();

        data = new ArrayList<OrderListItem>();

        LoginActivity loginActivity = new LoginActivity();
        Worker_Id = loginActivity.getWorkerID();
        unfinished = findViewById(R.id.order_unfinished);
        orderlist = (ListView)findViewById(R.id.list_item);
        finished = findViewById(R.id.order_finished);
        backButton = findViewById(R.id.backButton);

        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));
        getData("http://54.199.33.241/test/Worker_UnOrderList.php");
        try {
            getOrderList("http://54.199.33.241/test/Worker_UnOrderList.php");
        } catch (Exception e) {
            Log.i("OrderList cre Exception",e.toString());
        }
        setAdapter();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        unfinished.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unfinished.setBackgroundColor(Color.GRAY);
                unfinished.setTextColor(Color.WHITE);
                finished.setBackgroundColor(Color.WHITE);
                finished.setTextColor(Color.GRAY);
                orderlist.setAdapter(null);
                //直接在這裡改orderList
                getData("http://54.199.33.241/test/Worker_UnOrderList.php");
                try {
                    getOrderList("http://54.199.33.241/test/Worker_UnOrderList.php");
                } catch (Exception e) {
                    Log.i("UnOrderList cre Exception",e.toString());
                }
                setAdapter();
            }
        });


        finished.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finished.setBackgroundColor(Color.GRAY);
                finished.setTextColor(Color.WHITE);
                unfinished.setBackgroundColor(Color.WHITE);
                unfinished.setTextColor(Color.GRAY);
                orderlist.setAdapter(null);
                //直接在這裡改orderList
                getData("http://54.199.33.241/test/Worker_OrderList.php");
                try {
                    getOrderList("http://54.199.33.241/test/Worker_OrderList.php");
                } catch (Exception e) {
                    Log.i("UnOrderList create Exception",e.toString());
                }
                setAdapter();
            }
        });

    }

    private void setAdapter() {
        if(data.size()>0){
            OrderListAdapter adapter=
                    new OrderListAdapter(this,R.layout.adapter_order_list,data);
            orderlist.setAdapter(adapter);
            orderlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //當備案下時
                    String msg= dataId[position];
                    Toast.makeText(OrderList.this,msg,Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(OrderList.this, OrderInfo.class);
                    String Id = order_Id[position];
                    static_order_id = Id;
                    startActivity(intent);
                }
            });
        }
        else{
            Toast.makeText(this, "無訂單", Toast.LENGTH_SHORT).show();
        }

    }
    private void getOrderList(String Showurl) throws MalformedURLException {
        try {
            URL url = new URL(Showurl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(Worker_Id), "UTF-8");
            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            String result = "";
            String line = "";

            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            Log.i("result here", "[" + result + "]");
            try {
                JSONArray ja = new JSONArray(result);
                JSONObject jo = null;

                order_Id = new String[ja.length()];
                // Remove the local declaration of data here
                data = new ArrayList<>();

                for (int i = 0; i < ja.length(); i++) {
                    jo = ja.getJSONObject(i);
                    if (new String(jo.getString("DELIVERY_Address").getBytes("ISO-8859-1"), "UTF-8") != null && jo.getString("Expect_time") != null) {
                        data.add(new OrderListItem(new String(jo.getString("DELIVERY_Address").getBytes("ISO-8859-1"), "UTF-8"), jo.getString("Expect_time")));
                    }
                    // jo.getString("Expect_time")
                    Log.i("order data", data.toString());
                    order_Id[i] = jo.getString("ORDER_Id");
                }
            } catch (Exception e) {
                Log.i("newOrderList JSON Exception ", e.toString());
            }
        } catch (Exception e) {
            Log.i("GetOrderList Exception", e.toString());
        }
    }


    private void getData(String URL_Link){
        //還要把worker Id丟過去
        try {
            URL url = new URL(URL_Link);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            is = new BufferedInputStream(con.getInputStream());
        }
        catch(Exception e){
            Log.i("OrderList Exception",e.toString());
        }
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();

            while((line = br.readLine())!=null){
                sb.append(line+"\n");
            }
            is.close();
            result = sb.toString();
            Log.i("OrderList result",result);
        }catch(Exception e){
            Log.i("OrderList Exception2",e.toString());
        }
        try{
            JSONArray ja = new JSONArray(result);
            JSONObject jo = null;

            dataId = new String[ja.length()];

            for(int i = 0; i<ja.length();i++){
                jo = ja.getJSONObject(i);
                dataId[i] = jo.getString("Assign_Id");
                Log.i("order data",dataId[i]);
            }
        }catch(Exception e){
            data = null;
            Log.i("OrderList JSON Exception",e.toString());
        }
    }

}