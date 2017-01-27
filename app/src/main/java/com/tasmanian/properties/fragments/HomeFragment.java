package com.tasmanian.properties.fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Vector;

import com.tasmanian.properties.TasmanianProperty;
import com.tasmanian.properties.R;
import com.tasmanian.properties.adapter.CatListAdapter;
import com.tasmanian.properties.callbacks.IItemHandler;
import com.tasmanian.properties.common.Item;
import com.tasmanian.properties.helper.RecyclerOnScrollListener;
import com.tasmanian.properties.tasks.HTTPBackgroundTask;

/**
 * Created by w7 on 22/08/2016.
 */
public class HomeFragment extends ParentFragment implements IItemHandler {

    private TasmanianProperty propertUpdates = null;
    private View layout = null;

    private RecyclerView mRecyclerView = null;

    private SwipeRefreshLayout mSwipeRefreshLayout = null;

    private RecyclerOnScrollListener recycScollListener = null;


    private String total_pages = "0";

    private boolean isRefresh = false;

    private CatListAdapter adapter = null;

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        this.propertUpdates = (TasmanianProperty) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        layout = inflater.inflate(R.layout.tempalte_recyclerview, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) layout.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark, R.color.green,
                R.color.red, R.color.bs_blue, R.color.colorPrimaryDark);
        mSwipeRefreshLayout.setEnabled(false);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                recycScollListener.resetValue();
                getProertyData(1, 0);
            }
        });

        ((TextView) layout.findViewById(R.id.catgr_txt)).setText("No Data Found");
        ((TextView) layout.findViewById(R.id.tv_txt_title)).setVisibility(View.GONE);
        layout.findViewById(R.id.get_laocation_sr).setVisibility(View.VISIBLE);
        layout.findViewById(R.id.get_laocation_sr).setOnClickListener(propertUpdates);
        return layout;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        GridLayoutManager layoutManager = new GridLayoutManager(propertUpdates,2);

        mRecyclerView = (RecyclerView) layout.findViewById(R.id.content_list);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        recycScollListener = new RecyclerOnScrollListener(layoutManager) {

            @Override
            public void onLoadMoreData(int currentPage) {
                if (total_pages.length() > 0)
                    if (currentPage <= Integer.parseInt(total_pages) - 1) {
                        getProertyData(2, currentPage);
                        getView().findViewById(R.id.catgr_pbar).setVisibility(View.VISIBLE);

                    }
            }
        };

        getProertyData(1, 0);
        mRecyclerView.addOnScrollListener(recycScollListener);
        setHasOptionsMenu(true);
        getActivity().supportInvalidateOptionsMenu();

    }
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        if (!propertUpdates.isDrawerOpen()) {
//            inflater.inflate(R.menu.search, menu);
//
//        }
//
//        super.onCreateOptionsMenu(menu, inflater);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        switch (item.getItemId()) {
//
//            case R.id.menu_search:
//
//                propertUpdates.mySearchFragment=new MySearchFragment();
//                Bundle bundle = new Bundle();
//                propertUpdates.mySearchFragment.setArguments(bundle);
//                propertUpdates.swiftFragments(propertUpdates.mySearchFragment,"mySearchFragment");
//                break;
//            default:
//                break;
//
//        }
//        return super.onOptionsItemSelected(item);
//    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden)
            if (isRefresh) {
                isRefresh = false;
                getProertyData(1, 0);
            }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {

        adapter.clear();
        adapter.notifyDataSetChanged();
        adapter.release();
        adapter = null;

        mRecyclerView.removeAllViews();
        mRecyclerView = null;

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

    private void getProertyData(int requestId, int currentNo) {

        getView().findViewById(R.id.catgr_txt).setVisibility(View.GONE);

        try {
            if (adapter == null) {
                adapter = new CatListAdapter(propertUpdates, "");
                mRecyclerView.setAdapter(adapter);
            }

            String link = propertUpdates.getPropertyValue("categories");
            HTTPBackgroundTask task = new HTTPBackgroundTask(this, getActivity(), 1, requestId);
            task.execute(link);
        } catch (Exception e) {
            e.printStackTrace();
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

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onFragmentChildClick(View view) {

        int itemPosition = mRecyclerView.getChildLayoutPosition(view);

        Item item = adapter.getItems().get(itemPosition);

        propertUpdates.showViewSubcatPage(item);

        super.onFragmentChildClick(view);
    }

    @Override
    public String getFragmentName() {
        return "Home";// getString(R.string.protections);
    }

    @Override
    public int getFragmentActionBarColor() {
        return R.color.settings_gray;
    }
    @Override
    public void onFinish(Object results, int requestType) {

        getView().findViewById(R.id.catgr_pbar).setVisibility(View.GONE);

        switch (requestType) {
            case 1:

                mSwipeRefreshLayout.setRefreshing(false);
                mSwipeRefreshLayout.setEnabled(true);

                if (results != null) {

                    Item item = (Item) results;
                    Vector<Item> items = (Vector<Item>) item.get("category_detail");
                    if (items != null && items.size() > 0) {
                        adapter.setItems(items);
                        adapter.notifyDataSetChanged();
                        return;
                    }

                }

                adapter.clear();
                adapter.notifyDataSetChanged();
                getView().findViewById(R.id.catgr_txt).setVisibility(View.VISIBLE);

                break;

            default:
                break;
        }
    }

    @Override
    public void onError(String errorCode, int requestType) {
        mSwipeRefreshLayout.setRefreshing(false);
        mSwipeRefreshLayout.setEnabled(true);

        propertUpdates.showToast(errorCode);

        getView().findViewById(R.id.catgr_pbar).setVisibility(View.GONE);

    }
}
