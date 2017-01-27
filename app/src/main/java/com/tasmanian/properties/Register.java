package com.tasmanian.properties;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tasmanian.properties.callbacks.IItemHandler;
import com.tasmanian.properties.common.AppSettings;
import com.tasmanian.properties.tasks.HTTPostJson;
import com.tasmanian.properties.utils.MyValidations;
import com.tasmanian.properties.utils.Utils;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by w7u on 12/30/2016.
 */

public class Register extends AppCompatActivity implements IItemHandler, View.OnClickListener {

    private Double latitude, longitude;
    private TextView str;

    String address_txt,user_name_txt, password_txt, Firstname_txt, LastName_txt, Email_txt, phone_txt;

    private EditText user_name, password, Firstname, LastName, Email, phone, address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_register);

        user_name = (EditText) findViewById(R.id.et_user_name);
        password = (EditText) findViewById(R.id.et_user_pwd);
        Firstname = (EditText) findViewById(R.id.et_user_fname);
        LastName = (EditText) findViewById(R.id.et_user_lname);
        Email = (EditText) findViewById(R.id.et_user_email);
        phone = (EditText) findViewById(R.id.et_user_phone);
        address = (EditText) findViewById(R.id.et_user_address);
        str = (TextView) findViewById(R.id.pass_change);

        Intent intent=getIntent();
        if (intent!=null){

            address_txt=intent.getStringExtra("address");
            user_name_txt=intent.getStringExtra("username");
            password_txt=intent.getStringExtra("password");
            Firstname_txt=intent.getStringExtra("firstname");
            LastName_txt=intent.getStringExtra("lastname");
            Email_txt=intent.getStringExtra("email");
            phone_txt=intent.getStringExtra("contact");
            latitude=intent.getDoubleExtra("latitude",0.00);
            longitude=intent.getDoubleExtra("longitude",0.00);

            address.setText(address_txt);
            user_name.setText(user_name_txt);
            password.setText(password_txt);
            Firstname.setText(Firstname_txt);
            LastName.setText(LastName_txt);
            Email.setText(Email_txt);
            phone.setText(phone_txt);


        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_my_back);
        toolbar.setTitle("Member Registration");
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.sign_up_btn).setOnClickListener(this);
        findViewById(R.id.et_user_address).setOnClickListener(this);

        password.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() == 0) {
                    str.setText("Password Empty");

                } else if (s.length() < 4) {
                    str.setText("Weak Password");

                } else if (s.length() < 6) {
                    str.setText("Easy Password");

                } else if (s.length() < 9) {
                    str.setText("Strong Password");

                } else
                    str.setText("Very Strong Password");


                if (s.length() == 16)
                    str.setText("Password Max Length Reached");

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                Register.this.finish();
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_up_btn:
                makeRegisterUser();
                break;
            case R.id.et_user_address:
                Intent maps=new Intent(Register.this,MyMaps.class);

                maps.putExtra("uname",((EditText) findViewById(R.id.et_user_name)).getText().toString().trim());
                maps.putExtra("pwd",((EditText) findViewById(R.id.et_user_pwd)).getText().toString().trim());
                maps.putExtra("fname",((EditText) findViewById(R.id.et_user_fname)).getText().toString().trim());
                maps.putExtra("lname",((EditText) findViewById(R.id.et_user_lname)).getText().toString().trim());
                maps.putExtra("email",((EditText) findViewById(R.id.et_user_email)).getText().toString().trim());
                maps.putExtra("phone",((EditText) findViewById(R.id.et_user_phone)).getText().toString().trim());
                startActivity(maps);
                finish();
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
            String mylat = String.valueOf(latitude);
            String mylang = String.valueOf(longitude);

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
        String NAME_PATTERN = "[a-zA-z]*(['\\s]+[a-z]*)*";

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
                Register.this.finish();
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

