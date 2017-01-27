package com.tasmanian.properties;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.tasmanian.properties.R;
import com.tasmanian.properties.adapter.OfferAdapter;
import com.tasmanian.properties.callbacks.IItemHandler;
import com.tasmanian.properties.common.AppSettings;
import com.tasmanian.properties.fragments.ParentFragment;
import com.tasmanian.properties.models.Offers;
import com.tasmanian.properties.tasks.HTTPostJson;
import com.tasmanian.properties.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by w7u on 11/7/2016.
 */

public class OffersFrgment extends AppCompatActivity implements IItemHandler {
    private View layout = null;
    private String cid;
    private ListView listview_offer;
    private OfferAdapter adapter;
    private List<Offers> offerList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offers_layout);

        Intent data = getIntent();
        cid = data.getStringExtra("cid");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_my_back);
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.setTitle("Offers");
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
    public void onFinish(Object results, int requestType) {
        offerList = new ArrayList<Offers>();
        Utils.dismissProgress();
        try {

            switch (requestType) {

                case 1:
                    JSONObject joject = new JSONObject(results.toString());
                    if (joject.has("status") && joject.optString("status").equalsIgnoreCase("0")) {
                        JSONArray array = joject.getJSONArray("company_exoffers_detail");
                        if (array != null && array.length() > 0) {
                            for (int i = 0; i < array.length(); i++) {
                                Offers offerlist = new Offers();
                                JSONObject jObject = array.getJSONObject(i);

                                String title = jObject.optString("title");
                                String desc = jObject.optString("offer");
                                String offer_date = jObject.optString("offerdate");
                                String exp_date = jObject.optString("expirydate");

                                offerlist.setTitle(title);
                                offerlist.setDesc(desc);
                                offerlist.setExpdate(exp_date);
                                offerlist.setOfferdate(offer_date);
                                offerList.add(offerlist);
                            }
                            listview_offer = (ListView) findViewById(R.id.ex_offers);
                            adapter = new OfferAdapter(this, offerList);
                            listview_offer.setAdapter(adapter);
                        } else {
                            TextView tv_mdata=(TextView)findViewById(R.id.no_ex_offer);
                            tv_mdata.setVisibility(View.VISIBLE);
                            tv_mdata.setText("No Exclusive Offers Found");

                        }

                    }
                    break;

                default:
                    break;

            }

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

                OffersFrgment.this.finish();
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onError(String errorCode, int requestType) {

    }
}