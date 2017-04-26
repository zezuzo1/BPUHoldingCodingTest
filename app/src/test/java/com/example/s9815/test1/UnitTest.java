package com.example.s9815.test1;

import com.example.s9815.test1.model.GlobalFunction;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import org.apache.http.Header;

public class UnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void httpGetTest() throws  Exception {
        Map<String,String> map = new HashMap<>();
        String url = "https://api.github.com/users/JakeWharton/repos";

        try {
            GlobalFunction.asyncCommonRequest(url,map,new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    assertFalse("success", true);
                }
            });
        } catch (Exception e) {
            assertFalse(e.toString(),false);
        }
    }
}