package com.biin.biin.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.biin.biin.BiinApp;
import com.biin.biin.Components.Listeners.IBNGiftActionListener;
import com.biin.biin.Components.Listeners.IBNSitesLikeListener;
import com.biin.biin.Entities.BNGift;
import com.biin.biin.Entities.Biinie;
import com.biin.biin.Managers.BNAppManager;
import com.biin.biin.R;
import com.biin.biin.Utils.BNUtils;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by ramirezallan on 8/4/16.
 */
public class BNGiftAdapter extends RecyclerView.Adapter<BNGiftAdapter.BNGiftViewHolder> implements Response.Listener<JSONObject> {

    private static final String TAG = "BNGiftAdapter";
    private static Context context;

    private List<BNGift> gifts;
    private ImageLoader imageLoader;
    private IBNGiftActionListener giftsListener;
    private String organizationIdentifier = "";

    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    private LayoutInflater inflater;

    public BNGiftAdapter(Context context, List<BNGift> gifts, IBNGiftActionListener giftsListener) {
        super();
        this.context = context;
        this.gifts = gifts;
        this.giftsListener = giftsListener;
        imageLoader = ImageLoader.getInstance();
        viewBinderHelper.setOpenOnlyOne(true);
        inflater = LayoutInflater.from(context);
    }

    public void setOrganizationIdentifier(String organizationIdentifier){
        this.organizationIdentifier = organizationIdentifier;
    }

    public int delieverGift(String giftIdentifier){
        int position = -1;
        for (int i = 0; i < gifts.size(); i++) {
            if(gifts.get(i).getIdentifier().equals(giftIdentifier)){
                gifts.get(i).setStatus(BNGift.BNGiftStatus.DELIVERED);
                position = i;
            }
        }
        return position;
    }

    public void addItem(BNGift gift){
        this.gifts.add(0, gift);
        notifyItemInserted(0);
        notifyItemRangeChanged(0, gifts.size());
    }

