package com.biin.biin.Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.biin.biin.Components.Listeners.IBNToolbarListener;
import com.biin.biin.R;

/**
 * Created by ramirezallan on 5/24/16.
 */
public class BNToolbar {

    private Context context;
    private Activity activity;
    private IBNToolbarListener listener;

    private ImageView ivToolbarBack, ivToolbarLike, ivToolbarLiked, ivToolbarShare, ivToolbarPosition, ivToolbarCall, ivToolbarMail;
    private TextView tvMoreFrom, tvTitle;
    private LinearLayout hlLikes;

    private int primaryColor;
    private int secondaryColor;
    private boolean showLike;
    private boolean liked;
    private boolean showShare;
    private boolean showPosition;
    private boolean showCall;
    private boolean showMail;
    private boolean showMore;
    private String stringMore;
    private boolean showTitle;
    private String stringTitle;

    public BNToolbar(Activity context, String title) {
        this(context, 0, 0, false, false, false, false, false, false, false, "", true, title);
    }

    public BNToolbar(Activity context, boolean showLike, boolean liked, boolean showShare, boolean showMore, String stringMore) {
        this(context, 0, 0, showLike, liked, showShare, false, false, false, showMore, stringMore, false, "");
    }

    public BNToolbar(Activity context, boolean showLike, boolean liked, boolean showShare, boolean showPosition, boolean showCall, boolean showMail) {
        this(context, 0, 0, showLike, liked, showShare, showPosition, showCall, showMail, false, "", false, "");
    }

    public BNToolbar(Activity context, int primaryColor, int secondaryColor, boolean showLike, boolean liked, boolean showShare, boolean showPosition, boolean showCall, boolean showMail, boolean showMore, String stringMore, boolean showTitle, String stringTitle) {
        this.context = context;
        this.activity = context;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        this.showLike = showLike;
        this.liked = liked;
        this.showShare = showShare;
        this.showPosition = showPosition;
        this.showCall = showCall;
        this.showMail = showMail;
        this.showMore = showMore;
        this.stringMore = stringMore;
        this.showTitle = showTitle;
        this.stringTitle = stringTitle;

        setUpToolbar();
    }

    public void setTitle(String stringTitle){
        this.stringTitle = stringTitle;
        tvTitle.setText(this.stringTitle);
        tvTitle.setSelected(true);
    }

    private void setUpToolbar(){
        hlLikes = (LinearLayout)activity.findViewById(R.id.hlToolbarLikes);
        ivToolbarBack = (ImageView)activity.findViewById(R.id.ivToolbarBack);
        ivToolbarLike = (ImageView)activity.findViewById(R.id.ivToolbarLike);
        ivToolbarLiked = (ImageView)activity.findViewById(R.id.ivToolbarLiked);
        ivToolbarShare = (ImageView)activity.findViewById(R.id.ivToolbarShare);
        ivToolbarPosition = (ImageView)activity.findViewById(R.id.ivToolbarPosition);
        ivToolbarCall = (ImageView)activity.findViewById(R.id.ivToolbarCall);
        ivToolbarMail = (ImageView)activity.findViewById(R.id.ivToolbarMail);
        tvMoreFrom = (TextView)activity.findViewById(R.id.tvToolbarMore);
        tvTitle = (TextView)activity.findViewById(R.id.tvToolbarTitle);

//        ivToolbarBack.setBackgroundColor(primaryColor);
//        ivToolbarBack.setColorFilter(secondaryColor);
        ivToolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null) {
                    listener.onBack();
                }
                activity.finish();
            }
        });

        if(showLike) {
            hlLikes.setVisibility(View.VISIBLE);
            if (liked) {
                ivToolbarLike.setVisibility(View.GONE);
                ivToolbarLiked.setVisibility(View.VISIBLE);
            } else {
                ivToolbarLiked.setVisibility(View.GONE);
                ivToolbarLike.setVisibility(View.VISIBLE);
            }
            ivToolbarLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        listener.onLike();
                    }
                    // cambiar el boton
                    ivToolbarLike.setVisibility(View.GONE);
                    ivToolbarLiked.setVisibility(View.VISIBLE);
                }
            });

            ivToolbarLiked.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        listener.onUnlike();
                    }
                    // cambiar el boton
                    ivToolbarLiked.setVisibility(View.GONE);
                    ivToolbarLike.setVisibility(View.VISIBLE);
                }
            });
        }else{
            hlLikes.setVisibility(View.GONE);
        }

//        if(BNUtils.calculateContrast(context.getResources().getColor(R.color.colorAccentGray), primaryColor, secondaryColor)){
//            ivToolbarLiked.setColorFilter(primaryColor);
//            ivToolbarLike.setColorFilter(primaryColor);
//        }else{
//            ivToolbarLiked.setColorFilter(secondaryColor);
//            ivToolbarLike.setColorFilter(secondaryColor);
//        }

        if(showShare){
            ivToolbarShare.setVisibility(View.VISIBLE);
            ivToolbarShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        listener.onShare();
                    }
                }
            });
        }

        if(showPosition){
            ivToolbarPosition.setVisibility(View.VISIBLE);
            ivToolbarPosition.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        listener.onLocation();
                    }
                }
            });
        }

        if(showCall){
            ivToolbarCall.setVisibility(View.VISIBLE);
            ivToolbarCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        listener.onCall();
                    }
                }
            });
        }

        if(showMail){
            ivToolbarMail.setVisibility(View.VISIBLE);
            ivToolbarMail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        listener.onMail();
                    }
                }
            });
        }

        if(showMore) {
            Typeface lato_regular = BNUtils.getLato_regular();
            tvMoreFrom.setTypeface(lato_regular);
            tvMoreFrom.setText(context.getResources().getText(R.string.MoreFrom).toString().trim() + " " + stringMore);
            tvMoreFrom.setVisibility(View.VISIBLE);
            tvMoreFrom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        listener.onShowMore();
                    }
                }
            });
        }

        if(showTitle) {
            Typeface lato_black = BNUtils.getLato_black();
            tvTitle.setTypeface(lato_black);
            tvTitle.setLetterSpacing(0.3f);
            tvTitle.setText(stringTitle);
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setSelected(true);
        }
    }

    public void setListener(IBNToolbarListener listener) {
        this.listener = listener;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

}
