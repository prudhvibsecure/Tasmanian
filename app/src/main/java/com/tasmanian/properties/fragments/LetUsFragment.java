package com.tasmanian.properties.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import com.tasmanian.properties.TasmanianProperty;
import com.tasmanian.properties.R;
import com.tasmanian.properties.callbacks.IItemHandler;
import com.tasmanian.properties.common.AppSettings;
import com.tasmanian.properties.models.Categories;
import com.tasmanian.properties.tasks.HTTPTask;
import com.tasmanian.properties.tasks.HTTPostJson;
import com.tasmanian.properties.utils.MyValidations;
import com.tasmanian.properties.utils.Utils;

import java.util.ArrayList;

/**
 * Created by w7 on 22/08/2016.
 */
public class LetUsFragment extends ParentFragment implements IItemHandler {

    private TasmanianProperty propertUpdates = null;
    private View layout = null;
    private Spinner spin_category;
    private JSONObject jsonobject = null;

    private JSONArray jsonarray = null;
    private ArrayList<Categories> categories;
    public String cat_id;



    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        this.propertUpdates = (TasmanianProperty) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        layout = inflater.inflate(R.layout.contactus, container, false);
        spin_category = (Spinner)layout. findViewById(R.id.spinner_category);
        layout.findViewById(R.id.sub_letus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateLetUs();
            }
        });

        HTTPTask task = new HTTPTask(getActivity(), this);
        task.disableProgress();
        task.userRequest("", 2, AppSettings.getInstance(getActivity()).getPropertyValue("categories"));
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true);

        getActivity().supportInvalidateOptionsMenu();
    }
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        if (!propertUpdates.isDrawerOpen()) {
//            inflater.inflate(R.menu.search, menu);
////            inflater.inflate(R.menu.menu_main, menu);
//        }
//    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        switch (item.getItemId()) {
//
//            case R.id.contact_submit:
//               UpdateConatct();
//                break;
//
//            default:
//                break;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    private void UpdateLetUs() {

        try {

            String name = ((EditText) layout.findViewById(R.id.user_name)).getText().toString().trim();

            if (name.length() == 0) {
                showToast(R.string.peyun);
                ((EditText) layout.findViewById(R.id.user_name)).requestFocus();
                return;
            }

            String email = ((EditText) layout.findViewById(R.id.user_email)).getText().toString().trim();

            if (email.length() == 0) {
                showToast(R.string.peei);
                ((EditText) layout.findViewById(R.id.user_email)).requestFocus();
                return;
            }
            if (!MyValidations.emailValidation(email)) {
                showToast(R.string.peavei);
                ((EditText) layout.findViewById(R.id.user_email)).requestFocus();
                return;
            }
            Categories selectedcat = (Categories) spin_category.getSelectedItem();
            String catgory = selectedcat.getCname();
            if (catgory.length() == 0) {
                showToast("Please Select Category");
                ((Spinner)layout.findViewById(R.id.spinner_category)).requestFocus();
                return;
            }
            String message = ((EditText) layout.findViewById(R.id.user_message)).getText().toString().trim();

            if (message.length() == 0) {
                showToast(R.string.messages);
                ((EditText) layout.findViewById(R.id.user_message)).requestFocus();
                return;
            }
            String url = AppSettings.getInstance(getActivity()).getPropertyValue("enqury");
            JSONObject object = new JSONObject();
            object.put("name", name);
            object.put("eemail", email);
            object.put("categories", catgory);
            object.put("requirements", message);

            HTTPostJson post = new HTTPostJson(this, getActivity(), object.toString(), 1);
            post.setContentType("application/json");
            post.execute(url, "");
            Utils.showProgress(getString(R.string.pwait), getActivity());
        }catch (Exception e){

        }

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
        return "Let Us Help";
    }

    @Override
    public int getFragmentActionBarColor() {
        return R.color.settings_gray;
    }
    @Override
    public void onFinish(Object results, int requestType) {


        Utils.dismissProgress();

        try {

            switch (requestType) {
                case 1:
                    parseContactResponse((String) results, requestType);
                    break;
                case 2:
                    if (results == null)
                        return;

                    jsonobject = new JSONObject(results.toString());

                    categories = new ArrayList<Categories>();

                    try {

                        jsonarray = jsonobject.getJSONArray("category_detail");

                        int position = -1;

                        for (int i = 0; i < jsonarray.length(); i++) {
                            jsonobject = jsonarray.getJSONObject(i);

                            Categories category = new Categories();

                            category.setCid(jsonobject.optString("catid"));
                            category.setCname(jsonobject.optString("categoryname"));

                            if (jsonobject.optString("catid").equalsIgnoreCase(cat_id)) {
                                position = i;
                            }

                            categories.add(category);
                        }

                        spin_category.setAdapter(new ArrayAdapter<Categories>(getActivity(),
                                R.layout.spinner_row_nothing_selected, categories));

                        spin_category.setSelection(position);

                        spin_category.setPrompt("Select Category");
                    }catch (Exception e){
                        e.printStackTrace();
                    }


                default:
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void parseContactResponse(String response, int requestType) {
        Log.e("-=-=-=-=-=-=-=-=-", response + "");

        try {
            if (response != null && response.length() > 0) {
                response = response.trim();
                JSONObject jsonObject = new JSONObject(response);

                if (jsonObject.has("status") && jsonObject.optString("status").equalsIgnoreCase("0")) {
                    showToast(jsonObject.optString("status_description"));
                    propertUpdates.onKeyDown(4, null);
                    jsonObject=null;
                    return;
                }

                showToast(jsonObject.optString("status_description"));

            }
        }catch (Exception e){

        }
    }

    @Override
    public void onError(String errorCode, int requestType) {

    }
}
