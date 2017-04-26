package com.example.s9815.test1.model;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BuildConfig;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class GlobalFunction
{
	public static GlobalFunction sharedInstance = new GlobalFunction();

    private DisplayImageOptions imageLoaderOption;
	private boolean imageLoaderConfigDone = false;
    public JSONObject profile = null;

	public GlobalFunction()
	{
        imageLoaderOption = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .build();
	}

	public void initImageLoader(Context context)
	{
		if( imageLoaderConfigDone ) return;
		imageLoaderConfigDone = true;

		ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);

		config.denyCacheImageMultipleSizesInMemory();
		config.memoryCache(new WeakMemoryCache());
		config.diskCacheSize(104857600);

		if(BuildConfig.DEBUG)
			config.writeDebugLogs();

		ImageLoader.getInstance().init(config.build());
	}

	public void asyncLoadImage(String url, ImageView imageview)
	{
		ImageLoader.getInstance().displayImage(url, imageview, imageLoaderOption);
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

    public static void asyncHttpRequest(
            String url,
            Map<String, String> parameters,
            ResponseHandlerInterface response_handler)
    {
        RequestParams params = new RequestParams( parameters );
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.setUserAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36");
        if (BuildConfig.DEBUG)
            Log.d("oz", "request = " + url);
        httpClient.get(url, params, response_handler);
    }
}

