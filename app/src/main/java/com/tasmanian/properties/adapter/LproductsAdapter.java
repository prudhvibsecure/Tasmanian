package com.tasmanian.properties.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tasmanian.properties.MemberRegistration;
import com.tasmanian.properties.R;
import com.tasmanian.properties.common.AppSettings;
import com.tasmanian.properties.imageloader.ImageLoader;
import com.tasmanian.properties.models.Offers;
import com.tasmanian.properties.models.Products;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by w7u on 12/26/2016.
 */

public class LproductsAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    public List<Products> productslist = null;
    public ImageLoader imageLoader;

    private ArrayList<Products> arraylist;
    int count = 0;
    int progress = 0;

    public LproductsAdapter(Context context, List<Products> arraylist) {
        this.context = context;
        productslist = arraylist;
        imageLoader = new ImageLoader(context, false);


    }

    @Override
    public int getCount() {
        return productslist.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position + 1;
    }

    public static class ViewHolder {
        TextView template_name;
        TextView template_title;
        TextView template_des;
        TextView template_coutry;
        ImageView template_img;

    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        final LproductsAdapter.ViewHolder holder;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.products_lay_row, parent, false);
        holder = new LproductsAdapter.ViewHolder();
        holder.template_name = (TextView) itemView.findViewById(R.id.template_name);
        holder.template_title = (TextView) itemView.findViewById(R.id.template_title);
        holder.template_des = (TextView) itemView.findViewById(R.id.c_name_desc);
        holder.template_coutry = (TextView) itemView.findViewById(R.id.template_coutry);
        holder.template_img = (ImageView) itemView.findViewById(R.id.product_image);


        holder.template_name.setText(Html.fromHtml(productslist.get(position).getPrname()));

        holder.template_title.setText(Html.fromHtml(productslist.get(position).getTitle()));

        holder.template_des.setText(Html.fromHtml(productslist.get(position).getDesc()));
        holder.template_des.setEllipsize(TextUtils.TruncateAt.END);

        holder.template_coutry.setText(productslist.get(position).getCountry());

        String image_path = AppSettings.getInstance(context).getPropertyValue("i_paths");

        String imageUrl = productslist.get(position).getPurl();

        String I_url = image_path + imageUrl;
        imageLoader.DisplayImage(I_url, holder.template_img, 2);
        return itemView;
    }

    public String getFromStore(String key) {
        return context.getSharedPreferences("Tasmanian", 0).getString(key, "");
    }
}