package com.tasmanian.properties;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tasmanian.properties.callbacks.IItemHandler;
import com.tasmanian.properties.common.AppSettings;
import com.tasmanian.properties.tasks.HTTPostJson;
import com.tasmanian.properties.utils.MyValidations;
import com.tasmanian.properties.utils.Utils;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by w7u on 12/3/2016.
 */

public class MemberRegistration extends FragmentActivity implements IItemHandler, OnClickListener {

    GoogleMap googleMap;
    MarkerOptions markerOptions;
    LatLng latLng;
    private double latitude, longitude;

    private EditText user_name, password, Firstname, LastName, Email, phone, address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_signup);

        user_name = (EditText) findViewById(R.id.et_user_name);
        password = (EditText) findViewById(R.id.et_user_pwd);
        Firstname = (EditText) findViewById(R.id.et_user_fname);
        LastName = (EditText) findViewById(R.id.et_user_lname);
        Email = (EditText) findViewById(R.id.et_user_email);
        phone = (EditText) findViewById(R.id.et_user_phone);
        address = (EditText) findViewById(R.id.et_user_address);

        findViewById(R.id.sign_up_btn).setOnClickListener(this);

        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);

        // Getting a reference to the map
        googleMap = supportMapFragment.getMap();
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);

        // Setting a click event handler for the map
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng arg0) {

                // Getting the Latitude and Longitude of the touched location
                latLng = arg0;

                // Clears the previously touched position
                googleMap.clear();

                // Animating to the touched position
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

//                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
//                // Zoom in, animating the camera.
//                googleMap.animateCamera(CameraUpdateFactory.zoomIn());
                // Zoom out to zoom level 10, animating with a duration of 2 seconds.
//                googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 50, null);
                // Creating a marker
                markerOptions = new MarkerOptions();

                // Setting the position for the marker
                markerOptions.position(latLng);

                // Placing a marker on the touched position
                googleMap.addMarker(markerOptions);

                // Adding Marker on the touched location with address
                new MemberRegistration.ReverseGeocodingTask(getBaseContext()).execute(latLng);

            }
        });
        // findViewById(R.id.et_user_address).setOnClickListener(this);
    }

    @Override
    public void onFinish(Object results, int requestType) {

        try {
            switch (requestType) {
                case 1:
                    Utils.dismissProgress();
                    parseRegistrationResponse((String) results, requestType);
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(String errorCode, int requestType) {

    }

    private class ReverseGeocodingTask extends AsyncTask<LatLng, Void, String> {
        Context mContext;

        public ReverseGeocodingTask(Context context) {
            super();
            mContext = context;
        }

        // Finding address using reverse geocoding
        @Override
        protected String doInBackground(LatLng... params) {
            Geocoder geocoder = new Geocoder(mContext);
            latitude = params[0].latitude;
            longitude = params[0].longitude;

            List<Address> addresses = null;
            String addressText = "";

            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);

                addressText = String.format("%s, %s, %s",
                        address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                        address.getLocality(),
                        address.getCountryName());
            }

            return addressText;
        }

        @Override
        protected void onPostExecute(String addressText) {
            // Setting the title for the marker.
            // This will be displayed on taping the marker
            markerOptions.title(addressText);
            address.setText(addressText.toString());
            // Placing a marker on the touched position
            googleMap.addMarker(markerOptions);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_up_btn:
                makeRegisterUser();
                break;

        }

    }

    private void makeRegisterUser() {
        try {

            String et_uname = ((EditText) findViewById(R.id.et_user_name)).getText().toString().trim();

            if (et_uname.length() == 0) {
                showToast("Please Enter Username");
                ((EditText) findViewById(R.id.et_user_name)).requestFocus();
                return;
            }

            String et_pwd = ((EditText) findViewById(R.id.et_user_pwd)).getText().toString().trim();

            if (et_pwd.length() == 0) {
                showToast("Please Enter Password");
                ((EditText) findViewById(R.id.et_user_pwd)).requestFocus();
                return;
            }
            if (et_pwd.length() < 8 || et_pwd.length() > 16) {
                showToast(R.string.psmbc);
                ((EditText) findViewById(R.id.et_user_pwd)).requestFocus();
                return;
            }
            String et_fname = ((EditText) findViewById(R.id.et_user_fname)).getText().toString().trim();

            if (et_fname.length() == 0) {
                showToast("Please Enter First Name");
                ((EditText) findViewById(R.id.et_user_fname)).requestFocus();
                return;
            }
            if (!isValidName(et_fname)) {
                showToast(R.string.pechars);
                ((EditText) findViewById(R.id.et_user_fname)).requestFocus();
                return;
            }

            String et_lname = ((EditText) findViewById(R.id.et_user_lname)).getText().toString().trim();

            if (et_lname.length() == 0) {
                showToast("Please Enter Last Name");
                ((EditText) findViewById(R.id.et_user_lname)).requestFocus();
                return;
            }
            if (!isValidName(et_lname)) {
                showToast(R.string.pechars);
                ((EditText) findViewById(R.id.et_user_lname)).requestFocus();
                return;
            }
            String et_address = ((EditText) findViewById(R.id.et_user_address)).getText().toString().trim();

            if (et_address.length() == 0) {
                showToast("Please Select Map get Address");
                ((EditText) findViewById(R.id.et_user_address)).requestFocus();
                return;
            }
            String et_email = ((EditText) findViewById(R.id.et_user_email)).getText().toString().trim();

            if (et_email.length() == 0) {
                showToast("Please Enter Email Address");
                ((EditText) findViewById(R.id.et_user_email)).requestFocus();
                return;
            }
            if (!MyValidations.emailValidation(et_email)) {
                showToast("Please Enter Valid Email Address");
                ((EditText) findViewById(R.id.et_user_email)).requestFocus();
                return;
            }
            String et_phone = ((EditText) findViewById(R.id.et_user_phone)).getText().toString().trim();

            if (et_phone.length() == 0) {
                showToast("Please Enter Contact Number");
                ((EditText) findViewById(R.id.et_user_phone)).requestFocus();
                return;
            }
            String mylat=String.valueOf(latitude);
            String mylang=String.valueOf(longitude);

            String url = AppSettings.getInstance(this).getPropertyValue("register");

            JSONObject object = new JSONObject();
            object.put("username", et_uname);
            object.put("password", et_pwd);
            object.put("fname", et_fname);
            object.put("lname", et_lname);
            object.put("address", et_address);
            object.put("latitude", mylat);
            object.put("longitude", mylang);
            object.put("email", et_email);
            object.put("phoneno", et_phone);


            HTTPostJson post = new HTTPostJson(this, this, object.toString(), 1);
            post.setContentType("application/json");
            post.execute(url, "");
            Utils.showProgress(getString(R.string.pwait), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private boolean isValidName(String name) {
        String NAME_PATTERN ="[a-zA-z]*(['\\s]+[a-z]*)*";

        Pattern pattern = Pattern.compile(NAME_PATTERN);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }
    public void showToast(int text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
    private void parseRegistrationResponse(String response, int requestId) throws Exception {

        if (response != null && response.length() > 0) {
            response = response.trim();
            JSONObject jsonObject = new JSONObject(response);

            if (jsonObject.has("status") && jsonObject.optString("status").equalsIgnoreCase("0")) {
                showToast(jsonObject.optString("statusdescription"));
                MemberRegistration.this.finish();
                return;
            }
            showToast(jsonObject.optString("statusdescription"));

        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }
}
