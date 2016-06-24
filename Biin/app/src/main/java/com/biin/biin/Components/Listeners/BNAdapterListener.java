package com.biin.biin.Components.Listeners;

import com.biin.biin.Entities.BNSite;

/**
 * Created by ramirezallan on 6/22/16.
 */
public interface BNAdapterListener {
    void onEmptyAdapter(String type);
    void onLoadAdapter(String type);

    interface BNSitesLikeListener {
        void onSiteLiked(String identifier);
        void onSiteUnliked(String identifier);
    }

}
