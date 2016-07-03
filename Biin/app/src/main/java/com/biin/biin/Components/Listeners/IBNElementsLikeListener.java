package com.biin.biin.Components.Listeners;

/**
 * Created by ramirezallan on 6/30/16.
 */
public interface IBNElementsLikeListener {
    void onElementLiked(String identifier, int position);
    void onElementUnliked(String identifier, int position);
}
