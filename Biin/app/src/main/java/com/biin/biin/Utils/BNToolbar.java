package com.biin.biin.Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.graphics.ColorUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.biin.biin.R;

/**
 * Created by ramirezallan on 5/24/16.
 */
public class BNToolbar {

    private Context context;
    private Activity activity;

    private ImageView ivToolbarBack, ivToolbarLike, ivToolbarLiked, ivToolbarShare, ivToolbarPosition, ivToolbarCall, ivToolbarMail;
    private TextView tvMoreFrom;

    private int primaryColor;
    private int secondaryColor;
    private boolean liked;
    private boolean showShare;
    private boolean showPosition;
    private boolean showCall;
    private boolean showMail;
    private boolean showMore;
    private String stringMore;

    public BNToolbar(Activity context) {
        this(context, context.getResources().getColor(R.color.colorGreen), context.getResources().getColor(R.color.colorWhite), false, true, false, false, false, false);
    }

    public BNToolbar(Activity context, int primaryColor, int secondaryColor, boolean liked, boolean showShare, boolean showPosition, boolean showCall, boolean showMail, boolean showMore) {
        this(context, primaryColor, secondaryColor, liked, showShare, showPosition, showCall, showMail, showMore, "");
    }

    public BNToolbar(Activity context, int primaryColor, int secondaryColor, boolean liked, boolean showShare, boolean showPosition, boolean showCall, boolean showMail, boolean showMore, String stringMore) {
        this.context = context;
        this.activity = context;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        this.liked = liked;
        this.showShare = showShare;
        this.showPosition = showPosition;
        this.showCall = showCall;
        this.showMail = showMail;
        this.showMore = showMore;
        this.stringMore = stringMore;

        setUpToolbar();
    }

    private void setUpToolbar(){
        ivToolbarBack = (ImageView)activity.findViewById(R.id.ivToolbarBack);
        ivToolbarLike = (ImageView)activity.findViewById(R.id.ivToolbarLike);
        ivToolbarLiked = (ImageView)activity.findViewById(R.id.ivToolbarLiked);
        ivToolbarShare = (ImageView)activity.findViewById(R.id.ivToolbarShare);
        ivToolbarPosition = (ImageView)activity.findViewById(R.id.ivToolbarPosition);
        ivToolbarCall = (ImageView)activity.findViewById(R.id.ivToolbarCall);
        ivToolbarMail = (ImageView)activity.findViewById(R.id.ivToolbarMail);
        tvMoreFrom = (TextView)activity.findViewById(R.id.tvToolbarMore);

        ivToolbarBack.setBackgroundColor(primaryColor);
        ivToolbarBack.setColorFilter(secondaryColor);
        ivToolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });

        if(liked){
            ivToolbarLike.setVisibility(View.GONE);
            ivToolbarLiked.setVisibility(View.VISIBLE);
        }else {
            ivToolbarLiked.setVisibility(View.GONE);
            ivToolbarLike.setVisibility(View.VISIBLE);
        }

        if(BNUtils.calculateContrast(context.getResources().getColor(R.color.colorAccent), primaryColor, secondaryColor)){
            ivToolbarLiked.setColorFilter(primaryColor);
            ivToolbarLike.setColorFilter(primaryColor);
        }else{
            ivToolbarLiked.setColorFilter(secondaryColor);
            ivToolbarLike.setColorFilter(secondaryColor);
        }

        ivToolbarLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO darle like
                Toast.makeText(context, "Like", Toast.LENGTH_SHORT).show();
                // cambiar el boton
                ivToolbarLike.setVisibility(View.GONE);
                ivToolbarLiked.setVisibility(View.VISIBLE);
            }
        });

        ivToolbarLiked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO quitar el like
                Toast.makeText(context, "Unlike", Toast.LENGTH_SHORT).show();
                // cambiar el boton
                ivToolbarLiked.setVisibility(View.GONE);
                ivToolbarLike.setVisibility(View.VISIBLE);
            }
        });

        if(showShare){
            ivToolbarShare.setVisibility(View.VISIBLE);
            ivToolbarShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO abrir ventana de compartir
                    Toast.makeText(context, "Share", Toast.LENGTH_SHORT).show();
                }
            });
        }

        if(showPosition){
            ivToolbarPosition.setVisibility(View.VISIBLE);
            ivToolbarPosition.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO mostrar la ventana de geoposicion
                    Toast.makeText(context, "Geoposition", Toast.LENGTH_SHORT).show();
                }
            });
        }

        if(showCall){
            ivToolbarCall.setVisibility(View.VISIBLE);
            ivToolbarCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO realizar llamada telefonica o marcar numero
                    Toast.makeText(context, "Call", Toast.LENGTH_SHORT).show();
                }
            });
        }

        if(showMail){
            ivToolbarMail.setVisibility(View.VISIBLE);
            ivToolbarMail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO abrir aplicacion para enviar mail
                    Toast.makeText(context, "Mail", Toast.LENGTH_SHORT).show();
                }
            });
        }

        if(showMore) {
            Typeface lato_regular = Typeface.createFromAsset(context.getAssets(), "Lato-Regular.ttf");
            tvMoreFrom.setTypeface(lato_regular);
            tvMoreFrom.setText(context.getResources().getText(R.string.MoreFrom).toString().trim() + " " + stringMore);
            tvMoreFrom.setVisibility(View.VISIBLE);
            tvMoreFrom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO abrir vista de site
                    Toast.makeText(context, "Show more", Toast.LENGTH_SHORT).show();
                }
            });
        }
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

    public int getPrimaryColor() {
        return primaryColor;
    }

    public void setPrimaryColor(int primaryColor) {
        this.primaryColor = primaryColor;
    }

    public int getSecondaryColor() {
        return secondaryColor;
    }

    public void setSecondaryColor(int secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public boolean isShowShare() {
        return showShare;
    }

    public void setShowShare(boolean showShare) {
        this.showShare = showShare;
    }

    public boolean isShowPosition() {
        return showPosition;
    }

    public void setShowPosition(boolean showPosition) {
        this.showPosition = showPosition;
    }

    public boolean isShowCall() {
        return showCall;
    }

    public void setShowCall(boolean showCall) {
        this.showCall = showCall;
    }

    public boolean isShowMail() {
        return showMail;
    }

    public void setShowMail(boolean showMail) {
        this.showMail = showMail;
    }

    public boolean isShowMore() {
        return showMore;
    }

    public void setShowMore(boolean showMore) {
        this.showMore = showMore;
    }

    public String getStringMore() {
        return stringMore;
    }

    public void setStringMore(String stringMore) {
        this.stringMore = stringMore;
    }
}
