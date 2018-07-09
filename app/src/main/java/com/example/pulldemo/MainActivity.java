package com.example.pulldemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.io.InputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 1.显示天气信息
        TextView tv_weather = (TextView) findViewById(R.id.tv_weather);

        try ( // 1.1获取资产的管理者，通过上下文
              InputStream inputStream = getAssets().open("weather.xml");) {
            // 2.调用我们定义的解析xml业务方法
            List<Channel> list = Weather.parserXml(inputStream);
            StringBuffer sb = new StringBuffer();
            for (Channel channel : list) {
                sb.append(channel.toString() + "\n");
            }
            // 3.把数据展示到textview
            tv_weather.setText(sb);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
