package com.tasmanian.properties.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tasmanian.properties.R;
import com.tasmanian.properties.TasmanianProperty;
import com.tasmanian.properties.callbacks.IDownloadCallback;
import com.tasmanian.properties.callbacks.IItemHandler;
import com.tasmanian.properties.common.AppSettings;
import com.tasmanian.properties.common.Item;
import com.tasmanian.properties.tasks.FileUploader;
import com.tasmanian.properties.tasks.HTTPostJson;
import com.tasmanian.properties.utils.MyValidations;
import com.tasmanian.properties.utils.Utils;

import org.json.JSONObject;

/**
 * Created by w7u on 12/14/2016.
 */

public class UpdateBusinessFragment extends ParentFragment implements IItemHandler, View.OnClickListener,IDownloadCallback {

    private TasmanianProperty propertUpdates = null;
    private View layout = null;
    private Item item = null;
    String subcatid, addbus_user_name, addbus_company_name, addbus_comny_title, addbus_des_message, addbus_comny_address, addbus_comny_street, addbus_comny_stname;

    String addbus_contact_name, addbus_cont_email, addbus_cont_phn, addbus_cont_web,addbus_comny_late,addbus_comny_lang,fileName;
    private FileUploader uploader = null;

    private String imagepath=null,incatid;
    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        this.propertUpdates = (TasmanianProperty) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle mArgs = getArguments();
        if (mArgs != null) {
            item = (Item) mArgs.getSerializable("item");
            subcatid = (String) mArgs.getSerializable("subcatid");
            incatid = (String) mArgs.getSerializable("incatid");
            addbus_user_name = (String) mArgs.getSerializable("addbus_user_name");
            addbus_company_name = (String) mArgs.getSerializable("addbus_company_name");
            addbus_comny_title = (String) mArgs.getSerializable("addbus_comny_title");
            addbus_des_message = (String) mArgs.getSerializable("addbus_des_message");
            addbus_comny_address = (String) mArgs.getSerializable("addbus_comny_address");
            addbus_comny_street = (String) mArgs.getSerializable("addbus_comny_street");
            addbus_comny_stname = (String) mArgs.getSerializable("addbus_comny_stname");
            addbus_contact_name = (String) mArgs.getSerializable("addbus_contact_name");
            addbus_cont_email = (String) mArgs.getSerializable("addbus_cont_email");
            addbus_cont_phn = (String) mArgs.getSerializable("addbus_cont_phn");
            addbus_cont_web = (String) mArgs.getSerializable("addbus_cont_web");
            addbus_comny_late = (String) mArgs.getSerializable("addbus_comny_late");
            addbus_comny_lang = (String) mArgs.getSerializable("addbus_comny_lang");
            fileName = (String) mArgs.getSerializable("fileName");
            imagepath = (String) mArgs.getSerializable("imagepath");
        }
        layout = inflater.inflate(R.layout.addbusiness_screenthree, container, false);

        ((TextView) layout.findViewById(R.id.tv_txt_title)).setVisibility(View.VISIBLE);
        ((TextView) layout.findViewById(R.id.tv_txt_title)).setText("5 of 5");

