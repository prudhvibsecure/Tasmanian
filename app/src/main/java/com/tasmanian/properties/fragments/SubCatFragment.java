package com.tasmanian.properties.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Vector;

import com.tasmanian.properties.TasmanianProperty;
import com.tasmanian.properties.R;
import com.tasmanian.properties.adapter.CompanyAdapter;
import com.tasmanian.properties.adapter.InnerCatAdapter;
import com.tasmanian.properties.adapter.SubcatAdapter;
import com.tasmanian.properties.callbacks.IItemHandler;
import com.tasmanian.properties.common.Item;
import com.tasmanian.properties.helper.RecyclerOnScrollListener;
import com.tasmanian.properties.tasks.HTTPBackgroundTask;
import com.tasmanian.properties.tasks.HTTPPostTask;

import org.json.JSONObject;

/**
 * Created by w7 on 22/08/2016.
 */
public class SubCatFragment extends ParentFragment implements IItemHandler {

    private TasmanianProperty propertUpdates = null;
    private View layout = null;

    private RecyclerView mRecyclerView = null;

    private SwipeRefreshLayout mSwipeRefreshLayout = null;

    private RecyclerOnScrollListener recycScollListener = null;

    private Item item = null;

    private String total_pages = "0";

    private boolean isRefresh = false;

    public CompanyAdapter adapter = null;

    private InnerCatAdapter innerCatAdapter = null;

    private TextView tv_title;

    private String condition;

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        this.propertUpdates = (TasmanianProperty) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle mArgs = getArguments();

        item = (Item) mArgs.getSerializable("item");
        condition = item.getAttribute("innercategory");

        layout = inflater.inflate(R.layout.tempalte_recyclerview, container, false);

        tv_title = (TextView) layout.findViewById(R.id.tv_txt_title);
        tv_title.setText(item.getAttribute("subcatname"));

        mSwipeRefreshLayout = (SwipeRefreshLayout) layout.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark, R.color.green,
                R.color.red, R.color.bs_blue, R.color.colorPrimaryDark);
        mSwipeRefreshLayout.setEnabled(false);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                recycScollListener.resetValue();
                if (condition.equalsIgnoreCase("No")) {
                    getProertyData(1, 0);
                } else {
                    getInnerData(1, 0);
                }
            }
        });

        ((TextView) layout.findViewById(R.id.catgr_txt)).setText("No Data Found");

        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (condition.equalsIgnoreCase("No")) {

            LinearLayoutManager layoutManager = new LinearLayoutManager(propertUpdates);
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
        }
        if (condition.equalsIgnoreCase("Yes")) {
            GridLayoutManager layoutManager = new GridLayoutManager(propertUpdates, 2);
            mRecyclerView = (RecyclerView) layout.findViewById(R.id.content_list);

            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());

            recycScollListener = new RecyclerOnScrollListener(layoutManager) {

                @Override
                public void onLoadMoreData(int currentPage) {
                    if (total_pages.length() > 0)
                        if (currentPage <= Integer.parseInt(total_pages) - 1) {
                            getInnerData(3, currentPage);
                            getView().findViewById(R.id.catgr_pbar).setVisibility(View.VISIBLE);

                        }
                }
            };

            getInnerData(2, 0);
            mRecyclerView.addOnScrollListener(recycScollListener);
        }


        setHasOptionsMenu(true);
        getActivity().supportInvalidateOptionsMenu();
    }

    private void getInnerData(int requestId, int currentNo) {
        getView().findViewById(R.id.catgr_txt).setVisibility(View.GONE);

        try {
            if (innerCatAdapter == null) {
                innerCatAdapter = new InnerCatAdapter(propertUpdates, "");
                mRecyclerView.setAdapter(innerCatAdapter);
            }

            String link = propertUpdates.getPropertyValue("companies_list");
            JSONObject object = new JSONObject();
            object.put("subcatid", item.getAttribute("subcatid"));

            HTTPPostTask task = new HTTPPostTask(getActivity(), this);
            task.disableProgress();
            task.userRequest("", 2, link, object.toString(), 1);//
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

        if (condition.equalsIgnoreCase("No")) {
            adapter.clear();
            adapter.notifyDataSetChanged();
            adapter.release();
            adapter = null;
        } else {

            innerCatAdapter.clear();
            innerCatAdapter.notifyDataSetChanged();
            innerCatAdapter.release();
            innerCatAdapter = null;
        }


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
                adapter = new CompanyAdapter(propertUpdates, "");
                mRecyclerView.setAdapter(adapter);
            }

            String link = propertUpdates.getPropertyValue("companies_list");
            JSONObject object = new JSONObject();
            object.put("subcatid", item.getAttribute("subcatid"));

            HTTPPostTask task = new HTTPPostTask(getActivity(), this);
            task.disableProgress();
            task.userRequest("", 1, link, object.toString(), 1);//
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

        if (condition.equalsIgnoreCase("No")) {
            int itemPosition = mRecyclerView.getChildLayoutPosition(view);

            Item item = adapter.getItems().get(itemPosition);

            propertUpdates.showViewInfo(item);
        } else {
            int itemPosition = mRecyclerView.getChildLayoutPosition(view);

            Item item = innerCatAdapter.getItems().get(itemPosition);

            propertUpdates.showComanysInner(item);
        }

        super.onFragmentChildClick(view);
    }

    @Override
    public String getFragmentName() {
        return "SubCategory";// getString(R.string.protections);
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
                    Vector<Item> items = (Vector<Item>) item.get("company_category_detail");
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

            case 2:

                mSwipeRefreshLayout.setRefreshing(false);
                mSwipeRefreshLayout.setEnabled(true);

                if (results != null) {

                    Item item = (Item) results;
                    Vector<Item> items = (Vector<Item>) item.get("innercategory_detail");
                    if (items != null && items.size() > 0) {
                        innerCatAdapter.setItems(items);
                        innerCatAdapter.notifyDataSetChanged();
                        return;
                    }

                }

                innerCatAdapter.clear();
                innerCatAdapter.notifyDataSetChanged();
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
