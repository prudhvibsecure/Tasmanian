package com.tasmanian.properties.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tasmanian.properties.R;
import com.tasmanian.properties.TasmanianProperty;
import com.tasmanian.properties.common.Item;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by w7u on 12/14/2016.
 */

public class AddCompanyFragment extends ParentFragment implements View.OnClickListener {

    private TasmanianProperty propertUpdates = null;
    private View layout = null;
    private Item item = null;

    GoogleMap googleMap;
    MarkerOptions markerOptions;
    LatLng latLng;
    private double latitude, longitude;

    private String imagepath = null, fileName = "";

    private JSONObject jsonobject = null;

    private JSONArray jsonarray = null;

    private ImageView imageview;

    private Bitmap bitmap = null;

    private Dialog dialog;

    private static final LatLng TASMANIAA = new LatLng(-41.640079, 146.315918);
    private LatLngBounds  TASMANIA = new LatLngBounds(
            new LatLng(-41.640079, 146.315918), new LatLng(-41.640079, 146.315918));

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        this.propertUpdates = (TasmanianProperty) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        Bundle mArgs = getArguments();

        item = (Item) mArgs.getSerializable("item");
        layout = inflater.inflate(R.layout.addbusiness_screenone, container, false);

        ((TextView) layout.findViewById(R.id.tv_txt_title)).setVisibility(View.VISIBLE);
        ((TextView) layout.findViewById(R.id.tv_txt_title)).setText("3 of 5");

        imageview = (ImageView)layout. findViewById(R.id.compny_img);
        imageview.setScaleType(ImageView.ScaleType.FIT_XY);
        ((TextView) layout.findViewById(R.id.add_bus_next_comp)).setOnClickListener(this);

        layout.findViewById(R.id.compny_img).setOnClickListener(this);
       // layout.findViewById(R.id.addbus_comny_address).setOnClickListener(this);


        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.map);

        // Getting a reference to the map
        googleMap = supportMapFragment.getMap();
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);

// Constrain the camera target to the Adelaide bounds.
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(TASMANIAA, 5));

// Zoom in, animating the camera.
        googleMap.animateCamera(CameraUpdateFactory.zoomIn());

// Zoom out to zoom level 10, animating with a duration of 2 seconds.
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(7), 500, null);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(TASMANIAA)      // Sets the center of the map to Mountain View
                .zoom(7)                   // Sets the zoom
                .bearing(70)                // Sets the orientation of the camera to east
                .tilt(20)                   // Sets the tilt of the camera to 30 degrees
                .build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        GroundOverlayOptions newarkMap = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.tas_country))
                .position(TASMANIAA, 8600f, 6500f);

