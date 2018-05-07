package com.example.user.uploadimages;

/**
 * Created by user on 2/23/2018.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;


public  class VegitableShop extends Activity {
        //hi
        private static final String TAG = VegitableShop.class.getSimpleName();
        private static final String URL = "http://sabkuch.co.in/sabkuckapp/fruits.php";
        int amount = 0;
        private RecyclerView recyclerView;
        private List<Vegitable> movieList;
        private StoreAdapter mAdapter;
        Intent intent;
    String Fruit_name;
    int Fruit_id;
   // SQLiteHelper myDB;



        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // Inflate the layout for this fragment
            setContentView(R.layout.fragment_store);

            recyclerView = findViewById(R.id.recycler_view);
            movieList = new ArrayList<>();
            mAdapter = new StoreAdapter(VegitableShop.this, movieList);
          //  myDB = new SQLiteHelper(getActivity());
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(VegitableShop.this, 2);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(8), true));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);
            recyclerView.setNestedScrollingEnabled(false);

            fetchStoreItems();


        }

        private void fetchStoreItems() {
            JsonArrayRequest request = new JsonArrayRequest(URL,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            if (response == null) {
                                Toast.makeText(VegitableShop.this, "Couldn't fetch the store items! Pleas try again.", Toast.LENGTH_LONG).show();
                                return;
                            }

                            List<Vegitable> items = new Gson().fromJson(response.toString(), new TypeToken<List<Vegitable>>() {
                            }.getType());

                            movieList.clear();
                            movieList.addAll(items);

                            // refreshing recycler view
                            mAdapter.notifyDataSetChanged();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // error in getting json
                    Log.e(TAG, "Error: " + error.getMessage());
                    Toast.makeText(VegitableShop.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

           MyApplication.getInstance().addToRequestQueue(request);
        }

        public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

            private int spanCount;
            private int spacing;
            private boolean includeEdge;

            public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
                this.spanCount = spanCount;
                this.spacing = spacing;
                this.includeEdge = includeEdge;
            }

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int position = parent.getChildAdapterPosition(view); // item position
                int column = position % spanCount; // item column

                if (includeEdge) {
                    outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                    outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                    if (position < spanCount) { // top edge
                        outRect.top = spacing;
                    }
                    outRect.bottom = spacing; // item bottom
                } else {
                    outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                    outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                    if (position >= spanCount) {
                        outRect.top = spacing; // item top
                    }
                }
            }
        }

        /**
         * Converting dp to pixel
         */
        private int dpToPx(int dp) {
            Resources r = getResources();
            return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
        }

        class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.MyViewHolder> {
            private Context context;
            ImageLoader imageLoader;
            private List<Vegitable> movieList;
            final int[] count = {0};


            public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
                CheckBox c1;
                public TextView name, price, txtCount, id;
                public ImageView thumbnail,imgRemove;
                double amount = 0;
                public Button buttonInc, buttonDec, orderNow;
Button btn_update;
                public MyViewHolder(View view) {
                    super(view);
                    name = view.findViewById(R.id.txtName);
                    price = view.findViewById(R.id.txtPrice);
                    id = view.findViewById(R.id.id);
                    thumbnail = view.findViewById(R.id.image);
                    imgRemove = view.findViewById(R.id.imgRemove);
                    btn_update=view.findViewById(R.id.update);
                    view.setOnClickListener(this);

                    // intent = new Intent(context, BillActivity.class);

                   // final TextView txtCount = (TextView) view.findViewById(R.id.txt);
                   // Button buttonInc = (Button) view.findViewById(R.id.button1);
                   // Button buttonDec = (Button) view.findViewById(R.id.button2);


                }
                public void onClick(View v) {

                }
            }


            public StoreAdapter(Context context, List<Vegitable> movieList) {
                this.context = context;
                this.movieList = movieList;
            }

            @Override
            public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout, parent, false);

                return new MyViewHolder(itemView);
            }

            @Override
            public void onBindViewHolder(MyViewHolder holder, final int position) {
                final Vegitable movie = movieList.get(position);
                holder.name.setText(movie.getName());
                holder.price.setText(String.valueOf(movie.getPrice()));
                holder.id.setText(String.valueOf(movie.getid()));
                //   imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
                //   imageLoader.get(movie.getImage(), ImageLoader.getImageListener(holder.thumbnail, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));
                //   imageLoader.get(rc.getProfileImage(), ImageLoader.getImageListener(holder.circularImageView, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));
                Glide.with(context)
                        .load(movie.getImage())
                        .into(holder.thumbnail);


                holder.btn_update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent_update=new Intent(context,UpdateActivity.class);
                        intent_update.putExtra("id",movie.getid());
                        intent_update.putExtra("name",movie.getName());
                        intent_update.putExtra("productprice",movie.getPrice());
                        //Toast.makeText(context, ""+movie.getPrice(), Toast.LENGTH_SHORT).show();
                        intent_update.putExtra("image",movie.getImage());
                        startActivity(intent_update);
                    }
                });

                holder.imgRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new DeleteProduct();
                    }
                });

            }


            @Override
            public int getItemCount() {
                return movieList.size();
            }
        }
   }
