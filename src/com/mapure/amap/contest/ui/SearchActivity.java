package com.mapure.amap.contest.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiItemDetail;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.mapure.amap.contest.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Izzy
 */

public class SearchActivity extends Activity implements View.OnClickListener, PoiSearch.OnPoiSearchListener {

    private AutoCompleteTextView mEditText;
    private ListView mListView;

    private PoiSearch.Query query;// Poi查询条件类

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        mEditText = (AutoCompleteTextView) findViewById(R.id.search_et);
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String newText = s.toString();
                // 输入信息的回调方法
                Inputtips inputTips = new Inputtips(SearchActivity.this,
                        new Inputtips.InputtipsListener() {

                            @Override
                            public void onGetInputtips(List<Tip> tipList, int rCode) {
                                if (tipList != null) {
                                    List<String> listString = new ArrayList<>();
                                    for (Tip tip : tipList) {
                                        listString.add(tip.getName());
                                    }
                                    ArrayAdapter aAdapter = new ArrayAdapter<>(
                                            getApplicationContext(),
                                            R.layout.route_inputs, listString);
                                    mEditText.setAdapter(aAdapter);
                                    aAdapter.notifyDataSetChanged();
                                }

                            }
                        });
                try {
                    // 发送输入提示请求
                    // 第一个参数表示提示关键字，第二个参数默认代表全国，也可以为城市区号
                    inputTips.requestInputtips(newText, "027");
                } catch (AMapException e) {
                    e.printStackTrace();
                }
            }
        });
        Button mSearchButton = (Button) findViewById(R.id.search_button);
        mSearchButton.setOnClickListener(this);

        mListView = (ListView) findViewById(R.id.search_result_list);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Drawable mActionBarBackgroundDrawable = getResources().getDrawable(
                R.drawable.ab_solid_mapure);
        mActionBarBackgroundDrawable.setAlpha(255);
        getActionBar().setBackgroundDrawable(
                mActionBarBackgroundDrawable);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_button:
                String destination = mEditText.getText().toString();
                if (destination.isEmpty()) {
                    Toast.makeText(SearchActivity.this, "Please input something", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    query = new PoiSearch.Query(destination, "", "027");
                    PoiSearch poiSearch = new PoiSearch(this, query);
                    poiSearch.setOnPoiSearchListener(this);
                    poiSearch.searchPOIAsyn();
                }

                break;
        }
    }

    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
        if (rCode == 0) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    final List<PoiItem> poiItems = result.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                    List<String> data = new ArrayList<>();

                    if (poiItems != null && poiItems.size() > 0) {
                        for (PoiItem poiItem : poiItems) {
                            data.add(poiItem.getTitle());
                        }

                        mListView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1,
                                data));

                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                PoiItem poiItem = poiItems.get(position);

                                String point_id = poiItem.getPoiId();

                                LatLonPoint point = poiItem.getLatLonPoint();
                                double latitude = point.getLatitude();
                                double longitude = point.getLongitude();

                                String title = poiItem.getTitle();
                                String snippet = poiItem.getSnippet();

                                Bundle b = new Bundle();
                                b.putString("id", point_id);
                                b.putDouble("latitude", latitude);
                                b.putDouble("longitude", longitude);
                                b.putString("title", title);
                                b.putString("snippet", snippet);

                                Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                                intent.putExtras(b);

                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        });
                    } else {
                        Toast.makeText(SearchActivity.this, "No Result", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(SearchActivity.this, "No Result", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(SearchActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPoiItemDetailSearched(PoiItemDetail poiItemDetail, int i) {

    }
}
