package com.mapure.amap.contest.ui.fagment;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.mapure.amap.contest.R;
import com.mapure.amap.contest.ui.MainActivity;
import com.mapure.amap.contest.ui.ViewWebActivity;
import com.mapure.amap.contest.util.NetworkUtils;
import com.squareup.picasso.Picasso;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;

import java.io.*;

public class ImageNewsListFragment extends Fragment {

    private boolean isCached = false;

    String[] ids = {"tm_2_c", "tm_2_1", "tm_2_2", "tm_2_3", "tm_2_4"};
    String[] urls = new String[ids.length];
    String[] titles = new String[ids.length];
    String[] imageUrls = new String[ids.length];

    private ListView listView;
    private PullToRefreshAttacher pullToRefreshAttacher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pullToRefreshAttacher = ((MainActivity) getActivity()).getPullToRefreshAttacher();

        try {
            File dir = getActivity().getFilesDir();
            File urlFile = new File(dir, "urls");
            FileInputStream fis;
            BufferedReader reader;
            String line;

            if (urlFile.exists()) {

                isCached = true;

                fis = getActivity().openFileInput("urls");
                reader = new BufferedReader(
                        new InputStreamReader(fis, "UTF-8"));

                int index = 0;
                while ((line = reader.readLine()) != null) {
                    urls[index] = line;
                    index++;
                }

                fis = getActivity().openFileInput("titles");
                reader = new BufferedReader(
                        new InputStreamReader(fis, "UTF-8"));

                index = 0;
                while ((line = reader.readLine()) != null) {
                    titles[index] = line;
                    index++;
                }

                fis = getActivity().openFileInput("imageUrls");
                reader = new BufferedReader(
                        new InputStreamReader(fis, "UTF-8"));

                index = 0;
                while ((line = reader.readLine()) != null) {
                    imageUrls[index] = line;
                    index++;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_stories, null);

        listView = (ListView) view.findViewById(R.id.card_list_view);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = urls[position];
                Intent i = new Intent();
                i.putExtra("url", url);
                i.setClass(getActivity(), ViewWebActivity.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.slide_in,
                        R.anim.scale_out);
            }
        });

        pullToRefreshAttacher.addRefreshableView(listView, new PullToRefreshAttacher.OnRefreshListener() {
            @Override
            public void onRefreshStarted(View view) {
                if (NetworkUtils.isNetworkAvailable(getActivity())) {
                    new DownloadTask().execute();
                } else {
                    pullToRefreshAttacher.setRefreshComplete();
                    Crouton.makeText(getActivity(),
                            getString(R.string.check_network),
                            Style.ALERT).show();
                }
            }
        });

        if (isCached) {
            listView.setAdapter(new MyAdapter(getActivity()));
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!isCached) {
            Crouton.makeText(getActivity(), "这里空空如也，试着下拉刷新吧！", Style.INFO).show();
        }
    }

    private void infoToFile() {
        File dir = getActivity().getFilesDir();

        File urlsFile = new File(dir, "urls");
        File titlesFile = new File(dir, "titles");
        File imageUrlsFile = new File(dir, "imageUrls");
        FileOutputStream fout;

        try {
            if (urlsFile.exists()) {
                if (urlsFile.delete()) {
                    fout = getActivity().openFileOutput("urls",
                            Context.MODE_PRIVATE);

                    for (String s : urls) {
                        fout.write(s.getBytes());
                        fout.write("\n".getBytes());
                    }

                    fout.flush();
                    fout.close();
                }

                if (titlesFile.delete()) {
                    fout = getActivity().openFileOutput("titles",
                            Context.MODE_PRIVATE);

                    for (String s : titles) {
                        fout.write(s.getBytes());
                        fout.write("\n".getBytes());
                    }

                    fout.flush();
                    fout.close();
                }

                if (imageUrlsFile.delete()) {
                    fout = getActivity().openFileOutput("imageUrls",
                            Context.MODE_PRIVATE);

                    for (String s : imageUrls) {
                        fout.write(s.getBytes());
                        fout.write("\n".getBytes());
                    }

                    fout.flush();
                    fout.close();
                }
            } else {
                fout = getActivity().openFileOutput("urls",
                        Context.MODE_PRIVATE);

                for (String s : urls) {
                    fout.write(s.getBytes());
                    fout.write("\n".getBytes());
                }

                fout.flush();

                fout = getActivity().openFileOutput("titles",
                        Context.MODE_PRIVATE);

                for (String s : titles) {
                    fout.write(s.getBytes());
                    fout.write("\n".getBytes());
                }

                fout.flush();

                fout = getActivity().openFileOutput("imageUrls",
                        Context.MODE_PRIVATE);

                for (String s : imageUrls) {
                    fout.write(s.getBytes());
                    fout.write("\n".getBytes());
                }

                fout.flush();
                fout.close();
            }



        } catch (IOException ignored) {

        }

        isCached = true;
    }

    public final class ViewHolder {
        public TextView article_title;
        public ImageView card_pic;
    }

    public class MyAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        public MyAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return ids.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            // 若沒有調用findViewById獲取控件，則調用
            // 否則直接使用控件
            if (convertView == null) {

                holder = new ViewHolder();

                convertView = mInflater.inflate(R.layout.main_page_card,
                        null);
                holder.article_title = (TextView) convertView
                        .findViewById(R.id.top_stories_article_title);
                holder.card_pic = (ImageView) convertView
                        .findViewById(R.id.top_stories_card_pic);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Picasso.with(getActivity()).load(imageUrls[position]).into(holder.card_pic);
            holder.article_title.setText(titles[position]);

            return convertView;
        }

    }

    class DownloadTask extends AsyncTask<Void, Void, Void> {

        private boolean isRefreshSuccess = false;

        @Override
        protected Void doInBackground(Void... params) {

            try {
                Document doc = Jsoup
                        .connect("http://wdxq.cnhubei.com/html/huodong/")
                        .timeout(10000).get();

                for (int i = 0; i < ids.length; i++) {
                    Elements elements = doc.select("div#" + ids[i]).select("a");
                    urls[i] = elements.first().attr("href");
                    titles[i] = elements.get(1).text();
                    imageUrls[i] = elements.select("img").attr("src");
                }

                isRefreshSuccess = true;

            } catch (IOException ignored) {

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (isRefreshSuccess) {
                listView.setAdapter(new MyAdapter(getActivity()));
                pullToRefreshAttacher.setRefreshComplete();
                infoToFile();
            }
        }
    }


}
