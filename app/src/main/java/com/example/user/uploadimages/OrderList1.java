package com.example.user.uploadimages;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class OrderList1 extends AllOrderListNavigationDrawerActivity {
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
        setContentView(R.layout.activity_order_list1);
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

