package com.tasmanian.properties.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.tasmanian.properties.R;
import com.tasmanian.properties.TasmanianProperty;
import com.tasmanian.properties.callbacks.IItemHandler;
import com.tasmanian.properties.common.AppSettings;
import com.tasmanian.properties.tasks.HTTPostJson;
import com.tasmanian.properties.utils.MyValidations;
import com.tasmanian.properties.utils.Utils;

import org.json.JSONObject;

/**
 * Created by w7u on 12/9/2016.
 */

public class NewsFragment extends ParentFragment implements IItemHandler {

    private TasmanianProperty propertUpdates = null;
    private View layout = null;

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        this.propertUpdates = (TasmanianProperty) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        layout = inflater.inflate(R.layout.news_submit_layout, container, false);

        layout.findViewById(R.id.sub_newsdata).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateLetUs();
            }
        });
        return layout;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true);

        getActivity().supportInvalidateOptionsMenu();
    }

    private void UpdateLetUs() {

        try {

            String email = ((EditText) layout.findViewById(R.id.user_news_message)).getText().toString().trim();

            if (email.length() == 0) {
                showToast(R.string.peei);
                ((EditText) layout.findViewById(R.id.user_news_message)).requestFocus();
                return;
            }
            if (!MyValidations.emailValidation(email)) {
                showToast(R.string.peavei);
                ((EditText) layout.findViewById(R.id.user_news_message)).requestFocus();
                return;
            }


            String url = AppSettings.getInstance(getActivity()).getPropertyValue("news_data");
            JSONObject object = new JSONObject();
            object.put("nemail", email);

            HTTPostJson post = new HTTPostJson(this, getActivity(), object.toString(), 1);
            post.setContentType("application/json");
            post.execute(url, "");
            Utils.showProgress(getString(R.string.pwait), getActivity());
        } catch (Exception e) {

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
                    parseNewsResponse((String) results, requestType);
                    break;

                default:
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void parseNewsResponse(String response, int requestType) {
        Log.e("-=-=-=-=-=-=-=-=-", response + "");

        try {
            if (response != null && response.length() > 0) {
                response = response.trim();
                JSONObject jsonObject = new JSONObject(response);

                if (jsonObject.has("status") && jsonObject.optString("status").equalsIgnoreCase("0")) {
                    showToast(jsonObject.optString("status_description"));
                    propertUpdates.onKeyDown(4, null);
                    jsonObject = null;
                    return;
                }

                showToast(jsonObject.optString("status_description"));

            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onError(String errorCode, int requestType) {

    }
}
