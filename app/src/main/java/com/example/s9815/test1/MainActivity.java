package com.example.s9815.test1;

import android.app.Service;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

    private void UpdateUI()
    {
        try {

            if(adapter == null ) {
                adapter = new RepoAdapter(MainActivity.this, feedItemList);
                mRecyclerView.setAdapter(adapter);
            }

            feedItemList.clear();
            feedItemList.addAll(feedItemListBG);
            adapter.notifyDataSetChanged();

            JSONObject profile = GlobalFunction.sharedInstance.profile;

            setTitle(GlobalFunction.sharedInstance.profile.get("name") + "'s Repositories");

            ((TextView)findViewById(R.id.title_name)).setText((String)profile.get("name"));
            ((TextView)findViewById(R.id.title_email)).setText((String)profile.get("email"));

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            Menu menu = navigationView.getMenu();
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

    public void HttpGetRepoProfile(final String gitId)
    {
        try {
            Map<String,String> map = new HashMap<>();

            String url = "https://api.github.com/users/" + gitId;

            GlobalFunction.asyncCommonRequest(url,map,new JsonHttpResponseHandler(){

                    @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            oz_msg("listClipNew success. " + response.toString() );

                            try {
                                GlobalFunction.sharedInstance.profile = response;
                            } catch( Exception err ) {
                                oz_msg( "jsonobject parsr error : " + err.toString());
                            }

                            HttpGetRepository(gitId);
                        }
                    }
                    );
        } catch (Exception e) {
        } finally {
        }
    }

    public void HttpGetRepository(String gitId)
    {
        try {
            Map<String,String> map = new HashMap<>();

            String url = "https://api.github.com/users/" + gitId + "/repos";

            GlobalFunction.asyncCommonRequest(url,map,new JsonHttpResponseHandler(){

                    @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            oz_msg("listClipNew success. " + response.toString() );

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
                                oz_msg( "jsonobject parsr error : " + err.toString());
                            }

                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        UpdateUI();
                                    } catch (Exception err)
                                    {
                                        oz_msg(err.toString());
                                    }
                                }
                            });
                        }
                    }
                    );

        } catch (Exception e) {
        } finally {
        }
    }

    public void oz_msg(String parm)
    {
        Log.d("oz", parm);
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
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_company) {
            // Handle the camera action
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
