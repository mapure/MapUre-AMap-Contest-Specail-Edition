package com.mapure.amap.contest.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.*;
import android.widget.ImageView;
import com.mapure.amap.contest.R;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.util.Utility;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
/**
 * @author Izzy
 */

//微博Web授權Activity
public class WeiboAuthActivity extends FragmentActivity implements
		WeiboAuthListener {
	private WebView mWebView;

	private MenuItem refreshItem;

	private WeiboAuthListener mListener;

	@SuppressWarnings("deprecation")
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mListener = this;

		setContentView(R.layout.activity_weibo_auth);
		
		mWebView = (WebView) findViewById(R.id.web_view);

		WebSettings settings = mWebView.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setSaveFormData(false);
		settings.setSavePassword(false);
		settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		settings.setRenderPriority(WebSettings.RenderPriority.HIGH);

		CookieSyncManager.createInstance(this);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeAllCookie();

		mWebView.setVerticalScrollBarEnabled(false);
		mWebView.setHorizontalScrollBarEnabled(false);
		mWebView.setWebViewClient(new WeiboWebViewClient());
		mWebView.loadUrl(getWeiboOAuthUrl());
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
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.weibo_auth, menu);
		refreshItem = menu.findItem(R.id.action_refresh_page);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_refresh_page:
			refresh();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@SuppressWarnings("deprecation")
	public void refresh() {
		mWebView.clearView();
		mWebView.loadUrl("about:blank");
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ImageView iv = (ImageView) inflater
				.inflate(R.layout.refresh_view, null);

		Animation rotation = AnimationUtils.loadAnimation(this, R.anim.refresh);
		iv.startAnimation(rotation);

		refreshItem.setActionView(iv);
		mWebView.loadUrl(getWeiboOAuthUrl());
	}

	private void completeRefresh() {
		if (refreshItem.getActionView() != null) {
			refreshItem.getActionView().clearAnimation();
			refreshItem.setActionView(null);
		}
	}

	private class WeiboWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			if (url.startsWith("https://api.weibo.com/oauth2/default.html")) {
				handleRedirectUrl(view, url);
				view.stopLoading();

				return;
			}
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);

			mListener.onError(new WeiboDialogError(description, errorCode,
					failingUrl));
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			if (!url.equals("about:blank"))
				completeRefresh();
		}

	}

	private void handleRedirectUrl(WebView view, String url) {
		Bundle values = Utility.parseUrl(url);

		String error = values.getString("error");
		String error_code = values.getString("error_code");

		if (error == null && error_code == null) {
			mListener.onComplete(values);
		} else if (error.equals("access_denied")) {
			mListener.onCancel();
		} else {
			mListener.onWeiboException(new WeiboException(error, Integer
					.parseInt(error_code)));
		}
	}

	@Override
	public void onCancel() {

	}

	@Override
	public void onComplete(Bundle values) {
		String token = values.getString("access_token");
		String expire = values.getString("expires_in");
		String uid = values.getString("uid");

		SharedPreferences preferences = getSharedPreferences(
				"com.mapure.amap.contest", Context.MODE_APPEND);
		Editor editor = preferences.edit();
		editor.putString("weibo_token", token);
		editor.putString("weibo_expires", expire);
		editor.putString("uid", uid);
		editor.commit();

		SNSEditActivity.accessToken = new Oauth2AccessToken(token, expire);

		setResult(RESULT_OK);
		finish();
	}

	@Override
	public void onError(WeiboDialogError e) {

	}

	@Override
	public void onWeiboException(WeiboException arg0) {

	}

	private String getWeiboOAuthUrl() {

		Map<String, String> parameters = new HashMap<>();
		parameters.put("client_id", SNSEditActivity.WB_API_KEY);
		parameters.put("response_type", "token");
		parameters.put("redirect_uri",
				"https://api.weibo.com/oauth2/default.html");
		parameters.put("display", "mobile");
		return "https://api.weibo.com/oauth2/authorize" + "?"
				+ encodeUrl(parameters)
				+ "&scope=friendships_groups_read,friendships_groups_write";
	}

	public String encodeUrl(Map<String, String> param) {
		if (param == null) {
			return "";
		}

		StringBuilder sb = new StringBuilder();

		Set<String> keys = param.keySet();
		boolean first = true;

		for (String key : keys) {
			String value = param.get(key);
			if (!TextUtils.isEmpty(value) || key.equals("description")
					|| key.equals("url")) {
				if (first)
					first = false;
				else
					sb.append("&");
				try {
					sb.append(URLEncoder.encode(key, "UTF-8")).append("=")
							.append(URLEncoder.encode(param.get(key), "UTF-8"));
				} catch (UnsupportedEncodingException ignored) {

				}
			}

		}

		return sb.toString();
	}
}
