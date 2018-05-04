package com.example.user.uploadimages;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class AllOrderListNavigationDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    EditText edPswd;
    TextView txtDate, txtForget;
    Button btnSignIn, btnGet;
    Spinner spinner;
    static String Pswd, spinner1string, line, result;
    InputStream inpts = null,inpts1;
    boolean connected = false;
    String Flat,Date,Des,e;
    String address="http://sabkuch.co.in/sabkuckapp/User_detail.php";
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    private ArrayList<GetData> myadapter = new ArrayList<GetData>();
    ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_order_list_navigation_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        progressDialog=new ProgressDialog(this);
        //    progressDialog.setMessage("Getting Offers !");
        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("loading events !");
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
        progressDialog.show();
        new AsyncRunnerClass().execute((Void)null);



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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.all_order_list_navigation_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_insert) {
            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            // Handle the camera action
        } else if (id == R.id.nav_todays_orders) {
            Intent intent=new Intent(getApplicationContext(),OrderList1.class);
            startActivity(intent);

        } else if (id == R.id.nav_vegitable) {
            Intent intent=new Intent(getApplicationContext(),VegitableShop.class);
            startActivity(intent);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_logout) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public boolean Connetion() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return true;
        } else
            return false;
    }

    class AsyncRunnerClass extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            adapter=new OrderListAdapter(getApplicationContext(),myadapter);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }


        @Override
        protected Void doInBackground(Void... params) {


            connected=Connetion();
            if(connected==true) {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(address);


                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = httpClient.execute(httpPost);
                    HttpEntity entity = response.getEntity();
                    inpts = entity.getContent();

                    BufferedReader br = new BufferedReader(new InputStreamReader(inpts));
                    StringBuilder sb = new StringBuilder();





                    try {
                        while ((line = br.readLine()) != null) {
                            sb.append(line + "\n");
                            inpts.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }



                    result = sb.toString();

                    JSONArray jsonArray = new JSONArray(result);
                    JSONObject jsonObject = null;





                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        GetData g=new GetData();

                        String  userphone= jsonObject.getString("userphone");
                        String  username= jsonObject.getString("username");
                        String  date= jsonObject.getString("date");



                        g.setPhone(userphone);
                        g.setName(username);
                        g.setDate(date);

                        myadapter.add(g);

                        // e = jsonObject.getString("status");
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                } catch (ClientProtocolException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }


            }
            return null;
        }
    }



}