// Add an overlay to the map, retaining a handle to the GroundOverlay object.
        GroundOverlay imageOverlay = googleMap.addGroundOverlay(newarkMap);
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

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

                // Fill color of the circle
                // 0x represents, this is an hexadecimal code
                // 55 represents percentage of transparency. For 100% transparency, specify 00.
                // For 0% transparency ( ie, opaque ) , specify ff
                // The remaining 6 characters(00ff00) specify the fill color
                circleOptions.fillColor(0x5500ff00);

                // Border width of the circle
                circleOptions.strokeWidth(2);

                googleMap.addCircle(circleOptions);

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
                new AddCompanyFragment.ReverseGeocodingTask(getActivity()).execute(latLng);

            }
        });

        setHasOptionsMenu(true);

        getActivity().supportInvalidateOptionsMenu();

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void showToast(int text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
    }

    public void showToast(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {

        layout = null;

        super.onDestroyView();
    }

    @Override
    public void onDestroy() {

        imageview.setImageBitmap(null);
        bitmap = null;
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onFragmentChildClick(View view) {
        super.onFragmentChildClick(view);
    }

    @Override
    public String getFragmentName() {
        return "Company Add";
    }

    @Override
    public int getFragmentActionBarColor() {
        return R.color.settings_gray;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_bus_next_comp:
                nextScreen();
                break;
            case R.id.compny_img:
                brwoseFile();
                break;
//            case R.id.addbus_comny_address:
//                showMaps();
//                break;
            default:
                break;
        }

    }


    private void brwoseFile() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, 1000);


    }

    public void nextScreen() {

        String addbus_user_name = ((EditText) layout.findViewById(R.id.addbus_user_name)).getText().toString().trim();

        if (addbus_user_name.length() == 0) {
            showToast(R.string.peyun);
            ((EditText) layout.findViewById(R.id.addbus_user_name)).requestFocus();
            return;
        }
        if (!isValidName(addbus_user_name)){
            showToast(R.string.pechars);
            ((EditText) layout.findViewById(R.id.addbus_user_name)).requestFocus();
            return;
        }
        if (addbus_user_name.charAt(0)==' '){
            showToast(R.string.peyun);
            ((EditText) layout.findViewById(R.id.addbus_user_name)).requestFocus();
            return;
        }
        String addbus_company_name = ((EditText) layout.findViewById(R.id.addbus_company_name)).getText().toString().trim();

        if (addbus_company_name.length() == 0) {
            showToast("Please Enter Company Name");
            ((EditText) layout.findViewById(R.id.addbus_company_name)).requestFocus();
            return;
        }
        String addbus_comny_title = ((EditText) layout.findViewById(R.id.addbus_comny_title)).getText().toString().trim();

//        if (addbus_comny_title.length() == 0) {
//            showToast("Please Enter Company Title");
//            ((EditText) layout.findViewById(R.id.addbus_comny_title)).requestFocus();
//            return;
//        }
        String addbus_des_message = ((EditText) layout.findViewById(R.id.addbus_des_message)).getText().toString().trim();

        if (addbus_des_message.length() == 0) {
            showToast("Please Enter Your Description");
            ((EditText) layout.findViewById(R.id.addbus_des_message)).requestFocus();
            return;
        }

        String addbus_comny_address = ((EditText) layout.findViewById(R.id.addbus_comny_address)).getText().toString().trim();

        if (addbus_comny_address.length() == 0) {
            showToast("Please Enter Address");
            ((EditText) layout.findViewById(R.id.addbus_comny_address)).requestFocus();
            return;
        }
        String addbus_comny_street = ((EditText) layout.findViewById(R.id.addbus_comny_street)).getText().toString().trim();

        if (addbus_comny_street.length() == 0) {
            showToast("Please Enter Street Number");
            ((EditText) layout.findViewById(R.id.addbus_comny_street)).requestFocus();
            return;
        }
        String addbus_comny_stname = ((EditText) layout.findViewById(R.id.addbus_comny_stname)).getText().toString().trim();

        if (addbus_comny_stname.length() == 0) {
            showToast("Please Enter Street Name");
            ((EditText) layout.findViewById(R.id.addbus_comny_stname)).requestFocus();
            return;
        }

        String addbus_comny_late = String.valueOf(latitude);
        String addbus_comny_lang = String.valueOf(longitude);

        String image_path=((TextView) layout.findViewById(R.id.image_path)).getText().toString();
        if (image_path.length()==0){
            showToast("Please Select Company Logo");
            return;
        }

        if (imagepath != null && imagepath.length() > 0) {
            fileName = System.currentTimeMillis() + "_" + fileName;
        }

        propertUpdates.addBusContactFragment = new AddBusContactFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable("subcatid", item.getAttribute("subcatid"));
        bundle.putSerializable("incatid", item.getAttribute("incatid"));
        bundle.putSerializable("addbus_user_name", addbus_user_name);
        bundle.putSerializable("addbus_company_name", addbus_company_name);
        bundle.putSerializable("addbus_comny_title", addbus_comny_title);
        bundle.putSerializable("addbus_des_message", addbus_des_message);
        bundle.putSerializable("addbus_comny_address", addbus_comny_address);
        bundle.putSerializable("addbus_comny_street", addbus_comny_street);
        bundle.putSerializable("addbus_comny_stname", addbus_comny_stname);
        bundle.putSerializable("addbus_comny_stname", addbus_comny_stname);
        bundle.putSerializable("addbus_comny_late", addbus_comny_late);
        bundle.putSerializable("addbus_comny_lang", addbus_comny_lang);
        bundle.putSerializable("fileName", fileName);
        bundle.putSerializable("imagepath", imagepath);


        propertUpdates.addBusContactFragment.setArguments(bundle);
        propertUpdates.swiftFragments(propertUpdates.addBusContactFragment, "addBusContactFragment");

    }
    private boolean isValidName(String name) {
        String NAME_PATTERN ="[a-zA-z]*(['\\s]+[a-z]*)*";

        Pattern pattern = Pattern.compile(NAME_PATTERN);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
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
            ((EditText) layout.findViewById(R.id.addbus_comny_address)).setText(addressText.toString());
            // Placing a marker on the touched position
            googleMap.addMarker(markerOptions);

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {

            if (resultCode == getActivity().RESULT_OK) {
                JSONObject object = processData(data);

                if (object == null) {
                    showToast("Please select Company Logo");
                    return;
                }
                imagepath = object.optString("data");
                fileName = object.optString("displayname");
                bitmap = decodeFile(new File(imagepath), 2);

                if (bitmap != null) {

                    bitmap = getRoundedShape(bitmap);

                    imageview.setScaleType(ImageView.ScaleType.FIT_XY);

                    imageview.setImageBitmap(bitmap);


                    ((TextView) layout.findViewById(R.id.image_path)).setText(fileName);


                }


            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private Bitmap decodeFile(File f, int photoToLoad) {
        try {
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);
            int REQUIRED_SIZE = 70;

            if (photoToLoad == 1)
                REQUIRED_SIZE = 120;

            if (photoToLoad == 2 || photoToLoad == 5)
                REQUIRED_SIZE = 200;

            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;

            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);

        } catch (FileNotFoundException e) {
        }
        return null;
    }

    public Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
        int targetWidth = 200;
        int targetHeight = 200;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2, ((float) targetHeight - 1) / 2,
                (Math.min(((float) targetWidth), ((float) targetHeight)) / 2), Path.Direction.CCW);

        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap, new Rect(0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight()),
                new Rect(0, 0, targetWidth, targetHeight), null);
        return targetBitmap;
    }
    private JSONObject processData(Intent intent) {
        try {

            if (intent == null) {
                return null;
            }

            Uri uri = intent.getData();
            if (uri == null) {
                return null;
            }

            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                JSONObject object = new JSONObject();
                do {
                    String[] resultsColumns = cursor.getColumnNames();
                    for (int i = 0; i < resultsColumns.length; i++) {
                        String key = resultsColumns[i];

                        if (key.equalsIgnoreCase("com.google.android.apps.photos.api.special_type_id"))
                            key = "_id";

                        String value = cursor.getString(cursor.getColumnIndexOrThrow(
                                key/* resultsColumns[i] */));

                        if (value != null) {
                            if (key.contains("_"))
                                key = key.replace("_", "");
                            object.put(key, value);
                        }
                    }

                } while (cursor.moveToNext());

                cursor.close();
                cursor = null;

                return object;
            }

            return null;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }


}
