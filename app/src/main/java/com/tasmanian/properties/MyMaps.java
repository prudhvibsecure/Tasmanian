package com.tasmanian.properties;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MyMaps extends AppCompatActivity implements View.OnClickListener {

    GoogleMap googleMap;
    MarkerOptions markerOptions;
    LatLng latLng;
    private double latitude, longitude;
    private String addressText = "",username,password,firstname,lastname,email,contact;
    private static final LatLng TASMANIAA = new LatLng(-41.640079, 146.315918);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usermaps);


        Intent intent=getIntent();
        if (intent!=null){

            username=intent.getStringExtra("uname");
            password=intent.getStringExtra("pwd");
            firstname=intent.getStringExtra("fname");
            lastname=intent.getStringExtra("lname");
            email=intent.getStringExtra("email");
            contact=intent.getStringExtra("phone");

        }

        findViewById(R.id.get_loc).setOnClickListener(this);
        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);

        // Getting a reference to the map
        googleMap = supportMapFragment.getMap();
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(false);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        // Setting a click event handler for the map
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(TASMANIAA, 5));

        // Zoom in, animating the camera.
        googleMap.animateCamera(CameraUpdateFactory.zoomIn());

        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(8), 500, null);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(TASMANIAA)      // Sets the center of the map to Mountain View
                .zoom(8)                   // Sets the zoom
                .bearing(70)                // Sets the orientation of the camera to east
                .tilt(20)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        GroundOverlayOptions newarkMap = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.tas_country))
                .position(TASMANIAA, 8600f, 6500f);

// Add an overlay to the map, retaining a handle to the GroundOverlay object.
        GroundOverlay imageOverlay = googleMap.addGroundOverlay(newarkMap);
        googleMap.setOnMapClickListener(new OnMapClickListener() {

            @Override
            public void onMapClick(LatLng arg0) {

                // Getting the Latitude and Longitude of the touched location
                latLng = arg0;

                // Clears the previously touched position
                googleMap.clear();
                CircleOptions circleOptions = new CircleOptions();

                // Specifying the center of the circle
                circleOptions.center(arg0);

                // Radius of the circle
                circleOptions.radius(50);

                // Border color of the circle
                circleOptions.strokeColor(Color.BLACK);

                circleOptions.fillColor(0x5500ff00);

                // Border width of the circle
                circleOptions.strokeWidth(2);
                googleMap.addCircle(circleOptions);
                // Animating to the touched position
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                // Creating a marker
                markerOptions = new MarkerOptions();

                // Setting the position for the marker
                markerOptions.position(latLng);

                // Placing a marker on the touched position
                googleMap.addMarker(markerOptions);

                // Adding Marker on the touched location with address
                new ReverseGeocodingTask(getBaseContext()).execute(latLng);

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_loc:

                if (addressText.toString().equals("")) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Tasmanian")
                            .setMessage("Sorry,We do not serve this location.Please select another area.")
                            .setCancelable(false).setIcon(R.drawable.ic_launcher)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            });

                    builder.create().show();

                }
                else {
                    Intent register=new Intent(MyMaps.this,Register.class);
                    register.putExtra("address",addressText);
                    register.putExtra("username",username);
                    register.putExtra("password",password);
                    register.putExtra("firstname",firstname);
                    register.putExtra("lastname",lastname);
                    register.putExtra("email",email);
                    register.putExtra("contact",contact);
                    register.putExtra("latitude",latitude);
                    register.putExtra("longitude",longitude);
                    startActivity(register);
                    finish();
                }
                break;
        }

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
            ((TextView)findViewById(R.id.loc_txt)).setText(addressText);
            // Placing a marker on the touched position
            googleMap.addMarker(markerOptions);

        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent register=new Intent(MyMaps.this,Register.class);
        register.putExtra("address",addressText);
        register.putExtra("username",username);
        register.putExtra("password",password);
        register.putExtra("firstname",firstname);
        register.putExtra("lastname",lastname);
        register.putExtra("email",email);
        register.putExtra("contact",contact);
        startActivity(register);
        finish();
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

}
