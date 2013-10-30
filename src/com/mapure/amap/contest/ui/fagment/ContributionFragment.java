package com.mapure.amap.contest.ui.fagment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import com.mapure.amap.contest.R;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ContributionFragment extends ListFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_list, null);

        new GetContributionTask().execute();

        return view;
    }

    private class GetContributionTask extends AsyncTask<Void, Void, Void> {
        private String[] names = new String[30];
        private String[] moneys = new String[30];
        private String[] years = new String[30];

        List<String> list;

        @Override
        protected Void doInBackground(Void... params) {

            try {
                Document doc = Jsoup
                        .connect("http://wdxq.cnhubei.com/html/xiaoyou/")
                        .timeout(10000).get();

                list = new ArrayList<>();

                Elements elements = doc.getElementsByClass("part4");
                Elements personAndMoney = elements.select("tr");

                int index = 0;
                for ( ; index < personAndMoney.size(); index++) {

                    Element e = personAndMoney.get(index);

                    int i = 0;

                    for (Element element : e.select("td")) {
                        i++;

                        if (i == 1) {
                            names[index] = element.text();
                        } else if (i == 2) {
                            moneys[index] = element.text();
                        } else {
                            years[index] = element.text();
                        }
                    }

                    try {
                        int amount = Integer.parseInt(moneys[index]);
                        list.add(names[index] + " 于 " + years[index] + " 向母校捐赠 " + amount + "元人民币");
                    } catch (NumberFormatException e1) {
                        list.add(names[index] + " 于 " + years[index] + " 向母校捐赠 " + moneys[index]);
                    }

                    index++;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            setListAdapter(new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_expandable_list_item_1, list));

            super.onPostExecute(aVoid);
        }
    }
}
