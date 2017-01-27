package com.tasmanian.properties;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.tasmanian.properties.R;
import com.tasmanian.properties.adapter.CatListAdapter;
import com.tasmanian.properties.adapter.LproductsAdapter;
import com.tasmanian.properties.adapter.ProductsAdapter;
import com.tasmanian.properties.callbacks.IItemHandler;
import com.tasmanian.properties.common.AppSettings;
import com.tasmanian.properties.common.Item;
import com.tasmanian.properties.fragments.ParentFragment;
import com.tasmanian.properties.helper.RecyclerOnScrollListener;
import com.tasmanian.properties.imageloader.ImageLoader;
import com.tasmanian.properties.models.Products;
import com.tasmanian.properties.tasks.HTTPBackgroundTask;
import com.tasmanian.properties.tasks.HTTPPostTask;
import com.tasmanian.properties.tasks.HTTPostJson;
import com.tasmanian.properties.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by w7u on 11/7/2016.
 */

public class ProductsFr extends AppCompatActivity implements IItemHandler{
    private List<Products> products = null;

    private String cid;
    private boolean isRefresh = false;
    private ListView  listview_products;
    private LproductsAdapter aproadapter = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offers_layout);

        Intent data = getIntent();
        cid = data.getStringExtra("cid");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_my_back);
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.setTitle("Products");
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        getProductData(1, 0);

    }

    private void getProductData(int requestId, int currentNo) {

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
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                ProductsFr.this.finish();
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onFinish(Object results, int requestType) {

        products = new ArrayList<Products>();
        Utils.dismissProgress();

        try {
            switch (requestType) {
                case 1:
                    JSONObject jpObect = new JSONObject(results.toString());
                    if (jpObect.has("status") && jpObect.optString("status").equalsIgnoreCase("0")) {
                        JSONArray array = jpObect.getJSONArray("company_product_detail");
                        if (array != null && array.length() > 0) {
                            for (int i = 0; i < array.length(); i++) {
                                Products prlist = new Products();
                                JSONObject jObject = array.getJSONObject(i);

                                String title = jObject.optString("title");
                                String name = jObject.optString("name");
                                String description = jObject.optString("description");
                                String country = jObject.optString("countryoforigin");

                                String purl = jObject.optString("pimage");

                                prlist.setTitle(title);
                                prlist.setDesc(description);
                                prlist.setCountry(country);
                                prlist.setPrname(name);
                                prlist.setPurl(purl);
                                products.add(prlist);
                            }
                            listview_products = (ListView) findViewById(R.id.ex_offers);
                            aproadapter = new LproductsAdapter(this, products);
                            listview_products.setAdapter(aproadapter);
                            listview_products.setOnTouchListener(new View.OnTouchListener() {
                                // Setting on Touch Listener for handling the touch inside ScrollView
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    // Disallow the touch request for parent scroll on touch of child view
                                    v.getParent().requestDisallowInterceptTouchEvent(true);
                                    return false;
                                }
                            });

                            TextView tv_more = (TextView) findViewById(R.id.products_more);
                            tv_more.setVisibility(View.VISIBLE);
                        } else {
                            TextView tv_mdata = (TextView) findViewById(R.id.no_products);
                            tv_mdata.setVisibility(View.VISIBLE);
                            tv_mdata.setText("No Products Found");

                        }

                    }
                    break;


                default:
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onError(String errorCode, int requestType) {

    }
}