        ((TextView) layout.findViewById(R.id.add_bus_finish)).setOnClickListener(this);
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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
        return "Add Business Update";
    }

    @Override
    public int getFragmentActionBarColor() {
        return R.color.settings_gray;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_bus_finish:
                makeRequestAddBusiness();
                if (fileName != null) {
                    uploadCompanyFile();
                }
                break;
        }
    }
    private void uploadCompanyFile() {

        String url = AppSettings.getInstance(getActivity()).getPropertyValue("upload_file");

        uploader = new FileUploader(getActivity(), this);
        uploader.setFileName("", fileName);
        uploader.userRequest("", 1, url, imagepath);
    }
    private void makeRequestAddBusiness() {

        try {

            String addbus_fb = ((EditText) layout.findViewById(R.id.addbus_fb)).getText().toString().trim();
//            if (!addbus_fb.equals("https://www.facebook.com/")) {
//                showToast("Please Enter Facebook Url");
//                ((EditText) layout.findViewById(R.id.addbus_fb)).requestFocus();
//                return;
//            }
            if (!MyValidations.checkURL(addbus_fb)){
                showToast("Please Enter Facebook Url");
                ((EditText) layout.findViewById(R.id.addbus_cont_web)).requestFocus();
                return;
            }
//            if (addbus_fb.length() == 0) {
//                showToast("Please Enter Facebook Url");
//                ((EditText) layout.findViewById(R.id.addbus_fb)).requestFocus();
//                return;
//            }
            String addbus_twt = ((EditText) layout.findViewById(R.id.addbus_twt)).getText().toString().trim();

//            if (!addbus_twt.equals("https://twitter.com/")) {
//                showToast("Please Enter Twitter Url");
//                ((EditText) layout.findViewById(R.id.addbus_twt)).requestFocus();
//                return;
//            }
            if (!MyValidations.checkURL(addbus_twt)){
                showToast("Please Enter Twitter Url");
                ((EditText) layout.findViewById(R.id.addbus_cont_web)).requestFocus();
                return;
            }
//            if (addbus_twt.length() == 0) {
//                showToast("Please Enter Twitter Url");
//                ((EditText) layout.findViewById(R.id.addbus_twt)).requestFocus();
//                return;
//            }
            String addbus_gplus = ((EditText) layout.findViewById(R.id.addbus_gplus)).getText().toString().trim();

//            if (!addbus_gplus.equals("https://plus.google.com/")) {
//                showToast("Please Enter Googleplus Url");
//                ((EditText) layout.findViewById(R.id.addbus_gplus)).requestFocus();
//                return;
//            }
            if (!MyValidations.checkURL(addbus_twt)){
                showToast("Please Enter Googleplus Url");
                ((EditText) layout.findViewById(R.id.addbus_cont_web)).requestFocus();
                return;
            }
//            if (addbus_gplus.length() == 0) {
//                showToast("Please Enter Googleplus Url");
//                ((EditText) layout.findViewById(R.id.addbus_gplus)).requestFocus();
//                return;
//            }

            String url = AppSettings.getInstance(getActivity()).getPropertyValue("add_business");
            JSONObject object = new JSONObject();
            object.put("username", addbus_user_name);
            object.put("companyname", addbus_company_name);
            object.put("description", addbus_des_message);
            object.put("contactname", addbus_contact_name);
            object.put("streetno", addbus_comny_street);
            object.put("streetname", addbus_comny_stname);
            object.put("longitude", addbus_comny_lang);
            object.put("latitude", addbus_comny_late);
            object.put("email", addbus_cont_email);
            object.put("facebook", addbus_fb);
            object.put("twitter", addbus_twt);
            object.put("gplus", addbus_gplus);
            object.put("website", addbus_cont_web);
            object.put("companyslogan", addbus_comny_title);
            object.put("phoneno", addbus_cont_phn);
            object.put("subcatid", subcatid);
            object.put("incatid", incatid);
            object.put("company_logo", fileName);

            HTTPostJson post = new HTTPostJson(this, getActivity(), object.toString(), 2);
            post.setContentType("application/json");
            post.execute(url, "");
            Utils.showProgress(getString(R.string.pwait), getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFinish(Object results, int requestType) {

        Utils.dismissProgress();

        try {

            switch (requestType) {
                case 2:
                    parseBusinessResponse((String) results, requestType);
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

    }

    private void parseBusinessResponse(String response, int requestType) {
        Log.e("-=-=-=-=-=-=-=-=-", response + "");

        try {
            if (response != null && response.length() > 0) {
                response = response.trim();
                JSONObject jsonObject = new JSONObject(response);

                if (jsonObject.has("status") && jsonObject.optString("status").equalsIgnoreCase("0")) {
                    showToast(jsonObject.optString("statusdescription"));
                    Intent myIntent = new Intent(getActivity(), TasmanianProperty.class);
                    startActivity(myIntent);
                    getActivity().finish();
                    //propertUpdates.onKeyDown(4, null);

                    jsonObject=null;
                    return;
                }

                showToast(jsonObject.optString("statusdescription"));

            }
        }catch (Exception e){

        }
    }

    @Override
    public void onStateChange(int what, int arg1, int arg2, Object obj, int requestId) {

        try {

            switch (what) {

                case -1: // failed

                    showToast(obj + "");
                    uploader = null;

                    break;

                case 1: // progressBar

                    break;

                case 0: // success

                    //showToast("Company Logo Updated");
                    uploader = null;
                    break;

                default:
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
