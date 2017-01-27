package com.tasmanian.properties.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tasmanian.properties.R;
import com.tasmanian.properties.models.ColorGenerator;
import com.tasmanian.properties.models.Offers;
import com.tasmanian.properties.models.Reviews;
import com.tasmanian.properties.models.TextDrawable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by w7u on 12/9/2016.
 */

public class ReviewsAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    public List<Reviews> reviewsList = null;

    private ArrayList<Offers> arraylist;
    private TextDrawable.IBuilder builder = null;
    private ColorGenerator generator = ColorGenerator.MATERIAL;

    public ReviewsAdapter(Context context, List<Reviews> arraylist) {
        this.context = context;
        reviewsList = arraylist;

    }

    @Override
    public int getCount() {
        return reviewsList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position + 1;
    }

    public static class ViewHolder {
        TextView messgae_desc;
        TextView date_time_reviwe;
        TextView reviwer_name;
        ImageView reverImage;


    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        final ReviewsAdapter.ViewHolder holder;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.reviwer_row_tiem, parent, false);
        holder = new ReviewsAdapter.ViewHolder();

        builder = TextDrawable.builder().beginConfig().toUpperCase().textColor(Color.WHITE).endConfig().round();

        holder.messgae_desc = (TextView) itemView.findViewById(R.id.messgae_desc);
        holder.date_time_reviwe = (TextView) itemView.findViewById(R.id.date_time_reviwe);
        holder.reviwer_name = (TextView) itemView.findViewById(R.id.reviwer_name);
        holder.reverImage = (ImageView) itemView.findViewById(R.id.reviwer_image);

        holder.messgae_desc.setText((Html.fromHtml(reviewsList.get(position).getDescription())));
        holder.date_time_reviwe.setText((Html.fromHtml(reviewsList.get(position).getDate())));
        holder.reviwer_name.setText((Html.fromHtml(reviewsList.get(position).getName())));

        int color = generator.getColor(reviewsList.get(position).getName());
        TextDrawable ic1 = builder.build(reviewsList.get(position).getName().substring(0, 1), color);
        holder.reverImage.setImageDrawable(ic1);

        return itemView;
    }

}
