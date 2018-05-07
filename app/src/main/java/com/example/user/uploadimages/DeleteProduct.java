package com.example.user.uploadimages;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class DeleteProduct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_product);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
        new AsyncTaskRunner().execute((Void[])null);
    }




    public boolean Connetion()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return true;
        }
        else
            return false;
    }

    class AsyncTaskRunner extends AsyncTask<Void,Void,Void> {


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }



        @Override
        protected Void doInBackground(Void... params) {




                  boolean  connected=Connetion();
                    if (connected==true) {
                        Vegitable vegitable=new Vegitable();

                            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                        String id=String.valueOf(vegitable.getid());
                            nameValuePairs.add(new BasicNameValuePair("id",id));

                            //  nameValuePairs.add(new BasicNameValuePair("first_name",firstName));

                            HttpClient httpClient = new DefaultHttpClient();
                            HttpPost httpPost = new HttpPost("http://sabkuch.co.in/sabkuckapp/deleteItem.php?id="+"67");
                            try {
                                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                                HttpResponse response = httpClient.execute(httpPost);
                                HttpEntity entity = response.getEntity();
                              InputStream inpst = entity.getContent();
                                BufferedReader br = new BufferedReader(new InputStreamReader(inpst));
                                StringBuilder sb = new StringBuilder();
                                String line,result;
                                while ((line = br.readLine()) != null) {
                                    sb.append(line + "\n");
                                }
                                inpst.close();
                                result = sb.toString();

                            //    JSONObject responseJSON = new JSONObject(result);
                              //  String status = responseJSON.getString("status");
                                //  String userName = responseJSON.getString("email");


                                // String msg = jsonObject.getString("status_messsage");
                                //   if (status.equals("success")) {
                                Intent intent = new Intent(getApplicationContext(), AllOrderListNavigationDrawerActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                                // }

                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            } catch (ClientProtocolException e) {
                                e.printStackTrace();
                            } catch (IOException e) {

                                e.printStackTrace();
                            }
                        }

            return null;
        }
    }





}
