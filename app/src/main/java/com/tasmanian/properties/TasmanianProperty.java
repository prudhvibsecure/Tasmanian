package com.tasmanian.properties;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tasmanian.properties.callbacks.IItemHandler;
import com.tasmanian.properties.common.Constants;
import com.tasmanian.properties.common.Item;
import com.tasmanian.properties.fragments.AddBusContactFragment;
import com.tasmanian.properties.fragments.AddBusinessFragment;
import com.tasmanian.properties.fragments.AddCompanyFragment;
import com.tasmanian.properties.fragments.AddSubcatFragment;
import com.tasmanian.properties.fragments.BlogNewsFragment;
import com.tasmanian.properties.fragments.BuildingsFragment;
import com.tasmanian.properties.fragments.BusinessInnerFragment;
import com.tasmanian.properties.fragments.CategoryDetailFragment;
import com.tasmanian.properties.fragments.ContactUsfr;
import com.tasmanian.properties.fragments.DisAbilityFragment;
import com.tasmanian.properties.fragments.FinanceFragment;
import com.tasmanian.properties.fragments.GardenFragment;
import com.tasmanian.properties.fragments.HomeFragment;
import com.tasmanian.properties.fragments.InnerCatCoFragment;
import com.tasmanian.properties.fragments.InteriorFragment;
import com.tasmanian.properties.fragments.LegalFragment;
import com.tasmanian.properties.fragments.LetUsFragment;
import com.tasmanian.properties.fragments.MySearchFragment;
import com.tasmanian.properties.fragments.NewsFragment;
import com.tasmanian.properties.fragments.ParentFragment;
import com.tasmanian.properties.fragments.PrSuplyFragment;
import com.tasmanian.properties.fragments.SponserFragment;
import com.tasmanian.properties.fragments.SubCatFragment;
import com.tasmanian.properties.fragments.SustainFragment;
import com.tasmanian.properties.fragments.TradsFragment;
import com.tasmanian.properties.fragments.UpdateBusinessFragment;
import com.tasmanian.properties.tasks.HTTPostJson;
import com.tasmanian.properties.utils.Utils;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TasmanianProperty extends AppCompatActivity
        implements OnClickListener, IItemHandler {


    private FragmentManager manager = null;

    private HomeFragment homeFragment = null;

    private CategoryDetailFragment categoryDetailFragment = null;

    private TradsFragment tradsFragment = null;

    private SustainFragment sustainFragment = null;

    private DisAbilityFragment disAbilityFragment = null;

    private SubCatFragment subCatFragment = null;

    private LetUsFragment letUsFragment = null;

    private GardenFragment gardenFragment = null;

    private LegalFragment legalFragment = null;

    private SponserFragment sponserFragment = null;

    private PrSuplyFragment prSuplyFragment = null;

    private FinanceFragment financeFragment = null;

    private InteriorFragment interiorFragment = null;

    private BuildingsFragment buildingsFragment = null;

    private NewsFragment newsFragment = null;

    private AddBusinessFragment addBusinessFragment = null;

    public AddSubcatFragment addSubcatFragment = null;

    public AddCompanyFragment addCompanyFragment = null;

    public AddBusContactFragment addBusContactFragment = null;

    public UpdateBusinessFragment updateBusinessFragment = null;

    private InnerCatCoFragment innerCatCoFragment = null;

    private BusinessInnerFragment businessInnerFragment = null;

    public MySearchFragment mySearchFragment = null;

    private BlogNewsFragment blogNewsFragment=null;

    private ContactUsfr contactUsfr=null;

    private Properties properties = null;

    private Stack<ParentFragment> fragStack = null;

    private DrawerLayout drawerLayout = null;

    private Toolbar toolbar;

    private ActionBar actionBar = null;

    private ActionBarDrawerToggle mDrawerToggle;

    private NavigationView navigationView = null;

    private ImageView iv_profilePic = null;

    private View drawerHeader = null;

    public boolean commitFlag = true;

    private Dialog dialog;

    private Item item = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.propertyupdate);


        loadProperties();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //toolbar.setTitleTextColor(0xFFFFFFFF);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        fragStack = new Stack<ParentFragment>();

        getSupportActionBar().getThemedContext();

        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,

                R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                supportInvalidateOptionsMenu();
            }

        };

        drawerLayout.setDrawerListener(mDrawerToggle);

        actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayUseLogoEnabled(false);

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        toolbar.setSubtitle(getFromStore("username"));
        toolbar.setSubtitleTextColor(Color.BLACK);
        //actionBar.setTitle("Property Update");

        drawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerToggle.setDrawerIndicatorEnabled(true);

        manager = getSupportFragmentManager();

        if (savedInstanceState != null) {
            temp();
        }

        homeFragment = new HomeFragment();

        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.container, homeFragment, "homeFragment");

        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
        fragStack.push(homeFragment);

        navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                onNavigationDrawerItemSelected(menuItem.getItemId());
                return true;
            }
        });

        drawerHeader = navigationView.inflateHeaderView(R.layout.nav_header);
        iv_profilePic = (ImageView) drawerHeader.findViewById(R.id.iv_profilePic);
        // getUserData();

    }

    private void getUserData() {

//        String user_name=getFromStore("username");
//        if (user_name.length()==0) {
//            ((TextView) drawerHeader.findViewById(R.id.tv_username)).setText("Welcome" + " " + "Guest");
//        }else {
//            ((TextView) drawerHeader.findViewById(R.id.tv_username)).setText("Welcome" + " " + user_name);
//        }
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }


    public void onNavigationDrawerItemSelected(int menuId) {

		/*
         * if (position == R.id.m_logout) { addToStore("fromExit", "exit");
		 * return; }
		 */

        int count = fragStack.size();
        while (count > 1) {

            ParentFragment pf = fragStack.remove(count - 1);

            FragmentTransaction trans = manager.beginTransaction();
            trans.remove(pf);
            trans.commit();
            categoryDetailFragment = null;
            subCatFragment = null;
            letUsFragment = null;
            sponserFragment = null;
            legalFragment = null;
            prSuplyFragment = null;
            disAbilityFragment = null;
            sustainFragment = null;
            gardenFragment = null;
            tradsFragment = null;
            financeFragment = null;
            interiorFragment = null;
            buildingsFragment = null;
            newsFragment = null;
            addBusinessFragment = null;
            addSubcatFragment = null;
            addCompanyFragment = null;
            addBusContactFragment = null;
            updateBusinessFragment = null;
            businessInnerFragment = null;
            mySearchFragment = null;
            blogNewsFragment=null;
            contactUsfr=null;

            count--;
        }

        switch (menuId) {

            case R.id.m_home: // home

                swiftFragments(homeFragment, "homeFragment");

                break;

            case R.id.m_grden: // Garden and outdoors
                if (gardenFragment == null)
                    gardenFragment = new GardenFragment();

                swiftFragments(gardenFragment, "gardenFragment");

                break;
            case R.id.m_legal: // Legal
                if (legalFragment == null)
                    legalFragment = new LegalFragment();

                swiftFragments(legalFragment, "legalFragment");

                break;

            case R.id.m_finance: // Finance
                if (financeFragment == null)
                    financeFragment = new FinanceFragment();

                swiftFragments(financeFragment, "financeFragment");

                break;

            case R.id.m_trads: // Trads
                if (tradsFragment == null)
                    tradsFragment = new TradsFragment();

                swiftFragments(tradsFragment, "tradsFragment");

                break;
            case R.id.m_intern: // Interior
                if (interiorFragment == null)
                    interiorFragment = new InteriorFragment();

                swiftFragments(interiorFragment, "interiorFragment");

                break;
            case R.id.m_sustain: // Sistainbility
                if (sustainFragment == null)
                    sustainFragment = new SustainFragment();

                swiftFragments(sustainFragment, "sustainFragment");


                break;
            case R.id.m_disblityhm: // Disablity
                if (disAbilityFragment == null)
                    disAbilityFragment = new DisAbilityFragment();

                swiftFragments(disAbilityFragment, "disAbilityFragment");

                break;
            case R.id.m_prosup: // Products&supplies
                if (prSuplyFragment == null)
                    prSuplyFragment = new PrSuplyFragment();

                swiftFragments(prSuplyFragment, "prSuplyFragment");

                break;
            case R.id.m_builds: // Builds
                if (buildingsFragment == null)
                    buildingsFragment = new BuildingsFragment();

                swiftFragments(buildingsFragment, "buildingsFragment");

                break;
            case R.id.m_luhlp: // LetUsHelp

                if (letUsFragment == null)
                    letUsFragment = new LetUsFragment();

                swiftFragments(letUsFragment, "letUsFragment");

                break;

            case R.id.m_newsl: // newslatter
                if (newsFragment == null)
                    newsFragment = new NewsFragment();
                swiftFragments(newsFragment, "newsFragment");
                break;

            case R.id.m_sponser:
                if (sponserFragment == null)
                    sponserFragment = new SponserFragment();

                swiftFragments(sponserFragment, "sponserFragment");
                break;
            case R.id.m_adbus: // addBusiness
                if (addBusinessFragment == null)
                    addBusinessFragment = new AddBusinessFragment();
                swiftFragments(addBusinessFragment, "addBusinessFragment");

                break;

            case R.id.m_blog: // addBusiness
                if (blogNewsFragment == null)
                    blogNewsFragment = new BlogNewsFragment();
                swiftFragments(blogNewsFragment, "blogNewsFragment");

                break;
            case R.id.m_contact: // addBusiness
                if (contactUsfr == null)
                    contactUsfr = new ContactUsfr();
                swiftFragments(contactUsfr, "contactUsfr");

                break;

            default:
                break;
        }

    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*
         * if (!mNavigationDrawerFragment.isDrawerOpen()) { restoreActionBar();
		 * return true; }
		 */
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (drawerLayout != null)
                    drawerLayout.openDrawer(GravityCompat.START);
                return true;

            case R.id.login_menu:
                showLoginDialog();
                break;
            case R.id.logout_menu:
                logoutAlert();
                break;
            case R.id.profile_view:
//                Intent profile = new Intent(this, User_Profile.class);
//                startActivity(profile);
                break;
            case R.id.change_pwd_view:
                showChangPwdDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (menu.findItem(R.id.login_menu) != null) {
            String uid = getFromStore("username");
            if (uid.length() == 0) {
                menu.findItem(R.id.login_menu).setVisible(true);
                menu.findItem(R.id.logout_menu).setVisible(false);
                menu.findItem(R.id.profile_view).setVisible(false);
                menu.findItem(R.id.change_pwd_view).setVisible(false);
            } else {
                menu.findItem(R.id.login_menu).setVisible(false);
                menu.findItem(R.id.logout_menu).setVisible(true);
                menu.findItem(R.id.profile_view).setVisible(false);
                menu.findItem(R.id.change_pwd_view).setVisible(true);
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * loadProperties - Loads properties file from raw folder.
     */
    private void loadProperties() {
        try {
            InputStream rawResource = getResources().openRawResource(R.raw.settings);
            properties = new Properties();
            properties.load(rawResource);
            rawResource.close();
            rawResource = null;
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Properties getProperties() {
        return properties;
    }

    public String getPropertyValue(String key) {
        return properties.getProperty(key);
    }

    public void showToast(String value) {
        Toast.makeText(this, value, Toast.LENGTH_SHORT).show();
    }

    public void showToast(int text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public void onClick(View view) {

        switch (view.getId()) {


            case R.id.rv_row_listing:
                try {
                    fragStack.peek().onFragmentChildClick(view);
                }catch (Exception e){
                    showToast("Too Much Action Performed");
                    return;
                }
                break;
            case R.id.get_laocation_sr:

                mySearchFragment=new MySearchFragment();
                Bundle bundle = new Bundle();
                mySearchFragment.setArguments(bundle);
                swiftFragments(mySearchFragment,"mySearchFragment");
                break;
            default:
                break;
        }
    }

    private void makeRequsetLogin() {
        try {

            String user_name = ((EditText) dialog.findViewById(R.id.user_name)).getText().toString();
            if (user_name.length() == 0) {
                showToast(R.string.peyun);
                ((EditText) dialog.findViewById(R.id.user_name)).requestFocus();
                return;
            }

            String user_pass = ((EditText) dialog.findViewById(R.id.user_password)).getText().toString();
            if (user_pass.length() == 0) {
                showToast(R.string.pePswd);
                ((EditText) dialog.findViewById(R.id.user_password)).requestFocus();
                return;
            }
//            if (user_pass.length() < 8 || user_pass.length() > 16) {
//                showToast(R.string.psmbc);
//                ((EditText) dialog.findViewById(R.id.user_password)).requestFocus();
//                return;
//            }
            String login_url = properties.getProperty("user_login");

            JSONObject object = new JSONObject();
            object.put("username", user_name);
            object.put("password", user_pass);

            HTTPostJson post = new HTTPostJson(this, this, object.toString(), 1);
            post.setContentType("application/json");
            post.execute(login_url, "");
            Utils.showProgress(getString(R.string.pwait), this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void makeReqChangePwd() {
        try {

            String old_password = ((EditText) dialog.findViewById(R.id.user_old_pwd)).getText().toString();
            if (old_password.length() == 0) {
                showToast("Please Enter Old Password");
                ((EditText) dialog.findViewById(R.id.user_old_pwd)).requestFocus();
                return;
            }
            String new_password = ((EditText) dialog.findViewById(R.id.user_new_pwd)).getText().toString();
            if (new_password.length() == 0) {
                showToast("Please Enter New Password");
                ((EditText) dialog.findViewById(R.id.user_new_pwd)).requestFocus();
                return;
            }

            String login_url = properties.getProperty("forget_pass");

            JSONObject object = new JSONObject();
            object.put("username", getFromStore("username"));
            object.put("oldpassword", old_password);
            object.put("newpassword", new_password);

            HTTPostJson post = new HTTPostJson(this, this, object.toString(), 2);
            post.setContentType("application/json");
            post.execute(login_url, "");
            Utils.showProgress(getString(R.string.pwait), this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void makeRequsetFogetpassword() {
        try {

            String user_name = ((EditText) dialog.findViewById(R.id.user_forget_name)).getText().toString();
            if (user_name.length() == 0) {
                showToast(R.string.peyun);
                ((EditText) dialog.findViewById(R.id.user_forget_name)).requestFocus();
                return;
            }

            String login_url = properties.getProperty("forget_pass");

            JSONObject object = new JSONObject();
            object.put("username", user_name);

            HTTPostJson post = new HTTPostJson(this, this, object.toString(), 2);
            post.setContentType("application/json");
            post.execute(login_url, "");
            Utils.showProgress(getString(R.string.pwait), this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void swiftFragments(ParentFragment frag, String tag) {

        FragmentTransaction trans = manager.beginTransaction();
        if (frag.isAdded() && frag.isVisible())
            return;
        else if (frag.isAdded() && frag.isHidden()) {

            trans.hide(fragStack.get(fragStack.size() - 1));
            trans.show(frag);

        } else if (!frag.isAdded()) {

            ParentFragment pf = fragStack.get(fragStack.size() - 1);
            trans.hide(pf);

            trans.add(R.id.container, frag, tag);
            trans.show(frag);
        }

        trans.commitAllowingStateLoss();
        trans = null;

        getSupportActionBar().setTitle(frag.getFragmentName());

		/*
         * getSupportActionBar().setBackgroundDrawable(new
		 * ColorDrawable(frag.getFragmentActionBarColor()));
		 * updateStatusBarColor(frag.getFragmentStatusBarColor());
		 */

        if (!(frag instanceof HomeFragment))
            fragStack.push((ParentFragment) frag);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {

            // if (mNavigationDrawerFragment.isDrawerOpen()) {
            // mNavigationDrawerFragment.close();
            // return true;
            // }

            if (fragStack.size() > 1) {

                ParentFragment pf = fragStack.peek();

                if (pf.back() == true)
                    return true;

                fragStack.pop();

                FragmentTransaction trans = manager.beginTransaction();
                trans.remove(pf);

                ParentFragment pf1 = fragStack.get(fragStack.size() - 1);
                trans.show(pf1);

                if (commitFlag)
                    trans.commit();
                else
                    trans.commitAllowingStateLoss();

                getSupportActionBar().setTitle(pf1.getFragmentName());

				/*
                 * getSupportActionBar().setBackgroundDrawable(new
				 * ColorDrawable(pf1.getFragmentActionBarColor()));
				 * updateStatusBarColor(pf1.getFragmentStatusBarColor());
				 */

                return true;
            }

            return super.onKeyDown(keyCode, event);

        }
        return super.onKeyDown(keyCode, event);
    }

    OnClickListener onclick = new OnClickListener() {

        @Override
        public void onClick(View view) {

            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(view.getWindowToken(), 0);

            switch (view.getId()) {

                case R.id.home:
                    swiftFragments(homeFragment, "homeFragment");

                    break;
                case R.id.submit_btn:
                    makeRequsetLogin();
                    break;
                case R.id.tv_fogetpass:
                    closeDialog();
                    showFogetPassword();
                    break;

                case R.id.submit_btn_forget:
                    makeRequsetFogetpassword();
                    break;
                case R.id.submit_btn_change:
                    makeReqChangePwd();
                    break;
                case R.id.tv_member_register:
                    closeDialog();
                    Intent register = new Intent(getApplicationContext(), Register.class);
                    startActivity(register);
                    overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                    break;
                default:
                    break;
            }

        }
    };

    @Override
    public void onFinish(Object results, int requestType) {

        try {

            switch (requestType) {
                case 1:
                    Utils.dismissProgress();
                    parseLoginResponse((String) results, requestType);
                    break;
                case 2:
                    Utils.dismissProgress();
                    parseFogetpwdResponse((String) results, requestType);
                    break;
                case 3:
                    Utils.dismissProgress();
                    parseChangepwdResponse((String) results, requestType);
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
        showToast(errorCode);
        closeDialog();
        switch (requestType) {

            case 1:
                Utils.dismissProgress();
                break;

            case 2:
                break;

            default:
                break;
        }
    }

    public FragmentManager getFragManager() {
        return manager;
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {

            case 1001:
                break;

            case 2002:

                if (resultCode == RESULT_CANCELED) {
                    onKeyDown(4, null);
                    return;
                }

                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        addToStore("inactivity", "no");

        clearCache();

    }

    @Override
    protected void onStart() {
        super.onStart();
        addToStore("inactivity", "yes");
    }

    private void clearCache() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    deleteAppFiles(new File(Constants.CACHETEMP));
                } catch (Exception e) {
                    // TODO: handle exception

                }
                try {
                    File mydir = getDir("Downloads", Context.MODE_PRIVATE);
                    deleteAppFiles(mydir);
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        }).start();
    }

    /**
     * deleteAppFiles - This method is called on canceling a request.
     *
     * @param - file name to be deleted
     **/
    private boolean deleteAppFiles(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteAppFiles(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return (path.delete());
    }


    public boolean emailValidation(String email) {

        if (email == null || email.length() == 0 || email.indexOf("@") == -1 || email.indexOf(" ") != -1) {
            return false;
        }
        int emailLenght = email.length();
        int atPosition = email.indexOf("@");

        String beforeAt = email.substring(0, atPosition);
        String afterAt = email.substring(atPosition + 1, emailLenght);

        if (beforeAt.length() == 0 || afterAt.length() == 0) {
            return false;
        }
        if (email.charAt(atPosition - 1) == '.') {
            return false;
        }
        if (email.charAt(atPosition + 1) == '.') {
            return false;
        }
        if (afterAt.indexOf(".") == -1) {
            return false;
        }
        char dotCh = 0;
        for (int i = 0; i < afterAt.length(); i++) {
            char ch = afterAt.charAt(i);
            if ((ch == 0x2e) && (ch == dotCh)) {
                return false;
            }
            dotCh = ch;
        }
        if (afterAt.indexOf("@") != -1) {
            return false;
        }
        int ind = 0;
        do {
            int newInd = afterAt.indexOf(".", ind + 1);

            if (newInd == ind || newInd == -1) {
                String prefix = afterAt.substring(ind + 1);
                if (prefix.length() > 1 && prefix.length() < 6) {
                    break;
                } else {
                    return false;
                }
            } else {
                ind = newInd;
            }
        } while (true);
        dotCh = 0;
        for (int i = 0; i < beforeAt.length(); i++) {
            char ch = beforeAt.charAt(i);
            if (!((ch >= 0x30 && ch <= 0x39) || (ch >= 0x41 && ch <= 0x5a) || (ch >= 0x61 && ch <= 0x7a) || (ch == 0x2e)
                    || (ch == 0x2d) || (ch == 0x5f))) {
                return false;
            }
            if ((ch == 0x2e) && (ch == dotCh)) {
                return false;
            }
            dotCh = ch;
        }
        return true;
    }

    public boolean isDrawerOpen() {
        return false;// mNavigationDrawerFragment.isDrawerOpen();
    }

    @Override
    public void onResume() {
        super.onResume();
        // getUserData();

    }


    public void lockMode() {
        // mNavigationDrawerFragment.lockMode();
    }

    public void unLockMode() {
        // mNavigationDrawerFragment.unLockMode();
    }

    public String getFromStore(String key) {
        return getSharedPreferences("Tasmanian", 0).getString(key, "");
    }

    public void addToStore(String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences("Tasmanian", 0).edit();
        editor.putString(key, value);
        editor.commit();
    }

    private void temp() {

        FragmentTransaction trans = manager.beginTransaction();

        if (manager.findFragmentByTag("homeFragment") != null) {
            trans.remove(manager.findFragmentByTag("homeFragment"));
        }

        if (manager.findFragmentByTag("subcategoryFragment") != null) {
            trans.remove(manager.findFragmentByTag("subcategoryFragment"));
        }

        if (manager.findFragmentByTag("letUsFragment") != null) {
            trans.remove(manager.findFragmentByTag("letUsFragment"));
        }
        if (manager.findFragmentByTag("sponserFragment") != null) {
            trans.remove(manager.findFragmentByTag("sponserFragment"));
        }
        if (manager.findFragmentByTag("legalFragment") != null) {
            trans.remove(manager.findFragmentByTag("legalFragment"));
        }
        if (manager.findFragmentByTag("prSuplyFragment") != null) {
            trans.remove(manager.findFragmentByTag("prSuplyFragment"));
        }
        if (manager.findFragmentByTag("disAbilityFragment") != null) {
            trans.remove(manager.findFragmentByTag("disAbilityFragment"));
        }
        if (manager.findFragmentByTag("sustainFragment") != null) {
            trans.remove(manager.findFragmentByTag("sustainFragment"));
        }
        if (manager.findFragmentByTag("gardenFragment") != null) {
            trans.remove(manager.findFragmentByTag("gardenFragment"));
        }
        if (manager.findFragmentByTag("tradsFragment") != null) {
            trans.remove(manager.findFragmentByTag("tradsFragment"));
        }
        if (manager.findFragmentByTag("financeFragment") != null) {
            trans.remove(manager.findFragmentByTag("financeFragment"));
        }
        if (manager.findFragmentByTag("interiorFragment") != null) {
            trans.remove(manager.findFragmentByTag("interiorFragment"));
        }
        if (manager.findFragmentByTag("interiorFragment") != null) {
            trans.remove(manager.findFragmentByTag("interiorFragment"));
        }
        if (manager.findFragmentByTag("buildingsFragment") != null) {
            trans.remove(manager.findFragmentByTag("buildingsFragment"));
        }
        if (manager.findFragmentByTag("addBusinessFragment") != null) {
            trans.remove(manager.findFragmentByTag("addBusinessFragment"));
        }
        if (manager.findFragmentByTag("addCompanyFragment") != null) {
            trans.remove(manager.findFragmentByTag("addCompanyFragment"));
        }
        if (manager.findFragmentByTag("blogNewsFragment") != null) {
            trans.remove(manager.findFragmentByTag("blogNewsFragment"));
        }
        if (manager.findFragmentByTag("contactUsfr") != null) {
            trans.remove(manager.findFragmentByTag("contactUsfr"));
        }

        trans.commit();
        trans = null;


    }

    public void showViewSubcatPage(Item item) {
        if (categoryDetailFragment == null)
            categoryDetailFragment = new CategoryDetailFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable("item", item);
        categoryDetailFragment.setArguments(bundle);

        swiftFragments(categoryDetailFragment, "categoryDetailFragment");


    }

    public void addBusshowViewSubcatPage(Item item) {
        if (addSubcatFragment == null)
            addSubcatFragment = new AddSubcatFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable("item", item);
        addSubcatFragment.setArguments(bundle);

        swiftFragments(addSubcatFragment, "addSubcatFragment");


    }

    public void addBusscompanyPage(Item item) {
        if (addCompanyFragment == null)
            addCompanyFragment = new AddCompanyFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable("item", item);
        addCompanyFragment.setArguments(bundle);

        swiftFragments(addCompanyFragment, "addCompanyFragment");


    }

    public void showAddBusinessSubcatList(Item item) {
        if (businessInnerFragment == null)
            businessInnerFragment = new BusinessInnerFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable("item", item);
        businessInnerFragment.setArguments(bundle);
        swiftFragments(businessInnerFragment, "businessInnerFragment");


    }

    public void showSubcatList(Item item) {
        if (subCatFragment == null)
            subCatFragment = new SubCatFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable("item", item);
        subCatFragment.setArguments(bundle);
        swiftFragments(subCatFragment, "subCatFragment");


    }

    public void bloggerData(Item item){
        Intent intent = new Intent(this, BloggerData.class);
        String newsdate = item.getAttribute("newsdate");
        String title = item.getAttribute("title");
        String description = item.getAttribute("description");
        String newsimage = item.getAttribute("newsimage");

        intent.putExtra("newsdate", newsdate);
        intent.putExtra("title", title);
        intent.putExtra("description", description);
        intent.putExtra("newsimage", newsimage);
        startActivity(intent);
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }

    public void showComanysInner(Item item) {
        if (innerCatCoFragment == null)
            innerCatCoFragment = new InnerCatCoFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable("item", item);
        innerCatCoFragment.setArguments(bundle);
        swiftFragments(innerCatCoFragment, "innerCatCoFragment");


    }

    public void showViewInfo(Item item) {
        Intent intent = new Intent(this, TcompanyDetails.class);
        String cid = item.getAttribute("cid");
        intent.putExtra("cid", cid);
        startActivity(intent);
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }

    public void showLoginDialog() {

        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.login_user);
        dialog.findViewById(R.id.submit_btn).setOnClickListener(onclick);
        dialog.findViewById(R.id.tv_member_register).setOnClickListener(onclick);
        dialog.findViewById(R.id.tv_fogetpass).setOnClickListener(onclick);
        dialog.show();

    }

    public void showFogetPassword() {

        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.user_forget_password);
        dialog.findViewById(R.id.submit_btn_forget).setOnClickListener(onclick);
        dialog.show();

    }

    public void showChangPwdDialog() {

        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.change_password);
        dialog.findViewById(R.id.submit_btn_change).setOnClickListener(onclick);
        dialog.show();

    }

    public void logoutAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Do you want to Logout?");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addToStore("username", "");
                //showToast(R.string.ylss);
//                startActivity(getIntent());
//                TasmanianProperty.this.finish();

            }
        });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                closeDialog();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    private void closeDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
        dialog = null;
    }

    private void parseLoginResponse(String response, int requestType) throws Exception {
        if (response != null && response.length() > 0) {

            JSONObject jsonObject = new JSONObject(response);

            if (jsonObject.optString("status").equalsIgnoreCase("0")) {
                showToast(jsonObject.optString("statusdescription"));
                addToStore("username", jsonObject.optString("username"));
                closeDialog();
//                    Intent ss = new Intent(this, TasmanianProperty.class);
//                    startActivity(ss);
//                    TasmanianProperty.this.finish();

                return;
            }
            showToast(jsonObject.optString("statusdescription"));
        }

    }

    private void parseFogetpwdResponse(String response, int requestType) throws Exception {
        if (response != null && response.length() > 0) {

            JSONObject jsonObject = new JSONObject(response);

            if (jsonObject.optString("status").equalsIgnoreCase("0")) {
                showToast(jsonObject.optString("statusdescription"));
                closeDialog();

                return;
            }
            showToast(jsonObject.optString("statusdescription"));
        }

    }

    private void parseChangepwdResponse(String response, int requestType) throws Exception {
        if (response != null && response.length() > 0) {

            JSONObject jsonObject = new JSONObject(response);

            if (jsonObject.optString("status").equalsIgnoreCase("0")) {
                showToast(jsonObject.optString("statusdescription"));
                closeDialog();

                return;
            }
            showToast(jsonObject.optString("statusdescription"));
        }

    }
    private boolean isValidName(String name) {
        String NAME_PATTERN ="[a-zA-z]*(['\\s]+[a-z]*)*";

        Pattern pattern = Pattern.compile(NAME_PATTERN);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }

}
