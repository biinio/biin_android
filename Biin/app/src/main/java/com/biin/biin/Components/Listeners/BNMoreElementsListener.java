package com.biin.biin.Components.Listeners;

import com.biin.biin.Adapters.BNCategoryAdapter;
import com.biin.biin.Volley.Listeners.BNElementsListener;

/**
 * Created by ramirezallan on 6/27/16.
 */
public class BNMoreElementsListener implements BNElementsListener.IBNElementsListener {

    private BNCategoryAdapter adapter;

    public BNMoreElementsListener(BNCategoryAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void onItemRemoved(int position) {
        adapter.notifyItemRemoved(position);
    }

    @Override
    public void onItemInserted(int position) {
        adapter.notifyItemInserted(position);
    }

    @Override
    public void onLoadMoreElementsResponse() {
        adapter.setLoaded();
    }
}
