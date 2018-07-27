package com.example.jimison.opendataanalysis;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    OkHttpClient client;
    TextView resultInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        client = new OkHttpClient();
        resultInfo = findViewById(R.id.result);
    }

    //手動解析json
    public void manualAnalysis(View view) {
        Request request = new Request.Builder().url("http://tbike.tainan.gov.tw:8081/Service/StationStatus/Json").build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                final String result = response.body().string();
                final StringBuffer sb = new StringBuffer();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONArray data = new JSONArray(result);
                            for (int i = 0; i < data.length(); i++) {
                                String station = data.getJSONObject(i).getString("StationName");
                                String bikeCount = data.getJSONObject(i).getString("AvaliableBikeCount");
                                sb.append("站名：" + station + "\n空位：" + bikeCount + "\n");
                                sb.append("-------------------\n");
                            }
                            resultInfo.setText(sb.toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    //
    public void GsonAnalysis(View view) {
        Request request = new Request.Builder().url("http://tbike.tainan.gov.tw:8081/Service/StationStatus/Json").build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request,final IOException e) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        resultInfo.setText(e.getMessage());
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                final String str = response.body().string();
                final List<JsonData> jsonData = new Gson().fromJson(str, new TypeToken<List<JsonData>>() {
                }.getType());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        StringBuffer sb = new StringBuffer();
                        for(JsonData s:jsonData){
                            sb.append("站名：").append(s.getStation()).append("\n");
                            sb.append("可借："+s.getBikeCount()+"\n");
                            sb.append("可停："+s.getSpaceCount()+"\n");
                        }
                        resultInfo.setText(sb.toString());
                    }
                });
            }
        });
    }

    public class JsonData {
        @SerializedName("StationName")
        private String station;
        @SerializedName("AvaliableBikeCount")
        private int bikeCount;
        @SerializedName("AvaliableSpaceCount")
        private int spaceCount;

        public String getStation() {
            return station;
        }

        public int getBikeCount() {
            return bikeCount;
        }

        public int getSpaceCount() {
            return spaceCount;
        }
    }
}
