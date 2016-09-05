package com.biin.biin.Components.Listeners;

/**
 * Created by ramirezallan on 8/19/16.
 */
public interface IBNGiftActionListener {
    void onGiftDeleted(String identifier, int position);
    void onGiftShared(String identifier, int position);
}
