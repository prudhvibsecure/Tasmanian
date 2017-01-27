package com.tasmanian.properties;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tasmanian.properties.adapter.LproductsAdapter;
import com.tasmanian.properties.adapter.OfferAdapter;
import com.tasmanian.properties.adapter.ReviewsAdapter;
import com.tasmanian.properties.callbacks.IItemHandler;
import com.tasmanian.properties.common.AppSettings;
import com.tasmanian.properties.imageloader.ImageLoader;
import com.tasmanian.properties.models.Offers;
import com.tasmanian.properties.models.Products;
import com.tasmanian.properties.models.Reviews;
import com.tasmanian.properties.providers.JustifiedTextView;
import com.tasmanian.properties.tasks.HTTPostJson;
import com.tasmanian.properties.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by w7u on 12/23/2016.
 */

public class TcompanyDetails extends AppCompatActivity implements IItemHandler, View.OnClickListener {

    private View layout = null;
    private String cid;
    private ImageLoader imageLoader = null;
    private CollapsingToolbarLayout collapsingToolbar = null;

    private String phno, email, fb_link, glink, tlink, weblink;

    private Dialog dialog;

    private ListView listview_offer;
    private OfferAdapter adapter;
    private List<Offers> offerList = null;

    private LproductsAdapter aproadapter = null;

