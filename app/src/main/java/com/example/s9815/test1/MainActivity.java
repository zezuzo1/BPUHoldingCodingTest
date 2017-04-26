package com.example.s9815.test1;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.s9815.test1.model.GlobalFunction;
import com.example.s9815.test1.recycler.RepoAdapter;
import com.example.s9815.test1.recycler.RepoItem;
import com.loopj.android.http.*;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private List<RepoItem> feedItemListBG = new ArrayList<RepoItem>();
    private List<RepoItem> feedItemList = new ArrayList<RepoItem>();
    private RecyclerView mRecyclerView = null;
    private RepoAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GlobalFunction.sharedInstance.initImageLoader(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_list);
        LinearLayoutManager layout = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layout);

        HttpGetRepoProfile("JakeWharton");
    }

    public void HttpGetRepoProfile(final String gitId)
    {
        Map<String,String> map = new HashMap<>();
        String url = "https://api.github.com/users/" + gitId;

        try {
            GlobalFunction.asyncCommonRequest(url,map,new JsonHttpResponseHandler(){
                    @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            DebugMsg("listClipNew success. " + response.toString() );

                            try {
                                GlobalFunction.sharedInstance.profile = response;
                            } catch( Exception err ) {
                                DebugMsg( "jsonobject parsr error : " + err.toString());
                            }

                            HttpGetRepository(gitId);
                        }
                    });
        } catch (Exception e) {
        }
    }

    public void HttpGetRepository(String gitId)
    {
        Map<String,String> map = new HashMap<>();
        String url = "https://api.github.com/users/" + gitId + "/repos";

        try {
            GlobalFunction.asyncCommonRequest(url,map,new JsonHttpResponseHandler(){
                    @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            DebugMsg("listClipNew success. " + response.toString() );

                            try {
                                response = GlobalFunction.sortRepoJsonarray(response);
                                feedItemListBG.clear();
                                for( int i=0; i<response.length(); i++ )
                                {
                                    JSONObject ele = response.getJSONObject(i);
                                    RepoItem item = new RepoItem(ele);
                                    feedItemListBG.add(item);
                                }
                            } catch( Exception err ) {
                                DebugMsg( "jsonobject parsr error : " + err.toString());
                            }

                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        UpdateUI();
                                    } catch (Exception err)
                                    {
                                        DebugMsg(err.toString());
                                    }
                                }
                            });
                        }
                    });
        } catch (Exception e) {
        }
    }

    private void UpdateUI()
    {
        try {
            JSONObject profile = GlobalFunction.sharedInstance.profile;
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            Menu menu = navigationView.getMenu();

            if(adapter == null ) {
                adapter = new RepoAdapter(MainActivity.this, feedItemList);
                mRecyclerView.setAdapter(adapter);
            }

            feedItemList.clear();
            feedItemList.addAll(feedItemListBG);
            adapter.notifyDataSetChanged();

            setTitle(GlobalFunction.sharedInstance.profile.get("name") + "'s Repositories");

            ((TextView)findViewById(R.id.title_name)).setText((String)profile.get("name"));
            ((TextView)findViewById(R.id.title_email)).setText((String)profile.get("email"));

            menu.findItem(R.id.nav_company).setTitle("Company : " + profile.get("company"));
            menu.findItem(R.id.nav_location).setTitle("Location : " + profile.get("location"));
            menu.findItem(R.id.nav_blog).setTitle("Blog : " + profile.get("blog"));
            menu.findItem(R.id.nav_followers).setTitle("Followers : " + profile.get("followers").toString());
            menu.findItem(R.id.nav_following).setTitle("Following : " + profile.get("following").toString());
            menu.findItem(R.id.nav_repositories).setTitle("Repositories : " + profile.get("public_repos").toString());

            GlobalFunction.sharedInstance.asyncLoadImage(
                    (String)profile.get("avatar_url"),
                    (ImageView)findViewById(R.id.imageView));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void DebugMsg(String msg)
    {
        if (BuildConfig.DEBUG)
            Log.d("oz", msg);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_company) {
        } else if (id == R.id.nav_location) {
        } else if (id == R.id.nav_blog) {
        } else if (id == R.id.nav_followers) {
        } else if (id == R.id.nav_following) {
        } else if (id == R.id.nav_repositories) {
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
