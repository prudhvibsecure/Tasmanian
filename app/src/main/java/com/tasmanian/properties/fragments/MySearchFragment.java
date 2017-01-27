package com.tasmanian.properties.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.tasmanian.properties.R;
import com.tasmanian.properties.TasmanianProperty;
import com.tasmanian.properties.adapter.CompanyAdapter;
import com.tasmanian.properties.callbacks.IItemHandler;
import com.tasmanian.properties.common.AppSettings;
import com.tasmanian.properties.common.Item;
import com.tasmanian.properties.helper.RecyclerOnScrollListener;
import com.tasmanian.properties.tasks.HTTPPostTask;
import com.tasmanian.properties.tasks.HTTPTask;
import com.tasmanian.properties.tasks.HTTPostJson;
import com.tasmanian.properties.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

/**
 * Created by w7u on 12/20/2016.
 */

public class MySearchFragment extends ParentFragment implements IItemHandler {

    private TasmanianProperty propertUpdates = null;
    private View layout = null;

    private RecyclerView mRecyclerView = null;

    private SwipeRefreshLayout mSwipeRefreshLayout = null;

    private RecyclerOnScrollListener recycScollListener = null;

    private boolean isRefresh = false;

    private AutoCompleteTextView atvPlaces, atvBusiness;

    private ArrayList<String> list = null;

    String place_id;

    public CompanyAdapter adapter = null;

    private String radiustxt = "Auto";

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        this.propertUpdates = (TasmanianProperty) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        layout = inflater.inflate(R.layout.new_tempalte_recyclerview, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) layout.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark, R.color.green,
                R.color.red, R.color.bs_blue, R.color.colorPrimaryDark);
        mSwipeRefreshLayout.setEnabled(false);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                recycScollListener.resetValue();
                // nearstSearch(1, 0);
            }
        });

        atvPlaces = (AutoCompleteTextView) layout.findViewById(R.id.get_laocation_text);
        atvPlaces.setThreshold(1);

        atvPlaces.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                getLocationData(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });
        atvBusiness = (AutoCompleteTextView) layout.findViewById(R.id.get_bustype);

        atvBusiness.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                getBusType();

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });
        atvBusiness.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    nearstSearch(1, 0);
                    return true;
                }
                return false;
            }
        });
        ((TextView) layout.findViewById(R.id.catgr_txt)).setText("No Data Found");
