package com.example.user.uploadimages;

import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class UpdateActivity extends AppCompatActivity implements View.OnClickListener {
    String   imageURL;
    public static final String UPLOAD_URL = "http://sabkuch.co.in/sabkuckapp/Update_data.php";
    public static final String UPLOAD_KEY = "Image";
    public static final String TAG = "MY MESSAGE";
    Bitmap bitmapOldImage;
    String uploadImage;

    private int PICK_IMAGE_REQUEST = 1;
String name;
    int price;
    private ImageView buttonChoose;
    private Button buttonUpload ,btnCancel;
int a;
    private ImageView imageView;
int Fruit_p;
    private Bitmap bitmap;
EditText Fruit_name,Fruit_price;
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        buttonChoose = (ImageView) findViewById(R.id.buttonChoose);
        buttonUpload = (Button) findViewById(R.id.buttonUpload);
        //  buttonView = (Button) findViewById(R.id.buttonViewImage);
        Fruit_name= (EditText) findViewById(R.id.fruit_name);
        Fruit_price= (EditText) findViewById(R.id.fruit_price);
        imageView = (ImageView) findViewById(R.id.imageView);
        btnCancel=(Button) findViewById(R.id.btnCancel);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        a = b.getInt("id");
        name  = b.getString ("name");

        price = b.getInt("productprice");
        Fruit_p=b.getInt("price");
        Toast.makeText(this, ""+Fruit_p, Toast.LENGTH_SHORT).show();
        imageURL = b.getString("image");
        Glide.with(getApplicationContext())
                .load(imageURL)
                .into(imageView);



        Fruit_name.setText(name);
        Fruit_price.setText(""+price);


        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),VegitableShop.class);
                startActivity(i);
            }
        });
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadImage(){
        class UploadImage extends AsyncTask<Bitmap,Void,String>{

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                name=Fruit_name.getText().toString();
                price= Integer.parseInt(Fruit_price.getText().toString());
                super.onPreExecute();
                loading = ProgressDialog.show(UpdateActivity.this, "Updating Data", "Please wait...",true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Intent i=new Intent(getApplicationContext(),UpdateActivity.class);
                startActivity(i);
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Bitmap... params) {

                Bitmap bitmap = params[0];
                 uploadImage = getStringImage(bitmap);

                HashMap<String,String> data = new HashMap<>();
                Intent intent = getIntent();
                Bundle b = intent.getExtras();
                a = b.getInt("id");
                data.put(UPLOAD_KEY, uploadImage);
                data.put("id", String.valueOf(a));
                data.put("Name", name);
                data.put("productprice", String.valueOf(price));

                String result = rh.sendPostRequest(UPLOAD_URL,data);

                return result;
            }
        }


        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonChoose) {
            showFileChooser();
        }
        if(v == buttonUpload){
            uploadImage();
        }
    }
}