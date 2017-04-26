package com.example.s9815.test1.model;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BuildConfig;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.s9815.test1.R;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class GlobalFunction
{
	public static GlobalFunction sharedInstance = new GlobalFunction();

    private DisplayImageOptions imageLoaderOption;
	public JSONObject profile = null;
	private boolean image_loader_config_done = false;

	public GlobalFunction()
	{
        imageLoaderOption = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .build();
	}

	public void asyncLoadImage(String url, ImageView imageview)
	{
		ImageLoader.getInstance().displayImage(url, imageview, imageLoaderOption);
	}

	public void initImageLoader(Context context) {

		if( image_loader_config_done ) return;
		image_loader_config_done = true;

		ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);

		config.denyCacheImageMultipleSizesInMemory();
		config.memoryCache(new WeakMemoryCache());
		config.diskCacheSize(104857600);

		if(BuildConfig.DEBUG)
		{
			config.writeDebugLogs();
		}

		ImageLoader.getInstance().init(config.build());
	}

    public static JSONArray sortRepoJsonarray(JSONArray jsonArr)
    {
        JSONArray sortedJsonArray = new JSONArray();

        List<JSONObject> jsonValues = new ArrayList<JSONObject>();
        for (int i = 0; i < jsonArr.length(); i++) {
            try {
                jsonValues.add(jsonArr.getJSONObject(i));
            } catch (Exception err)
            {
            }
        }

        Collections.sort( jsonValues, new Comparator<JSONObject>() {
            private static final String KEY_NAME = "stargazers_count";

            @Override
            public int compare(JSONObject a, JSONObject b) {
                int valA ;
                int valB ;

                try {
                    valA = (int) a.get(KEY_NAME);
                    valB = (int) b.get(KEY_NAME);
                } 
                catch (JSONException e) {
                    return 0;
                }

                return valB - valA;
            }
        });

        for (int i = 0; i < jsonArr.length(); i++) {
            sortedJsonArray.put(jsonValues.get(i));
        }

        return sortedJsonArray;
    }

    @SuppressWarnings("unchecked")
    public static void asyncCommonRequest(
            String url,
            Map<String, String> parameters,
            ResponseHandlerInterface response_handler)
    {
        if (BuildConfig.DEBUG)
        {
            Log.d("oz", "request = " + url);
        }

        RequestParams params = new RequestParams( parameters );
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.setUserAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36");
        httpClient.get(url, params, response_handler);
    }
}

