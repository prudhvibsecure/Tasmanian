package com.tasmanian.properties.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tasmanian.properties.R;
import com.tasmanian.properties.TasmanianProperty;
import com.tasmanian.properties.common.Item;
import com.tasmanian.properties.utils.MyValidations;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by w7u on 12/14/2016.
 */

public class AddBusContactFragment extends ParentFragment implements View.OnClickListener {

    private TasmanianProperty propertUpdates = null;
    private View layout = null;
    private Item item = null;

    String subcatid,addbus_user_name,addbus_company_name,addbus_comny_title,addbus_des_message,addbus_comny_address,addbus_comny_street,addbus_comny_stname,addbus_comny_late,addbus_comny_lang,fileName,incatid;
    String imagepath=null;
    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        this.propertUpdates = (TasmanianProperty) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        Bundle mArgs = getArguments();
        if (mArgs!=null) {
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
            addbus_comny_late = (String) mArgs.getSerializable("addbus_comny_late");
            addbus_comny_lang = (String) mArgs.getSerializable("addbus_comny_lang");
            fileName = (String) mArgs.getSerializable("fileName");
            imagepath = (String) mArgs.getSerializable("imagepath");

        }

        layout = inflater.inflate(R.layout.addbusiness_screentwo, container, false);

        ((TextView) layout.findViewById(R.id.tv_txt_title)).setVisibility(View.VISIBLE);
        ((TextView) layout.findViewById(R.id.tv_txt_title)).setText("4 of 5");

        ((TextView) layout.findViewById(R.id.add_bus_next_cont)).setOnClickListener(this);
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
    public  void showToast(int text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
    }
    public  void showToast(String text) {
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
        return "Contact Add";
    }

    @Override
    public int getFragmentActionBarColor() {
        return R.color.settings_gray;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_bus_next_cont:
                lastNavigation();
                break;
        }
    }

    private void lastNavigation() {

        String addbus_contact_name = ((EditText) layout.findViewById(R.id.addbus_contact_name)).getText().toString().trim();

        if (addbus_contact_name.length() == 0) {
            showToast("Please Enter Contact Name");
            ((EditText) layout.findViewById(R.id.addbus_contact_name)).requestFocus();
            return;
        }
        if (!isValidName(addbus_contact_name)) {
            showToast(R.string.pechars);
            ((EditText) layout.findViewById(R.id.addbus_contact_name)).requestFocus();
            return;
        }
        if (addbus_contact_name.charAt(0)==' '){
            showToast("Please Enter Contact Name");
            ((EditText) layout.findViewById(R.id.addbus_contact_name)).requestFocus();
            return;
        }
        String addbus_cont_email = ((EditText) layout.findViewById(R.id.addbus_cont_email)).getText().toString().trim();

        if (addbus_cont_email.length() == 0) {
            showToast("Please Enter Email Address");
            ((EditText) layout.findViewById(R.id.addbus_cont_email)).requestFocus();
            return;
        }
        if (!MyValidations.emailValidation(addbus_cont_email)){
            showToast(R.string.peavei);
            ((EditText) layout.findViewById(R.id.addbus_cont_email)).requestFocus();
            return;
        }
        String addbus_cont_phn = ((EditText) layout.findViewById(R.id.addbus_cont_phn)).getText().toString().trim();

        if (addbus_cont_phn.length() == 0) {
            showToast("Please Enter Phone Number");
            ((EditText) layout.findViewById(R.id.addbus_cont_phn)).requestFocus();
            return;
        }
        String addbus_cont_web = ((EditText) layout.findViewById(R.id.addbus_cont_web)).getText().toString().trim();

//        if (addbus_cont_web.length() == 0) {
//            showToast("Please Enter Website");
//            ((EditText) layout.findViewById(R.id.addbus_cont_web)).requestFocus();
//            return;
//        }

        if (!MyValidations.checkURL(addbus_cont_web)){
            showToast("Please Enter Valid Website Url");
            ((EditText) layout.findViewById(R.id.addbus_cont_web)).requestFocus();
            return;
        }

        propertUpdates.updateBusinessFragment=new UpdateBusinessFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable("subcatid", subcatid);
        bundle.putSerializable("addbus_user_name", addbus_user_name);
        bundle.putSerializable("addbus_company_name", addbus_company_name);
        bundle.putSerializable("addbus_comny_title", addbus_comny_title);
        bundle.putSerializable("addbus_des_message", addbus_des_message);
        bundle.putSerializable("addbus_comny_address", addbus_comny_address);
        bundle.putSerializable("addbus_comny_street", addbus_comny_street);
        bundle.putSerializable("addbus_comny_stname", addbus_comny_stname);
        bundle.putSerializable("addbus_contact_name", addbus_contact_name);
        bundle.putSerializable("addbus_cont_email", addbus_cont_email);
        bundle.putSerializable("addbus_cont_phn", addbus_cont_phn);
        bundle.putSerializable("addbus_cont_web", addbus_cont_web);
        bundle.putSerializable("addbus_comny_late", addbus_comny_late);
        bundle.putSerializable("addbus_comny_lang", addbus_comny_lang);
        bundle.putSerializable("fileName", fileName);
        bundle.putSerializable("imagepath", imagepath);
        bundle.putSerializable("incatid", incatid);

        propertUpdates.updateBusinessFragment.setArguments(bundle);
        propertUpdates.swiftFragments(propertUpdates.updateBusinessFragment,"updateBusinessFragment");
    }
    private boolean isValidName(String name) {
        String NAME_PATTERN ="[a-zA-z]*(['\\s]+[a-z]*)*";

        Pattern pattern = Pattern.compile(NAME_PATTERN);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }
}