//        ((TextView) layout.findViewById(R.id.tv_txt_title)).setVisibility(View.GONE);
        ((TextView) layout.findViewById(R.id.k5_txt)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.setBackgroundColor(Color.GRAY);
                ((TextView) layout.findViewById(R.id.kauto_txt)).setBackgroundColor(Color.WHITE);
                ((TextView) layout.findViewById(R.id.kauto_txt)).setBackground(getResources().getDrawable(R.drawable.new_border));
                ((TextView) layout.findViewById(R.id.k100_txt)).setBackgroundColor(Color.WHITE);
                ((TextView) layout.findViewById(R.id.k100_txt)).setBackground(getResources().getDrawable(R.drawable.new_border));

                ((TextView) layout.findViewById(R.id.k15_txt)).setBackgroundColor(Color.WHITE);
                ((TextView) layout.findViewById(R.id.k15_txt)).setBackground(getResources().getDrawable(R.drawable.new_border));
                ((TextView) layout.findViewById(R.id.k50_txt)).setBackgroundColor(Color.WHITE);
                ((TextView) layout.findViewById(R.id.k50_txt)).setBackground(getResources().getDrawable(R.drawable.new_border));

                radiustxt = ((TextView) layout.findViewById(R.id.k5_txt)).getText().toString();

            }
        });
        ((TextView) layout.findViewById(R.id.k15_txt)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setBackgroundColor(Color.GRAY);
                ((TextView) layout.findViewById(R.id.kauto_txt)).setBackgroundColor(Color.WHITE);
                ((TextView) layout.findViewById(R.id.kauto_txt)).setBackground(getResources().getDrawable(R.drawable.new_border));

                ((TextView) layout.findViewById(R.id.k100_txt)).setBackgroundColor(Color.WHITE);
                ((TextView) layout.findViewById(R.id.k100_txt)).setBackground(getResources().getDrawable(R.drawable.new_border));
                ((TextView) layout.findViewById(R.id.k5_txt)).setBackgroundColor(Color.WHITE);
                ((TextView) layout.findViewById(R.id.k5_txt)).setBackground(getResources().getDrawable(R.drawable.new_border));
                ((TextView) layout.findViewById(R.id.k50_txt)).setBackgroundColor(Color.WHITE);
                ((TextView) layout.findViewById(R.id.k50_txt)).setBackground(getResources().getDrawable(R.drawable.new_border));

                radiustxt = ((TextView) layout.findViewById(R.id.k15_txt)).getText().toString();
            }
        });
        ((TextView) layout.findViewById(R.id.k50_txt)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setBackgroundColor(Color.GRAY);
                ((TextView) layout.findViewById(R.id.kauto_txt)).setBackgroundColor(Color.WHITE);
                ((TextView) layout.findViewById(R.id.kauto_txt)).setBackground(getResources().getDrawable(R.drawable.new_border));
                ((TextView) layout.findViewById(R.id.k100_txt)).setBackgroundColor(Color.WHITE);
                ((TextView) layout.findViewById(R.id.k100_txt)).setBackground(getResources().getDrawable(R.drawable.new_border));
                ((TextView) layout.findViewById(R.id.k15_txt)).setBackgroundColor(Color.WHITE);
                ((TextView) layout.findViewById(R.id.k15_txt)).setBackground(getResources().getDrawable(R.drawable.new_border));
                ((TextView) layout.findViewById(R.id.k5_txt)).setBackgroundColor(Color.WHITE);
                ((TextView) layout.findViewById(R.id.k5_txt)).setBackground(getResources().getDrawable(R.drawable.new_border));
                radiustxt = ((TextView) layout.findViewById(R.id.k50_txt)).getText().toString();
            }
        });
        ((TextView) layout.findViewById(R.id.k100_txt)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setBackgroundColor(Color.GRAY);
                ((TextView) layout.findViewById(R.id.kauto_txt)).setBackgroundColor(Color.WHITE);
                ((TextView) layout.findViewById(R.id.kauto_txt)).setBackground(getResources().getDrawable(R.drawable.new_border));
                ((TextView) layout.findViewById(R.id.k50_txt)).setBackgroundColor(Color.WHITE);
                ((TextView) layout.findViewById(R.id.k50_txt)).setBackground(getResources().getDrawable(R.drawable.new_border));
                ((TextView) layout.findViewById(R.id.k15_txt)).setBackgroundColor(Color.WHITE);
                ((TextView) layout.findViewById(R.id.k15_txt)).setBackground(getResources().getDrawable(R.drawable.new_border));
                ((TextView) layout.findViewById(R.id.k5_txt)).setBackgroundColor(Color.WHITE);
                ((TextView) layout.findViewById(R.id.k5_txt)).setBackground(getResources().getDrawable(R.drawable.new_border));
                radiustxt = ((TextView) layout.findViewById(R.id.k100_txt)).getText().toString();

            }
        });
        ((TextView) layout.findViewById(R.id.kauto_txt)).setBackgroundColor(Color.GRAY);
        ((TextView) layout.findViewById(R.id.kauto_txt)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setBackgroundColor(Color.GRAY);
                ((TextView) layout.findViewById(R.id.k100_txt)).setBackgroundColor(Color.WHITE);
                ((TextView) layout.findViewById(R.id.k100_txt)).setBackground(getResources().getDrawable(R.drawable.new_border));
                ((TextView) layout.findViewById(R.id.k50_txt)).setBackgroundColor(Color.WHITE);
                ((TextView) layout.findViewById(R.id.k50_txt)).setBackground(getResources().getDrawable(R.drawable.new_border));
                ((TextView) layout.findViewById(R.id.k15_txt)).setBackgroundColor(Color.WHITE);
                ((TextView) layout.findViewById(R.id.k15_txt)).setBackground(getResources().getDrawable(R.drawable.new_border));
                ((TextView) layout.findViewById(R.id.k5_txt)).setBackgroundColor(Color.WHITE);
                ((TextView) layout.findViewById(R.id.k5_txt)).setBackground(getResources().getDrawable(R.drawable.new_border));
                radiustxt = ((TextView) layout.findViewById(R.id.kauto_txt)).getText().toString();
            }
        });

        return layout;
    }

    private void nearstSearch(int requestId, int currentNo) {
        try {

            if (adapter == null) {
                adapter = new CompanyAdapter(propertUpdates, "");
                mRecyclerView.setAdapter(adapter);
            }

            String Arestext = ((AutoCompleteTextView) layout.findViewById(R.id.get_bustype)).getText().toString();

            if (Arestext.length() == 0) {
                return;
            }

           String radus=radiustxt.replace("km","");
            String link = propertUpdates.getPropertyValue("near_search");
            JSONObject object = new JSONObject();
            object.put("businesstype", Arestext);
            object.put("placeid", place_id);
            object.put("radius", radus.trim());
            HTTPPostTask task = new HTTPPostTask(getActivity(), this);
            //task.disableProgress();
            task.userRequest(getString(R.string.pwait), 3, link, object.toString(), requestId);//
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getLocationData(String place) {

        try {
            // https://maps.googleapis.com/maps/api/place/autocomplete/json?input=(INPUT)&types(TYPE)&sensor=(SENSOR)&key=(KEY)
            String url = AppSettings.getInstance(getActivity()).getPropertyValue("g_location");
            url = url.replace("(INPUT)", place);
            url = url.replace("(SENSOR)", "false");
            url = url.replace("(TYPE)", "geocode");
            url = url.replace("(KEY)", getString(R.string.my_key));

            HTTPTask get = new HTTPTask(getActivity(), this);
            get.disableProgress();
            get.userRequest(getString(R.string.pwait), 2, url);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getBusType() {

        try {
            String url = AppSettings.getInstance(getActivity()).getPropertyValue("bustype");

            JSONObject object = new JSONObject();
            object.put("businesstype", ((AutoCompleteTextView) layout.findViewById(R.id.get_bustype)).getText().toString());


            HTTPostJson post = new HTTPostJson(this, getActivity(), object.toString(), 1);
            post.setContentType("application/json");
            post.execute(url, "");
            // Utils.showProgress(getString(R.string.pwait), getActivity());
        } catch (Exception e) {

        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LinearLayoutManager layoutManager = new LinearLayoutManager(propertUpdates);

        mRecyclerView = (RecyclerView) layout.findViewById(R.id.content_list);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        recycScollListener = new RecyclerOnScrollListener(layoutManager) {

            @Override
            public void onLoadMoreData(int currentPage) {
                nearstSearch(3, 0);
            }
        };
        nearstSearch(2, 0);

//        String address = atvPlaces.getText().toString();
//        GeocodingLocation locationAddress = new GeocodingLocation();
//        locationAddress.getAddressFromLocation(address,
//                getActivity(), new GeocoderHandler());
        turnGPSOn(); // method to turn on the GPS if its in off state.
        getMyCurrentLocation();
        mRecyclerView.addOnScrollListener(recycScollListener);
        setHasOptionsMenu(true);
        getActivity().supportInvalidateOptionsMenu();

    }

//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        super.onHiddenChanged(hidden);
//        if (!hidden)
//            if (isRefresh) {
//                isRefresh = false;
//                nearstSearch(1, 0);
//            }
//    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {

        adapter.clear();
        adapter.notifyDataSetChanged();
        adapter.release();
        adapter = null;

        mRecyclerView.removeAllViews();
        mRecyclerView = null;

        layout = null;

        super.onDestroyView();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        turnGPSOff();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public String getFragmentName() {
        return "Search";// getString(R.string.protections);
    }

    @Override
    public int getFragmentActionBarColor() {
        return R.color.settings_gray;
    }

    @Override
    public void onFragmentChildClick(View view) {


        int itemPosition = mRecyclerView.getChildLayoutPosition(view);

        Item item = adapter.getItems().get(itemPosition);

        propertUpdates.showViewInfo(item);
    }

    @Override
    public void onFinish(Object results, int requestType) {
        try {
            switch (requestType) {

                case 1:
                    JSONObject jsonObject = new JSONObject(results.toString());

                    if (jsonObject.has("status") && jsonObject.optString("status").equalsIgnoreCase("0")) {
                        JSONArray array = jsonObject.getJSONArray("categorynames");
                        if (array != null && array.length() > 0) {
                            list = new ArrayList<String>();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jObject = array.getJSONObject(i);
                                String businesstype = jObject.optString("businesstype");
                                businesstype = businesstype.trim();
                                if (businesstype.length() > 0) {
                                    list.add(businesstype);

                                }
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                                    android.R.layout.select_dialog_item, list);
                            atvBusiness.setThreshold(1);
                            atvBusiness.setAdapter(adapter);
                        }

                    }
                    break;
                case 2:
                    JSONObject jsonOb = new JSONObject(results.toString());
                    JSONArray array = jsonOb.getJSONArray("predictions");
                    if (array != null && array.length() > 0) {
                        list = new ArrayList<String>();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jObject = array.getJSONObject(i);
                            String description = jObject.getString("description");
                            place_id = jObject.getString("place_id");
                            String reference = jObject.getString("reference");
//                            map.setDescription(description);
//                            map.setPlace_id(place_id);
//                            map.setReference(reference);
                            list.add(description);

                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                                android.R.layout.simple_expandable_list_item_1, list);
                        atvPlaces.setThreshold(1);
                        atvPlaces.setAdapter(adapter);
                    }
                    break;
                case 3:
                    if (results != null) {
//                        mSwipeRefreshLayout.setRefreshing(false);
//                        mSwipeRefreshLayout.setEnabled(true);
                        Item item = (Item) results;
                        Vector<Item> items = (Vector<Item>) item.get("company_category_detail");
                        if (items != null && items.size() > 0) {
                            adapter.setItems(items);
                            adapter.notifyDataSetChanged();
                            return;
                        }

                    }
                    adapter.clear();
                    adapter.notifyDataSetChanged();
                    propertUpdates.showToast("No Data Found");
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(String errorCode, int requestType) {
//        mSwipeRefreshLayout.setRefreshing(false);
//        mSwipeRefreshLayout.setEnabled(true);

       // propertUpdates.showToast(errorCode);
        switch (requestType) {

            case 1:

                break;

            case 2:
                break;

            case 3:
                Utils.dismissProgress();
                break;

            default:
                break;
        }

        getView().findViewById(R.id.catgr_pbar).setVisibility(View.GONE);
    }

    public void turnGPSOn() {
        try {

            String provider = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);


            if (!provider.contains("gps")) { //if gps is disabled
                final Intent poke = new Intent();
                poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
                poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
                poke.setData(Uri.parse("3"));
                getActivity().sendBroadcast(poke);
            }
        } catch (Exception e) {

        }
    }

    // Method to turn off the GPS
    public void turnGPSOff() {
        String provider = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if (provider.contains("gps")) { //if gps is enabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            getActivity().sendBroadcast(poke);

        }
    }

    void getMyCurrentLocation() {


        LocationManager locManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        LocationListener locListener = new MyLocationListener();


        try {
            gps_enabled = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
        try {
            network_enabled = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        //don't start listeners if no provider is enabled
        //if(!gps_enabled && !network_enabled)
        //return false;

        if (gps_enabled) {
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);

        }


        if (gps_enabled) {
            location = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);


        }


        if (network_enabled && location == null) {
            locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locListener);

        }


        if (network_enabled && location == null) {
            location = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        }

        if (location != null) {

            MyLat = location.getLatitude();
            MyLong = location.getLongitude();


        } else {
            Location loc = getLastKnownLocation(getActivity());
            if (loc != null) {

                MyLat = loc.getLatitude();
                MyLong = loc.getLongitude();


            }
        }
        locManager.removeUpdates(locListener); // removes the periodic updates from location listener to //avoid battery drainage. If you want to get location at the periodic intervals call this method using //pending intent.

        try {
// Getting address from found locations.
            Geocoder geocoder;

            List<Address> addresses;
            geocoder = new Geocoder(getActivity(), Locale.getDefault());
            addresses = geocoder.getFromLocation(MyLat, MyLong, 1);

            StateName = addresses.get(0).getAdminArea();
            CityName = addresses.get(0).getLocality();
            pincode = addresses.get(0).getPostalCode();
            CountryName = addresses.get(0).getCountryName();
            // you can get more details other than this . like country code, state code, etc.


//            System.out.println(" StateName " + StateName);
//            System.out.println(" CityName " + CityName);
//            System.out.println(" CountryName " + CountryName);
        } catch (Exception e) {
            e.printStackTrace();
        }


        atvPlaces.setText(Html.fromHtml(CityName + "\n"+StateName+ "\n" + pincode+ "\n" + CountryName));
    }

    // Location listener class. to get location.
    public class MyLocationListener implements LocationListener {
        public void onLocationChanged(Location location) {
            if (location != null) {
            }
        }

        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
        }

        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub
        }
    }

    private boolean gps_enabled = false;
    private boolean network_enabled = false;
    Location location;

    Double MyLat, MyLong;
    String CityName = "";
    String StateName = "";
    String CountryName = "";
    String pincode = "";

// below method to get the last remembered location. because we don't get locations all the times .At some instances we are unable to get the location from GPS. so at that moment it will show us the last stored location.

    public static Location getLastKnownLocation(Context context) {
        Location location = null;
        LocationManager locationmanager = (LocationManager) context.getSystemService("location");
        List list = locationmanager.getAllProviders();
        boolean i = false;
        Iterator iterator = list.iterator();
        do {
            //System.out.println("---------------------------------------------------------------------");
            if (!iterator.hasNext())
                break;
            String s = (String) iterator.next();
            //if(i != 0 && !locationmanager.isProviderEnabled(s))
            if (i != false && !locationmanager.isProviderEnabled(s))
                continue;
            // System.out.println("provider ===> "+s);
            Location location1 = locationmanager.getLastKnownLocation(s);
            if (location1 == null)
                continue;
            if (location != null) {
                //System.out.println("location ===> "+location);
                //System.out.println("location1 ===> "+location);
                float f = location.getAccuracy();
                float f1 = location1.getAccuracy();
                if (f >= f1) {
                    long l = location1.getTime();
                    long l1 = location.getTime();
                    if (l - l1 <= 600000L)
                        continue;
                }
            }
            location = location1;
            // System.out.println("location  out ===> "+location);
            //System.out.println("location1 out===> "+location);
            i = locationmanager.isProviderEnabled(s);
            // System.out.println("---------------------------------------------------------------------");
        } while (true);
        return location;
    }


}