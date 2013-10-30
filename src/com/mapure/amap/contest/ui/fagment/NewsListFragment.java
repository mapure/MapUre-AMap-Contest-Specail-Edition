package com.mapure.amap.contest.ui.fagment;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.mapure.amap.contest.R;
import com.mapure.amap.contest.ui.ViewWebActivity;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NewsListFragment extends ListFragment {

    private String url;
    private String[] titles = new String[25];
    private String[] urls = new String[25];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = getArguments();
        url = b.getString("url");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_news_list, null);

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {

                try {
                    Document doc = Jsoup
                            .connect(
                                    url)
                            .timeout(10000).get();

                    Elements elements = doc.getElementsByClass("list_ul");

                    Elements listElements = elements.select("li > a");

                    int i = 0;
                    for (Element element : listElements) {
                        urls[i] = element.attr("href");
                        titles[i] = element.text();
                        i++;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                if (titles[0] != null) {
                    List<String> list = new ArrayList<>();

                    for (String s : titles) {
                        list.add(s.trim());
                    }

                    if (NewsListFragment.this.isAdded()) {

                        setListAdapter(new ArrayAdapter<>(getActivity(),
                                R.layout.news_list_item, list));
                    }
                } else {
                    Toast.makeText(getActivity(), "Network Error!", Toast.LENGTH_SHORT).show();
                }

            }
        }.execute();

        return view;

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        String url = urls[position];
        Intent i = new Intent();
        i.putExtra("url", url);
        i.setClass(getActivity(), ViewWebActivity.class);
        startActivity(i);
        getActivity().overridePendingTransition(R.anim.slide_in,
                R.anim.scale_out);
    }
}
