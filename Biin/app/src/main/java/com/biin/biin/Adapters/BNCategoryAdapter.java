package com.biin.biin.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.biin.biin.Components.BNProgressViewHolder;
import com.biin.biin.Components.Listeners.BNLoadMoreElementsListener;
import com.biin.biin.ElementsActivity;
import com.biin.biin.Utils.BNUtils;
import com.biin.biin.BiinApp;
import com.biin.biin.Entities.BNElement;
import com.biin.biin.R;

import java.util.List;

/**
 * Created by ramirezallan on 5/12/16.
 */
public class BNCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "BNCategoryAdapter";
    private static Context context;

    private List<BNElement> elements;
    private ImageLoader imageLoader;

    // load more start
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private BNLoadMoreElementsListener onLoadMoreListener;
    // load more end

    public BNCategoryAdapter(Context context, List<BNElement> elements) {
        super();
        this.context = context;
        this.elements = elements;
        imageLoader = BiinApp.getInstance().getImageLoader();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // load more start
        RecyclerView.ViewHolder holder = null;

        if(viewType == VIEW_PROG){

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_item, parent, false);
            v.setLayoutParams(new RecyclerView.LayoutParams((int)(56 * BNUtils.getDensity()), (BNUtils.getWidth() / 2) + (int)(58 * BNUtils.getDensity())));
            holder = new BNProgressViewHolder(v);

        }else if(viewType == VIEW_ITEM) {
        // load more end

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bncategory_item, parent, false);
            v.setLayoutParams(new RecyclerView.LayoutParams((elements.size() == 1) ? BNUtils.getWidth() : (BNUtils.getWidth() / 2), (BNUtils.getWidth() / 2) + (int) (58 * BNUtils.getDensity())));
            holder = new BNCategoryViewHolder(v);

        // load more start
        }
        // load more end
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        // load more start
        if (viewHolder instanceof BNProgressViewHolder) {
            BNProgressViewHolder holder = (BNProgressViewHolder) viewHolder;
            holder.progressBar.setIndeterminate(true);
        } else if (viewHolder instanceof BNCategoryViewHolder) {
            BNCategoryViewHolder holder = (BNCategoryViewHolder) viewHolder;
        // load more end

            final BNElement item = elements.get(position);
            TableRow.LayoutParams params = new TableRow.LayoutParams((elements.size() == 1) ? BNUtils.getWidth() : (BNUtils.getWidth() / 2), (BNUtils.getWidth() / 2) + (int) (58 * BNUtils.getDensity()));

//        loadCategoryElementImage(item.getMedia().get(0).getUrl(), holder);
            imageLoader.get(item.getMedia().get(0).getUrl(), ImageLoader.getImageListener(holder.ivCategoryElement, R.drawable.bg_feedback, R.drawable.biin));
            holder.ivCategoryElement.setImageUrl(item.getMedia().get(0).getUrl(), imageLoader);
            holder.ivCategoryElement.setBackgroundColor(item.getShowcase().getSite().getOrganization().getPrimaryColor());

            holder.rlCategoryLabel.setBackgroundColor(item.getShowcase().getSite().getOrganization().getPrimaryColor());
            holder.tvCategoryElement.setText(item.getTitle());
            holder.tvCategoryElement.setTextColor(item.getShowcase().getSite().getOrganization().getSecondaryColor());
            holder.tvCategorySubtitle.setText(item.getShowcase().getSite().getTitle());
            holder.tvCategorySubtitle.setTextColor(item.getShowcase().getSite().getOrganization().getSecondaryColor());
            holder.tvCategoryLocation.setText(item.getShowcase().getSite().getSubTitle());
            holder.tvCategoryLocation.setTextColor(item.getShowcase().getSite().getOrganization().getSecondaryColor());
//        holder.tvCategoryPrice.setText(item.getListPrice());
            holder.tvCategoryPrice.setTextColor(item.getShowcase().getSite().getOrganization().getSecondaryColor());
            holder.tvCategoryDiscount.setText("Ȼ" + item.getPrice());
            holder.tvCategoryDiscount.setTextColor(item.getShowcase().getSite().getOrganization().getSecondaryColor());

            if (item.isHasDiscount() && !item.getDiscount().isEmpty()) {
                holder.tvCategoryOffer.setText("-" + item.getDiscount() + "%");
                holder.tvCategoryOffer.setTextColor(item.getShowcase().getSite().getOrganization().getSecondaryColor());
                holder.ivCategoryOffer.setColorFilter(item.getShowcase().getSite().getOrganization().getPrimaryColor());
                holder.flCategoryOffer.setVisibility(View.VISIBLE);
            } else {
                holder.flCategoryOffer.setVisibility(View.GONE);
            }
            holder.cvCategory.setLayoutParams(params);

            holder.cvCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, ElementsActivity.class);
                    SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.preferences_key), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(BNUtils.BNStringExtras.BNElement, item.getIdentifier());
                    editor.commit();
                    i.putExtra(BNUtils.BNStringExtras.BNShowMore, true);
                    context.startActivity(i);
                }
            });

        // load more start
        }
        // load more end

    }

    @Override
    public int getItemCount() {
        return elements.size();
    }

    // load more start
    @Override
    public int getItemViewType(int position) {
        return elements.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    public void setLoaded(){
        loading = false;
    }

    public void setOnLoadMoreListener(BNLoadMoreElementsListener onLoadMoreListener){
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public BNCategoryAdapter(Context context, List<BNElement> elements, RecyclerView recyclerView) {
        this(context, elements);

        if(recyclerView.getLayoutManager() instanceof LinearLayoutManager){
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                    if(!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)){
                        if(onLoadMoreListener != null){
                            onLoadMoreListener.onLoadMoreElements();
                        }
                        loading = true;
                    }
                }
            });
        }
    }
    // load more end

    public BNElement getCategoryElement(int position){
        return elements.get(position);
    }

    private void loadCategoryElementImage(String imageURL, final BNCategoryViewHolder holder) {
        imageLoader.get(imageURL, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                holder.ivCategoryElement.setImageBitmap(response.getBitmap());
            }
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
    }

    public static class BNCategoryViewHolder extends RecyclerView.ViewHolder{

        protected CardView cvCategory;
        protected RelativeLayout rlCategoryLabel;
        protected FrameLayout flCategoryOffer;
        protected ImageView ivCategoryOffer;
        protected NetworkImageView ivCategoryElement;
        protected TextView tvCategoryElement, tvCategorySubtitle, tvCategoryLocation, tvCategoryPrice, tvCategoryDiscount, tvCategoryOffer;

        public BNCategoryViewHolder(View itemView) {
            super(itemView);

            cvCategory = (CardView)itemView.findViewById(R.id.cvCategory);
            rlCategoryLabel = (RelativeLayout)itemView.findViewById(R.id.rlCategoryLabel);
            flCategoryOffer = (FrameLayout)itemView.findViewById(R.id.flCategoryOffer);

            ivCategoryElement = (NetworkImageView)itemView.findViewById(R.id.ivCategoryElement);
            ivCategoryOffer = (ImageView)itemView.findViewById(R.id.ivCategoryOffer);

            tvCategoryElement = (TextView)itemView.findViewById(R.id.tvCategoryElement);
            tvCategorySubtitle = (TextView)itemView.findViewById(R.id.tvCategorySubtitle);
            tvCategoryLocation = (TextView)itemView.findViewById(R.id.tvCategoryLocation);
            tvCategoryPrice = (TextView)itemView.findViewById(R.id.tvCategoryPrice);
            tvCategoryDiscount = (TextView)itemView.findViewById(R.id.tvCategoryDiscount);
            tvCategoryOffer = (TextView)itemView.findViewById(R.id.tvCategoryOffer);

            Typeface lato_regular = BNUtils.getLato_regular();
            Typeface lato_black = BNUtils.getLato_black();

            tvCategoryElement.setTypeface(lato_black);
            tvCategorySubtitle.setTypeface(lato_black);
            tvCategoryLocation.setTypeface(lato_regular);
            tvCategoryPrice.setTypeface(lato_regular);
            tvCategoryDiscount.setTypeface(lato_regular);
            tvCategoryOffer.setTypeface(lato_black);
        }
    }

}