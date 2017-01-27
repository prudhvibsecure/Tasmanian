package com.tasmanian.properties;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.tasmanian.properties.R;
import com.tasmanian.properties.adapter.OfferAdapter;
import com.tasmanian.properties.adapter.ProductsAdapter;
import com.tasmanian.properties.adapter.ReviewsAdapter;
import com.tasmanian.properties.callbacks.IItemHandler;
import com.tasmanian.properties.common.AppSettings;
import com.tasmanian.properties.common.Item;
import com.tasmanian.properties.fragments.ParentFragment;
import com.tasmanian.properties.helper.RecyclerOnScrollListener;
import com.tasmanian.properties.models.Offers;
import com.tasmanian.properties.models.Reviews;
import com.tasmanian.properties.tasks.HTTPPostTask;
import com.tasmanian.properties.tasks.HTTPostJson;
import com.tasmanian.properties.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by w7u on 12/9/2016.
 */

public class RateListFragment extends AppCompatActivity implements IItemHandler {
    private String cid;
    private ListView listview_offer;
    private ReviewsAdapter adapter;
    private List<Reviews> reviewses = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offers_layout);

        Intent data = getIntent();
        cid = data.getStringExtra("cid");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_my_back);
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.setTitle("Reviews");
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        getOfferList();
    }

    private void getOfferList() {

        try {
            String link = AppSettings.getInstance(this).getPropertyValue("view_compny_details");
            JSONObject object = new JSONObject();
            object.put("cid", cid);

            HTTPostJson post = new HTTPostJson(this, this, object.toString(), 1);
            post.setContentType("application/json");
            post.execute(link, "");
            Utils.showProgress(getString(R.string.pwait), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                RateListFragment.this.finish();
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onFinish(Object results, int requestType) {
        reviewses = new ArrayList<Reviews>();
        Utils.dismissProgress();
        try {

            switch (requestType) {

                case 1:
                    JSONObject joject = new JSONObject(results.toString());

                    if (joject.has("status") && joject.optString("status").equalsIgnoreCase("0")) {
                        JSONArray array = joject.getJSONArray("reviews_list");
                        if (array.equals(null)) {
                            TextView tv_mdata = (TextView)findViewById(R.id.no_ex_offer);
                            tv_mdata.setVisibility(View.VISIBLE);
                            tv_mdata.setText("No Reviews Found");
                        }
                        if (array != null && array.length() > 0) {
                            for (int i = 0; i < array.length(); i++) {
                                Reviews reviews = new Reviews();
                                JSONObject jObject = array.getJSONObject(i);
                                String name = jObject.optString("fname") + jObject.optString("lname");
                                String desc = jObject.optString("description");
                                String rdate = jObject.optString("reviewdate");

                                reviews.setDescription(desc);
                                reviews.setDate(rdate);
                                reviews.setName(name);
                                reviewses.add(reviews);
                            }
                            listview_offer = (ListView)findViewById(R.id.ex_offers);
                            adapter = new ReviewsAdapter(this, reviewses);
                            listview_offer.setAdapter(adapter);
                        } else {
                            TextView tv_mdata = (TextView)findViewById(R.id.no_ex_offer);
                            tv_mdata.setVisibility(View.VISIBLE);
                            tv_mdata.setText("No Reviews Found");


                        }

                    }
                    break;

                default:
                    break;

            }

        } catch (Exception e) {
            TextView tv_mdata = (TextView)findViewById(R.id.no_ex_offer);
            tv_mdata.setVisibility(View.VISIBLE);
            tv_mdata.setText("No Reviews Found");
            e.printStackTrace();
        }

    }

    @Override
    public void onError(String errorCode, int requestType) {

    }
}