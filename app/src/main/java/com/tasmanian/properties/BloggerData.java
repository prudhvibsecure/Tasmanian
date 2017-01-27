package com.tasmanian.properties;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.InterstitialAd;
import com.tasmanian.properties.common.AppSettings;
import com.tasmanian.properties.imageloader.ImageLoader;

/**
 * Created by w7u on 1/18/2017.
 */

public class BloggerData extends AppCompatActivity {

    private TextView tv_desc,tv_date;

    private ImageView tv_image;

    private String newsdate,title,desc,image;
    private String nid;
    private ImageLoader imageLoader = null;
    private CollapsingToolbarLayout collapsingToolbar = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.received_message_row);

        tv_image=(ImageView)findViewById(R.id.show_disimage);
        tv_desc=(TextView) findViewById(R.id.message_display);
        tv_date=(TextView) findViewById(R.id.message_display_date);

        Intent data = getIntent();
        if (data!=null) {
            newsdate = data.getStringExtra("newsdate");
            desc = data.getStringExtra("description");
            title = data.getStringExtra("title");
            image = data.getStringExtra("newsimage");
            imageLoader = new ImageLoader(this, false);

        }

        String image_path = AppSettings.getInstance(this).getPropertyValue("i_paths");
        String I_url = image_path + image;
        imageLoader.DisplayImage(I_url, tv_image, 2);

        tv_desc.setText(Html.fromHtml(desc));
        tv_date.setText(Html.fromHtml(newsdate));


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_my_back);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setCollapsedTitleTextColor(Color.BLACK);
        collapsingToolbar.setTitle(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                BloggerData.this.finish();
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }
}
