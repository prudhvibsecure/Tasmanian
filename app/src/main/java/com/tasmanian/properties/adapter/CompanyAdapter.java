package com.tasmanian.properties.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tasmanian.properties.R;
import com.tasmanian.properties.common.AppSettings;
import com.tasmanian.properties.common.Item;
import com.tasmanian.properties.imageloader.ImageLoader;

import java.util.Vector;

/**
 * Created by w7u on 11/7/2016.
 */

public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.ContactViewHolder> {

    Vector<Item> items = new Vector<Item>();
    private Context context = null;
    public ImageLoader imageLoader;

    public CompanyAdapter(Context context, String domainName) {
        this.context = context;
        imageLoader = new ImageLoader(context, false);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onBindViewHolder(CompanyAdapter.ContactViewHolder contactViewHolder, int i) {

        Item item = items.get(i);

        contactViewHolder.template_msg.setText(item.getAttribute("description"));
        if (item.getAttribute("description").length()==0){
            contactViewHolder.template_msg.setText("There is no Information");
        }
        // contactViewHolder.template_date.setText(item.getAttribute("addeddate"));

        contactViewHolder.template_title.setText(item.getAttribute("company_name"));

        String image_path = AppSettings.getInstance(context).getPropertyValue("i_paths");

        String imageUrl = item.getAttribute("company_logo");

        String I_url = image_path + imageUrl;
        imageLoader.DisplayImage(I_url, contactViewHolder.template_icon, 2);


    }

    @Override
    public CompanyAdapter.ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_property_task, viewGroup,
                false);

        itemView.setOnClickListener((View.OnClickListener) context);

        return new CompanyAdapter.ContactViewHolder(itemView);
    }

    public int getCount() {
        return items.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position + 1;
    }

    public void setItems(Vector<Item> vector) {
        this.items = vector;
    }

    public Vector<Item> getItems() {
        return this.items;
    }

    public void clear() {
        items.clear();
    }


    public void release() {
        items.clear();
        imageLoader.clearCache();
        items = null;
        imageLoader = null;
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {

        protected TextView template_title;

        protected TextView template_msg;

        protected TextView template_date;

        protected ImageView template_icon;


        protected RelativeLayout rv_row_lisiting_layout;

        public ContactViewHolder(View v) {
            super(v);

            template_title = (TextView) v.findViewById(R.id.title_row);

            template_msg = (TextView) v.findViewById(R.id.messgae_row);

            template_date = (TextView) v.findViewById(R.id.date_time);

            template_icon = (ImageView) v.findViewById(R.id.property_image);

            rv_row_lisiting_layout = (RelativeLayout) v.findViewById(R.id.rv_row_listing);

        }
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }
}