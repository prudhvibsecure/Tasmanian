package com.tasmanian.properties.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tasmanian.properties.R;
import com.tasmanian.properties.TasmanianProperty;

/**
 * Created by w7u on 1/17/2017.
 */

public class ContactUsfr extends ParentFragment implements View.OnClickListener {

    private TasmanianProperty propertUpdates = null;
    private View layout = null;

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        this.propertUpdates = (TasmanianProperty) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.contactfr, container, false);
        layout.findViewById(R.id.tv_email).setOnClickListener(this);
        layout.findViewById(R.id.tv_mobiler_office).setOnClickListener(this);
        layout.findViewById(R.id.tv_mobiler_offic).setOnClickListener(this);
        layout.findViewById(R.id.tv_mobiler).setOnClickListener(this);
        layout.findViewById(R.id.facebook_circle).setOnClickListener(this);
        layout.findViewById(R.id.youtube).setOnClickListener(this);
        layout.findViewById(R.id.instagram).setOnClickListener(this);
        ((TextView) layout.findViewById(R.id.tv_data)).setText(Html.fromHtml("<b>Office Address:</b> <br/>\n" +
                "Level 2, 118 Murray Street,<br/>\n" +
                "Hobart 7000 Tasmania<br/><br/>\n" +
                "\n" +
                "<b>Display Homes Location:</b> <br/>\n" +
                "Olive Grove<br/>\n" +
                "Geilston Bay<br/>\n" +
                "Kingston<br/>\n" +
                "Warrane<br/>"));
        return layout;
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.tv_email:
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("text/html");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"info@tasmanian.properties"});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(emailIntent);
                break;
            case R.id.tv_mobiler_office:
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:0406565422"));
                startActivity(intent);
                break;
            case R.id.tv_mobiler:
                Intent intent1 = new Intent(Intent.ACTION_CALL, Uri.parse("tel:0466068777"));
                startActivity(intent1);
                break;
            case R.id.youtube:
                Intent youtube = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCzsOMp2Yuf7i0Aen3NLyugw"));
                startActivity(youtube);
                break;
            case R.id.facebook_circle:
                Intent fb = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/tasmanianproperties/"));
                startActivity(fb);
                break;
            case R.id.tv_mobiler_offic:
                Intent intent2 = new Intent(Intent.ACTION_CALL, Uri.parse("tel:0467069035"));
                startActivity(intent2);
                break;
            case R.id.instagram:
                Intent instagram = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/tasmanianproperties/"));
                startActivity(instagram);
                break;
            default:
                break;
        }

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
        return "Contact Us";
    }

    @Override
    public int getFragmentActionBarColor() {
        return R.color.settings_gray;
    }
}