    @Override
    public BNGiftViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.bngift_item, parent, false);
        v.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        BNGiftViewHolder holder = new BNGiftViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(final BNGiftViewHolder holder, final int position) {
        viewBinderHelper.bind(holder.swipeGift, String.valueOf(position));
        final BNGift item = gifts.get(position);

        imageLoader.displayImage(item.getMedia().get(0).getUrl(), holder.ivGift);

        SimpleDateFormat showFormatter = new SimpleDateFormat(BNUtils.getDisplayDateFormat());
        holder.tvDate.setText(showFormatter.format(item.getReceivedDate()));
        holder.tvName.setText(item.getName());
        holder.tvDescription.setText(item.getMessage());

        holder.ivGift.setBackgroundColor(item.getPrimaryColor());
//        if(BNUtils.calculateContrast(context.getResources().getColor(R.color.colorWhite), item.getPrimaryColor(), item.getSecondaryColor())) {
//            holder.tvName.setTextColor(item.getPrimaryColor());
//            holder.tvRequest.setTextColor(item.getSecondaryColor());
//            holder.tvRequest.setBackgroundColor(item.getPrimaryColor());
//        }else{
            holder.tvName.setTextColor(item.getSecondaryColor());
            holder.tvRequest.setTextColor(item.getPrimaryColor());
            holder.tvRequest.setBackgroundColor(item.getSecondaryColor());
//        }

        String text = context.getString(R.string.SENT);
        if(item.getStatus() == BNGift.BNGiftStatus.SENT) {
            if (item.getOrganizationIdentifier().equals(organizationIdentifier)) {
                text = context.getString(R.string.READY_TO_CLAIM);
                holder.tvRequest.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                claimGift(item.getIdentifier());
                                String text = context.getString(R.string.CLAIMED);
                                holder.tvRequest.setOnClickListener(null);
                                holder.tvRequest.setText(text);
                            }
                        }
                );
            } else {
                text = context.getString(R.string.SENT);
                holder.tvRequest.setOnClickListener(null);
            }
        }else{
            if(item.getStatus() == BNGift.BNGiftStatus.CLAIMED) {
                text = context.getString(R.string.CLAIMED);
            }else {
                if(item.getStatus() == BNGift.BNGiftStatus.DELIVERED) {
                    text = context.getString(R.string.DELIVERED);
                }
            }
            holder.tvRequest.setOnClickListener(null);
        }
        holder.tvRequest.setText(text);

        holder.ivDelete.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gifts.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, gifts.size());
                    viewBinderHelper.closeLayout(String.valueOf(position));
                    giftsListener.onGiftDeleted(item.getIdentifier(), position);
                }
            }
        );
        holder.ivShare.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Share (position: " + position + ")", Toast.LENGTH_SHORT).show();
                }
            }
        );
        holder.ivStores.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Stores (position: " + position + ")", Toast.LENGTH_SHORT).show();
                }
            }
        );
    }

    private void claimGift(String gift){
        JSONObject request = new JSONObject();
        try {
            JSONObject model = new JSONObject();
            model.put("giftIdentifier", gift);
            request.put("model", model);
        }catch (JSONException e){
            Log.e(TAG, "Error:" + e.getMessage());
        }
        Biinie biinie = BNAppManager.getInstance().getDataManagerInstance().getBiinie();
        String identifier = "";

        if(biinie != null && biinie.getIdentifier() != null && !biinie.getIdentifier().isEmpty()){
            identifier = biinie.getIdentifier();
        }else {
            SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.preferences_key), Context.MODE_PRIVATE);
            identifier = preferences.getString(BNUtils.BNStringExtras.BNBiinie, "");
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                BNAppManager.getInstance().getNetworkManagerInstance().getGiftClaimUrl(identifier),
                request,
                this,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        onVolleyError(error);
                    }
                });
        BiinApp.getInstance().addToRequestQueue(jsonObjectRequest, TAG);
    }

    private void onVolleyError(VolleyError error){
        Log.e(TAG, "Error:" + error.getMessage());
    }

    @Override
    public void onResponse(JSONObject response) {
        Log.e(TAG, "Response:" + response.toString());
        try {
            String result = response.getString("result");
            String status = response.getString("status");
            if(status.equals("0") && result.equals("1")){
                Log.e(TAG, "Gift reclamado correctamente.");
            }else{
                Log.e(TAG, "Error actualizando el gift en el server.");
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error parseando el JSON.", e);
        }
    }

    @Override
    public int getItemCount() {
        return gifts.size();
    }

    public static class BNGiftViewHolder extends RecyclerView.ViewHolder {

        protected String id;
        protected SwipeRevealLayout swipeGift;
        protected CardView cvGift;
        protected ImageView ivGift, ivDelete, ivShare, ivStores;
        protected TextView tvDate, tvName, tvDescription, tvRequest, tvDummy;

        public BNGiftViewHolder(View itemView) {
            super(itemView);

            cvGift = (CardView)itemView.findViewById(R.id.cvGiftItem);
            swipeGift = (SwipeRevealLayout) itemView.findViewById(R.id.swipeGift);

            ivGift = (ImageView) itemView.findViewById(R.id.ivGiftImage);
            ivDelete = (ImageView) itemView.findViewById(R.id.ivGiftDelete);
            ivShare = (ImageView) itemView.findViewById(R.id.ivGiftShare);
            ivStores = (ImageView) itemView.findViewById(R.id.ivGiftStores);

            tvName = (TextView)itemView.findViewById(R.id.tvGiftName);
            tvDate = (TextView)itemView.findViewById(R.id.tvGiftDate);
            tvDescription = (TextView)itemView.findViewById(R.id.tvGiftDetails);
            tvRequest = (TextView)itemView.findViewById(R.id.tvRequestGift);
            tvDummy = (TextView)itemView.findViewById(R.id.tvGiftDummy);

            Typeface lato_regular = BNUtils.getLato_regular();
            Typeface lato_black = BNUtils.getLato_black();

            tvName.setTypeface(lato_black);
            tvDate.setTypeface(lato_regular);
            tvDescription.setTypeface(lato_regular);
            tvRequest.setTypeface(lato_regular);
            tvDummy.setTypeface(lato_regular);
        }
    }
}
