package com.example.dyw.testthread;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView mListView;
    private static String URL = "http://www.imooc.com/api/teacher?type=4&num=30";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.lv_main);
        new NewsAsyncTask().execute(URL);

        int x = 4;
        System.out.println("values is :"+((x>4)?99 : 9));

    }
    /**
     * 将url对应的json数据格式转化为所封装的NewsBean
     * */

    private List<NewsBean> getJsonDate(String url) {

        List<NewsBean> newsBeanList = new ArrayList<>();
        try {
            String jsonString = readStream(new URL(url).openStream());
            JSONObject jsonObject;

            jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0;i<jsonArray.length();i++)
            {
                NewsBean newsBean = new NewsBean();

                jsonObject = jsonArray.getJSONObject(i);
                newsBean.newsIconUrl = jsonObject.getString("picSmall");

                newsBean.newsTitle = jsonObject.getString("name");
                newsBean.newsContent = jsonObject.getString("description");
                newsBeanList.add(newsBean);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newsBeanList;
    }

    /**
     * 通过InputStream解析网页返回的数据
     * */

    private String readStream(InputStream is){
        InputStreamReader isr;
        String result="";

        try {
            String line = "";
            isr = new InputStreamReader(is,"utf-8");
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine())!=null)
            {
                result += line;
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 实现网络的异步访问
     * AsyncTask<Params,Progress,Result>
     * Params:启动任务时，输入参数的类型
     * Progress:后台任务执行中返回进度值的类型
     * Result:后台执行任务完成后返回结果的类型
     * */

    class NewsAsyncTask extends AsyncTask<String,Void,List<NewsBean>> {

        @Override
        protected List<NewsBean> doInBackground(String... params) {
            return getJsonDate(params[0]);
        }

        @Override
        protected void onPostExecute(List<NewsBean> newsBeen) {
            super.onPostExecute(newsBeen);
            NewsAdapter adapter = new NewsAdapter(MainActivity.this,newsBeen,mListView);
            mListView.setAdapter(adapter);
        }
    }

}
