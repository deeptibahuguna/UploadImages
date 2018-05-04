package com.example.user.uploadimages;

/**
 * Created by Pramila on 6/6/2017.
 */
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Pramila on 6/5/2017.
 */
public class OrderListAdapter  extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {
    ArrayList<GetData> GetData=null;
    Context context;
    String id;

    public OrderListAdapter(Context applicationContext, ArrayList<GetData> GetData) {
        this.GetData = GetData;
        this.context = applicationContext;
        //this.resource = cardviewevent;
        // this.getarray = GetData;

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardviewevent, parent, false);
        ViewHolder ViewHolder = new ViewHolder(v, context, GetData);
        return ViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String Date ,phoneNumber , Name;
        GetData GetDatas = GetData.get(position);
        Date = " Date: "+GetDatas.getDate();
        phoneNumber = " Phone Number: "+GetDatas.getPhone();
        Name = " Name: "+GetDatas.getName();


        holder.txtdatetime.setText(Date);
        holder.txtName.setText(Name);
        holder.txtPhone.setText(phoneNumber);

    }


    @Override
    public int getItemCount() {
        return GetData.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        ImageView imageView;
        TextView txtdatetime, txtName,txtPhone;
        LinearLayout linearLayout;
        ArrayList<GetData> mdata = new ArrayList<GetData>();
        Context ctx;

        public ViewHolder(View itemView, Context ctx, ArrayList<GetData> mdata) {
            super(itemView);
            this.ctx = ctx;
            this.mdata = mdata;
            linearLayout=(LinearLayout) itemView.findViewById(R.id.linear_layout);
            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtdatetime = (TextView) itemView.findViewById(R.id.txtDateTime);
            txtPhone = (TextView) itemView.findViewById(R.id.txtPhone);
            linearLayout.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            GetData myData = this.mdata.get(position);
            Intent i = new Intent(this.ctx, OnePersonOrder.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("phone",myData.getPhone());
            i.putExtra("date",myData.getDate());
            this.ctx.startActivity(i);
        }
    }
}






