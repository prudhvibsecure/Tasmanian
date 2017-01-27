package com.tasmanian.properties.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tasmanian.properties.MemberRegistration;
import com.tasmanian.properties.R;
import com.tasmanian.properties.Register;
import com.tasmanian.properties.models.Offers;

import java.util.ArrayList;
import java.util.List;

public class OfferAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    public List<Offers> offerlist = null;

    private ArrayList<Offers> arraylist;
    int count = 0;
    int progress = 0;

    public OfferAdapter(Context context, List<Offers> arraylist) {
        this.context = context;
        offerlist = arraylist;

    }

    @Override
    public int getCount() {
        return offerlist.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position + 1;
    }

    public static class ViewHolder {
        TextView desc;
        TextView exp_date;
        TextView offer_title;
        TextView offer_date;
        TextView view_off_btn;

    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.offers_row_ly, parent, false);
        holder = new ViewHolder();
        holder.desc = (TextView) itemView.findViewById(R.id.off_desc);
        holder.offer_date = (TextView) itemView.findViewById(R.id.offer_date);
        holder.exp_date = (TextView) itemView.findViewById(R.id.exp_datee);
        holder.view_off_btn = (TextView) itemView.findViewById(R.id.view_offer);
        holder.offer_title = (TextView) itemView.findViewById(R.id.off_title);

        if (getFromStore("username").length()==0){
            holder.view_off_btn.setVisibility(View.VISIBLE);
            holder.offer_date.setVisibility(View.GONE);
            holder.desc.setVisibility(View.GONE);
            holder.exp_date.setVisibility(View.GONE);
        }
        holder.offer_title.setText((Html.fromHtml(offerlist.get(position).getTitle())));
        holder.desc.setText((Html.fromHtml(offerlist.get(position).getDesc())));
        holder.offer_date.setText((Html.fromHtml("<font color='#400093'><b>Offer Date</b></font>:" + " " + offerlist.get(position).getOfferdate())));
        holder.exp_date.setText((Html.fromHtml("<font color='#400093'><b>Expiry Date</b></font>:" + " " + offerlist.get(position).getExpdate())));
        holder.view_off_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
				Intent register=new Intent(context, Register.class);
                context.startActivity(register);

            }
        });
        return itemView;
    }

    public String getFromStore(String key) {
        return context.getSharedPreferences("Tasmanian", 0).getString(key, "");
    }
}
