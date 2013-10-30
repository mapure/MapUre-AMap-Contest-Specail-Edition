package com.mapure.amap.contest.ui.fagment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import com.mapure.amap.contest.R;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WishFragment extends Fragment {

    private List<HashMap<String, String>> list;
    private GridView gridView;

    private final String url = "http://wdxq.cnhubei.com/wall_wd/zhufu_list.php";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wish, null);

        gridView = (GridView) view.findViewById(R.id.wish_grid_view);

        new GetWishTask().execute(url);

        return view;
    }

    class GetWishTask extends AsyncTask<String, Void, Void> {

        private boolean isSuccess = true;

        String[] names = new String[18];
        String[] contents = new String[18];
        String[] times = new String[18];

        @Override
        protected Void doInBackground(String... params) {

            try {
                Document doc = Jsoup
                        .connect(params[0])
                        .timeout(10000).get();

                Document doc2 = Jsoup
                        .connect(params[0] + "?start=6")
                        .timeout(10000).get();

                Document doc3 = Jsoup
                        .connect(params[0] + "?start=12")
                        .timeout(10000).get();

                Elements elements = doc.getElementsByClass("tr");
                Elements elements2 = doc2.getElementsByClass("tr");
                Elements elements3 = doc3.getElementsByClass("tr");

                for (int i = 0; i < elements.size(); i++) {
                    if (i % 3 == 0) {
                        names[i / 3] = elements.get(i).text();
                    } else if (i % 3 == 1) {
                        contents[i / 3] = elements.get(i).text();
                    } else {
                        times[i / 3] = elements.get(i).text();
                    }
                }

                for (int i = 0; i < elements2.size(); i++) {
                    if (i % 3 == 0) {
                        names[i / 3 + 6] = elements2.get(i).text();
                    } else if (i % 3 == 1) {
                        contents[i / 3 + 6] = elements2.get(i).text();
                    } else {
                        times[i / 3 + 6] = elements2.get(i).text();
                    }
                }

                for (int i = 0; i < elements3.size(); i++) {
                    if (i % 3 == 0) {
                        names[i / 3 + 12] = elements3.get(i).text();
                    } else if (i % 3 == 1) {
                        contents[i / 3 + 12] = elements3.get(i).text();
                    } else {
                        times[i / 3 + 12] = elements3.get(i).text();
                    }
                }
            } catch (IOException e) {
                isSuccess = false;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (isSuccess) {
                HashMap<String, String> map;

                list = new ArrayList<>();

                for (int i = 0; i < 18; i++) {
                    map = new HashMap<>();
                    map.put("name", names[i]);
                    map.put("content", contents[i]);
                    map.put("time", times[i]);

                    list.add(map);
                }

                if (gridView.getAdapter() == null) {
                    SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(),
                            list, R.layout.wish_grid_item,
                            new String[] {"name", "content", "time"},
                            new int[] {R.id.sender_name, R.id.wish_content, R.id.wish_time});

                    gridView.setAdapter(simpleAdapter);
                } else {
                    ((SimpleAdapter) gridView.getAdapter()).notifyDataSetChanged();
                }

            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.network_error),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
