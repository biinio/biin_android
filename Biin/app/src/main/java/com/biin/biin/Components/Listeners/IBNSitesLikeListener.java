package com.biin.biin.Components.Listeners;

/**
 * Created by ramirezallan on 6/22/16.
 */
public interface IBNSitesLikeListener {
    void onSiteLiked(String identifier, int position);
    void onSiteUnliked(String identifier, int position);
}
