package com.mapure.amap.contest.ui.fagment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import com.mapure.amap.contest.R;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class TimeTableFragment extends Fragment {

    private WebView mWebView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_time_table, null);

        mWebView = (WebView) view.findViewById(R.id.time_table_result);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {

                String result = null;

                try {
                    Document doc = Jsoup
                            .connect("http://wdxq.cnhubei.com/html/huodong/RC/2013/0717/1450.html")
                            .timeout(10000).get();

                    result = doc.select("table").toString();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                if (s != null) {
                    mWebView.loadDataWithBaseURL(null, s, "text/html", "utf-8", null);
                }
            }
        }.execute();
    }
}
