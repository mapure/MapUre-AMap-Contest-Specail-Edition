package com.mapure.amap.contest.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 
 * @author Izzy
 *
 * 網絡工具類，所用方法均爲公開的靜態方法
 */

public class NetworkUtils {
	
	//空構造器，防止惡意生成對象
	private NetworkUtils() {
		
	}

	/**
	 * 
	 * 檢測是否聯接至網絡
	 * 
	 * @param context
	 * @return 是否聯接至網絡
	 */
	public static boolean isNetworkAvailable(Context context) {

		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connManager != null) {
			NetworkInfo[] info = connManager.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}

		}
		return false;
	}

	/**
	 * 檢查網絡類型，返回值爲網絡類型
	 * 若僞聯接網絡，則返回0
	 * 
	 * @param context
	 * @return NetworkType
	 */
	public static int getNetworkType(Context context) {

		if (isNetworkAvailable(context)) {
			 ConnectivityManager cm = (ConnectivityManager)
		                context.getSystemService(Context.CONNECTIVITY_SERVICE);
		        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		        return networkInfo.getType();
		}
		return 0;
	}
	
	/**
	 * 
	 * 檢測網絡是否爲無線網
	 * 
	 * @param context
	 * @return 是否爲Wi-Fi
	 */
	public static boolean isWifi(Context context) {

		if (isNetworkAvailable(context)) {
			ConnectivityManager cm = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = cm.getActiveNetworkInfo();
			if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * 判斷網絡是否爲移動數據
	 * 
	 * @param context
	 * @return 是否爲移動數據聯接
	 */
	public static boolean isMobileData(Context context) {
		
		if (isNetworkAvailable(context))  {
			ConnectivityManager cm = (ConnectivityManager)
	                context.getSystemService(Context.CONNECTIVITY_SERVICE);
	        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
	        if (networkInfo.getType() != ConnectivityManager.TYPE_WIFI) {
                return true;
            }
		}
        return false;
    }
}
