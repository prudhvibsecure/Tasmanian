package com.tasmanian.properties.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Vector;

import com.tasmanian.properties.R;
import com.tasmanian.properties.common.AppSettings;
import com.tasmanian.properties.common.Item;
import com.tasmanian.properties.imageloader.ImageLoader;

/**
 * Created by w7 on 27/08/2016.
 */
public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ContactViewHolder> {

    Vector<Item> items = new Vector<Item>();
    private Context context = null;
    public ImageLoader imageLoader;

    public ProductsAdapter(Context context, String domainName) {
        this.context = context;
        imageLoader = new ImageLoader(context, false);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int i) {

        Item item = items.get(i);

        contactViewHolder.template_name.setText(Html.fromHtml(item.getAttribute("name")));

        contactViewHolder.template_title.setText(Html.fromHtml(item.getAttribute("title")));

        contactViewHolder.template_des.setText(Html.fromHtml("<font color='#DA0909'><b>Description</b></font>:<br/><br/>" + "" + item.getAttribute("description")));

        contactViewHolder.template_coutry.setText(Html.fromHtml("<font color='#DA0909'><b>Country</b></font>:" + "" + item.getAttribute("countryoforigin")));

        String image_path = AppSettings.getInstance(context).getPropertyValue("i_paths");

        String imageUrl = item.getAttribute("pimage");

        String I_url = image_path + imageUrl;
        imageLoader.DisplayImage(I_url, contactViewHolder.template_img, 2);
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.products_lay_row, viewGroup,
                false);

      //  itemView.setOnClickListener((View.OnClickListener) context);

        return new ContactViewHolder(itemView);
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

        protected TextView template_des;

        protected TextView template_name;

        protected TextView template_coutry;

        protected TextView template_title;

        protected ImageView template_img;


        protected RelativeLayout rv_row_listing;

        public ContactViewHolder(View v) {
            super(v);

            template_name = (TextView) v.findViewById(R.id.template_name);
            template_des = (TextView) v.findViewById(R.id.c_name_desc);
            template_title = (TextView) v.findViewById(R.id.template_title);
            template_coutry = (TextView) v.findViewById(R.id.template_coutry);
            template_img= (ImageView) v.findViewById(R.id.product_image);
            rv_row_listing = (RelativeLayout) v.findViewById(R.id.rv_row_listing);

        }
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

}