    private ListView listview_ratings, listview_products;
    private ReviewsAdapter rdapter;
    private List<Reviews> reviewses = null;
    private List<Products> products = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_maps_layout);

        Intent data = getIntent();
        cid = data.getStringExtra("cid");
        imageLoader = new ImageLoader(this, false);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_my_back);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setCollapsedTitleTextColor(Color.BLACK);
        collapsingToolbar.setTitle("");

        AppBarLayout appbar = (AppBarLayout) findViewById(R.id.appbar);
        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {

            boolean isVisible = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle("Company Details");
                    isVisible = true;
                } else if (isVisible) {
                    collapsingToolbar.setTitle("");
                    isVisible = false;
                }
            }
        });
        findViewById(R.id.comp_review).setOnClickListener(this);
        findViewById(R.id.comp_fb).setOnClickListener(this);
        findViewById(R.id.comp_gplus).setOnClickListener(this);
        findViewById(R.id.comp_twitter).setOnClickListener(this);
        findViewById(R.id.comp_email).setOnClickListener(this);
        findViewById(R.id.contact_name).setOnClickListener(this);
        findViewById(R.id.offer_more).setOnClickListener(this);
        findViewById(R.id.products_more).setOnClickListener(this);
        findViewById(R.id.rating_more).setOnClickListener(this);


        TextView tv_offerts=(TextView)findViewById(R.id.offer_heading);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/The_Black_Box-FFP.ttf");
        tv_offerts.setTypeface(custom_font);

        TextView tv_products=(TextView)findViewById(R.id.products_heading);
        tv_products.setTypeface(custom_font);
        TextView tv_ratings=(TextView)findViewById(R.id.rating_heading);
        tv_ratings.setTypeface(custom_font);

        getCompanyData();

        try {
            String link = AppSettings.getInstance(this).getPropertyValue("view_compny_details");
            JSONObject object = new JSONObject();
            object.put("cid", cid);

            HTTPostJson post = new HTTPostJson(this, this, object.toString(), 3);
            post.setContentType("application/json");
            post.execute(link, "");

            // Utils.showProgress(getString(R.string.pwait), this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        getProductData(1, 0);
        getRating(1, 0);
    }

    private void getCompanyData() {

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

    private void getProductData(int requestId, int currentNo) {


        try {
            String link = AppSettings.getInstance(this).getPropertyValue("view_compny_details");
            JSONObject object = new JSONObject();
            object.put("cid", cid);

            HTTPostJson post = new HTTPostJson(this, this, object.toString(), 5);
            post.setContentType("application/json");
            post.execute(link, "");
            //Utils.showProgress(getString(R.string.pwait), getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getRating(int requestId, int currentNo) {


        try {
            String link = AppSettings.getInstance(this).getPropertyValue("view_compny_details");
            JSONObject object = new JSONObject();
            object.put("cid", cid);

            HTTPostJson post = new HTTPostJson(this, this, object.toString(), 4);
            post.setContentType("application/json");
            post.execute(link, "");
            //Utils.showProgress(getString(R.string.pwait), getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFinish(Object results, int requestType) {
        offerList = new ArrayList<Offers>();
        reviewses = new ArrayList<Reviews>();
        products = new ArrayList<Products>();
        Utils.dismissProgress();

        try {

            switch (requestType) {
                case 1:
                    if (results != null) {
                        JSONObject object = new JSONObject(results.toString());
                        ((TextView) findViewById(R.id.tv_views_pages)).setText(object.optString("pageviews"));
                        String rating = object.optString("rate");

                        if (rating.equalsIgnoreCase("0.1") || rating.equalsIgnoreCase("0.2") || rating.equalsIgnoreCase("0.3") || rating.equalsIgnoreCase("0.4") || rating.equalsIgnoreCase("0.5") || rating.equalsIgnoreCase("0.6") || rating.equalsIgnoreCase("0.7") || rating.equalsIgnoreCase("0.8") || rating.equalsIgnoreCase("0.9") || rating.equalsIgnoreCase("Not Rated Yet")) {
                            ((TextView) findViewById(R.id.comp_rating)).setText(object.optString("rate"));
                            ((TextView) findViewById(R.id.comp_rating)).setBackgroundColor(Color.RED);
                        } else {
                            ((TextView) findViewById(R.id.comp_rating)).setText(object.optString("rate"));
                            ((TextView) findViewById(R.id.comp_rating)).setBackgroundColor(Color.GREEN);
                        }
                        if (object.has("status") && object.optString("status").equalsIgnoreCase("0")) {
                            JSONArray array = object.getJSONArray("company_detail");
                            if (array != null && array.length() > 0) {
                                JSONObject jObject = array.getJSONObject(0);
                                ((TextView) findViewById(R.id.com_title)).setText(Html.fromHtml(jObject.optString("company_name")));
                                ((TextView) findViewById(R.id.comp_desc)).setText(jObject.optString("description"));

                                phno = jObject.optString("phoneno");
                                email = jObject.optString("email");
                                fb_link = jObject.optString("facebook");
                                tlink = jObject.optString("twitter");
                                glink = jObject.optString("gplus");
                                weblink = jObject.optString("website");


                                String address = jObject.optString("address");
                                if (address == null) {
                                    ((TextView) findViewById(R.id.comp_address)).setText(Html.fromHtml(jObject.optString("streetno") + "," + jObject.optString("streetname")));
                                } else {
                                    ((TextView) findViewById(R.id.comp_address)).setText(Html.fromHtml(address));
                                }

                                String image_path = AppSettings.getInstance(this).getPropertyValue("i_paths");
                                ImageView logo = (ImageView) findViewById(R.id.comp_logo);
                                imageLoader.DisplayImage(image_path + jObject.optString("company_logo"), logo);


                            }
                        }
                    }
                    break;
                case 2:
                    if (results != null) {
                        JSONObject object = new JSONObject(results.toString());
                        if (object.has("status") && object.optString("status").equalsIgnoreCase("0")) {
                            showToast(object.optString("statusdescription"));
                            closeDialog();
                        }
                        showToast(object.optString("statusdescription"));
                    }
                    break;
                case 3:
                    JSONObject joject = new JSONObject(results.toString());
                    if (joject.has("status") && joject.optString("status").equalsIgnoreCase("0")) {
                        JSONArray array = joject.getJSONArray("company_exoffers_detail");
                        if (array != null && array.length() > 0) {
                            for (int i = 0; i < 2; i++) {
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
                            listview_offer = (ListView) findViewById(R.id.offers_list);
                            adapter = new OfferAdapter(this, offerList);
                            listview_offer.setAdapter(adapter);

//                            listview_offer.setOnTouchListener(new View.OnTouchListener() {
//                                // Setting on Touch Listener for handling the touch inside ScrollView
//                                @Override
//                                public boolean onTouch(View v, MotionEvent event) {
//                                    // Disallow the touch request for parent scroll on touch of child view
//                                    v.getParent().requestDisallowInterceptTouchEvent(true);
//                                    return false;
//                                }
//                            });
                            RelativeLayout tv_mdata = (RelativeLayout) findViewById(R.id.offer_more_ll);
                            tv_mdata.setVisibility(View.VISIBLE);
                        } else {
                            TextView tv_mdata = (TextView) findViewById(R.id.no_ex_offer);
                            tv_mdata.setVisibility(View.VISIBLE);
                            tv_mdata.setText("No Exclusive Offers Found");

                        }

                    }
                    break;
                case 4:
                    JSONObject jsonObject = new JSONObject(results.toString());
                    if (jsonObject.has("status") && jsonObject.optString("status").equalsIgnoreCase("0")) {
                        JSONArray array = jsonObject.getJSONArray("reviews_list");
                        if (array.equals(null)) {
                            TextView tv_mdata = (TextView) findViewById(R.id.no_ratings);
                            tv_mdata.setVisibility(View.VISIBLE);
                            tv_mdata.setText("No Reviews Found");
                            return;
                        }
                        if (array != null && array.length() > 0) {
                            for (int i = 0; i < 2; i++) {
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
                            listview_ratings = (ListView) findViewById(R.id.ratings_list);
                            rdapter = new ReviewsAdapter(this, reviewses);
                            listview_ratings.setAdapter(rdapter);
//                            listview_ratings.setOnTouchListener(new View.OnTouchListener() {
//                                // Setting on Touch Listener for handling the touch inside ScrollView
//                                @Override
//                                public boolean onTouch(View v, MotionEvent event) {
//                                    // Disallow the touch request for parent scroll on touch of child view
//                                    v.getParent().requestDisallowInterceptTouchEvent(true);
//                                    return false;
//                                }
//                            });
                            RelativeLayout tv_more = (RelativeLayout) findViewById(R.id.rating_more_ll);
                            tv_more.setVisibility(View.VISIBLE);
                        } else {
                            TextView tv_mdata = (TextView) findViewById(R.id.no_ratings);
                            tv_mdata.setVisibility(View.VISIBLE);
                            tv_mdata.setText("No Reviews Found");


                        }

                    }
                    break;

                case 5:
                    JSONObject jpObect = new JSONObject(results.toString());
                    if (jpObect.has("status") && jpObect.optString("status").equalsIgnoreCase("0")) {
                        JSONArray array = jpObect.getJSONArray("company_product_detail");
                        if (array != null && array.length() > 0) {
                            for (int i = 0; i < 1; i++) {
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
                            listview_products = (ListView) findViewById(R.id.products_list);
                            aproadapter = new LproductsAdapter(this, products);
                            listview_products.setAdapter(aproadapter);
//                            listview_products.setOnTouchListener(new View.OnTouchListener() {
//                                // Setting on Touch Listener for handling the touch inside ScrollView
//                                @Override
//                                public boolean onTouch(View v, MotionEvent event) {
//                                    // Disallow the touch request for parent scroll on touch of child view
//                                    v.getParent().requestDisallowInterceptTouchEvent(true);
//                                    return false;
//                                }
//                            });

                            RelativeLayout tv_more = (RelativeLayout) findViewById(R.id.products_more_ll);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                TcompanyDetails.this.finish();
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

    @Override
    public void onClick(View v) {

        switch (v.getId()) {


            case R.id.comp_review:
                String username = getFromStore("username");
                if (username.length() != 0) {
                    showReviewDialog();
                } else {
                    showToast("Sorry-You have to be a Subscriber to Post a Review-Please Register Now");
                }

                break;
            case R.id.dialog_rating_button:
                makeRequestRating();
                break;
            case R.id.comp_fb:

                if (fb_link.toString().length() == 0) {
                    showToast("Facebook url not found...");
                    return;
                } else {
                    Intent gurl = new Intent(Intent.ACTION_VIEW, Uri.parse(fb_link));
                    try {
                        startActivity(gurl);
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(this, "Sorry there is no page.", Toast.LENGTH_SHORT).show();
                    }

                }
                break;
            case R.id.comp_twitter:


                if (tlink.toString().length() == 0) {
                    showToast("Twitter url not found...");
                    return;
                } else {
                    Intent gurl = new Intent(Intent.ACTION_VIEW, Uri.parse(tlink));
                    try {
                        startActivity(gurl);
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(this, "Sorry there is no page.", Toast.LENGTH_SHORT).show();
                    }

                }
                break;
            case R.id.comp_gplus:

//                Spanned gpurl=Html.fromHtml (((TextView) layout.findViewById(R.id.comp_gplus)).getText().toString());
//
                if (glink.toString().length() == 0) {
                    showToast("Googleplus url not found...");
                    return;
                } else {
                    Intent gurl = new Intent(Intent.ACTION_VIEW, Uri.parse(glink));
                    try {
                        startActivity(gurl);
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(this, "Sorry there is no page.", Toast.LENGTH_SHORT).show();
                    }

                }

                break;

            case R.id.contact_name:

//                Spanned number=Html.fromHtml (((TextView) layout.findViewById(R.id.contact_name)).getText().toString());
//
                if (phno.toString().length() == 0) {
                    showToast("Number not found...");
                    return;
                } else {
//                    Intent conurl = new Intent(Intent.ACTION_CALL, Uri.parse(phno.toString()));
//                    startActivity(conurl);
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phno));
                    startActivity(intent);
                }

                break;
            case R.id.comp_email:

//                Spanned comp_email=Html.fromHtml (((TextView) layout.findViewById(R.id.comp_email)).getText().toString());
//
                if(email.toString().length()==0){
                    showToast("Email Address not found...");
                    return;
                }else {
                    String[] CC = {""};
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.setType("text/html");
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                    emailIntent.putExtra(Intent.EXTRA_CC, CC);
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "Type here message");
                    startActivity(emailIntent);
                }
                break;
            case R.id.offer_more:
                launchActivity(OffersFrgment.class);
                break;
            case R.id.products_more:

                launchActivity(ProductsFr.class);

                break;
            case R.id.rating_more:
                launchActivity(RateListFragment.class);
                break;
        }

    }

    private void closeDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
        dialog = null;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }
    private void makeRequestRating() {
        try {
            RatingBar sRatingBar = (RatingBar) dialog.findViewById(R.id.dialog_rating_rating_bar);
            String rate = String.valueOf(sRatingBar.getRating());
            if (rate.equalsIgnoreCase("0.0")) {
                ((RatingBar) dialog.findViewById(R.id.dialog_rating_rating_bar)).requestFocus();
                Toast.makeText(this, "Give your rating...", Toast.LENGTH_LONG).show();
                return;
            }

            if (((EditText) dialog.findViewById(R.id.review_et)).getText().length() == 0) {
                showToast("Please Give Your Feedback Message...");
                return;
            }

            String username = getFromStore("username");
            if (username.length() == 0) {
                username = "Guset";
            }
            String link = AppSettings.getInstance(this).getPropertyValue("rate_review");
            JSONObject object = new JSONObject();
            object.put("username", username);
            object.put("cid", cid);
            object.put("rate", rate);
            object.put("description", ((EditText) dialog.findViewById(R.id.review_et)).getText().toString());


            HTTPostJson post = new HTTPostJson(this, this, object.toString(), 2);
            post.setContentType("application/json");
            post.execute(link, "");
            Utils.showProgress(getString(R.string.pwait), this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void showToast(String value) {
        Toast.makeText(this, value, Toast.LENGTH_SHORT).show();
    }

    public void showToast(int text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public String getFromStore(String key) {
        return getSharedPreferences("Tasmanian", 0).getString(key, "");
    }

    private void showReviewDialog() {

        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.user_review_dialog);
        dialog.findViewById(R.id.dialog_rating_button).setOnClickListener(this);
        dialog.show();

    }

    private void launchActivity(Class<?> cls) {
        Intent products = new Intent(getApplicationContext(), cls);
        products.putExtra("cid", cid);
        startActivity(products);
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }

}
