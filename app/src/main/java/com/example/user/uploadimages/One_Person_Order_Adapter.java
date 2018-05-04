package com.example.user.uploadimages;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import static android.R.attr.phoneNumber;

/**
 * Created by Pramila on 5/2/2018.
 */

public class One_Person_Order_Adapter extends  RecyclerView.Adapter<One_Person_Order_Adapter.ViewHolder> {
    ArrayList<GetOrder> getOrders=null;
    Context context;
    String id;

    public One_Person_Order_Adapter(Context applicationContext, ArrayList<GetOrder> getOrders) {
        this.getOrders = getOrders;
        this.context = applicationContext;
        //this.resource = cardviewevent;
        // this.getarray = GetOrder;

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_one_person_order, parent, false);
        ViewHolder ViewHolder = new ViewHolder(v, context, getOrders);
        return ViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String Price,Name,Kg,Total;
        GetOrder getOrder = getOrders.get(position);
        int Weight=getOrder.getWeight();
        int P=getOrder.getPrice();

        Name = " Name: "+getOrder.getName();
        Kg = " Weight: "+getOrder.getWeight();
        Price = " Price: "+getOrder.getPrice();
        Total="Total: "+P+"x"+Weight+" = "+getOrder.getTotal();



        holder.txtName.setText(Name);
        holder.txtPrice.setText(Price);
        holder.txtKg.setText(Kg);
        holder.txtTotal.setText(Total);

    }

    @Override
    public int getItemCount() {
        return getOrders.size();

    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtName,txtKg,txtPrice,txtTotal;
        ArrayList<GetOrder> getOrders = new ArrayList<GetOrder>();
        Context ctx;
        public ViewHolder(View itemView, Context context, ArrayList<GetOrder> getOrders) {
            super(itemView);
            this.ctx = context;
            this.getOrders = getOrders;
            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtPrice = (TextView) itemView.findViewById(R.id.txtPrice);
            txtKg = (TextView) itemView.findViewById(R.id.txtKg);
            txtTotal = (TextView) itemView.findViewById(R.id.txtTotal);

        }

        @Override
        public void onClick(View v) {

        }
    }
}
