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
public class BNShowcaseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "BNShowcaseAdapter";
    private static Context context;

    private List<BNElement> elements;
    private ImageLoader imageLoader;

    private boolean showMore = false;

    // load more start
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private BNLoadMoreElementsListener onLoadMoreListener;
    // load more end

    public BNShowcaseAdapter(Context context, List<BNElement> elements) {
        super();
        this.context = context;
        this.elements = elements;
        imageLoader = BiinApp.getInstance().getImageLoader();
    }

    public void setShowMore(boolean showMore) {
        this.showMore = showMore;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // load more start
        RecyclerView.ViewHolder holder = null;

        if(viewType == VIEW_PROG){

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_item, parent, false);
            v.setLayoutParams(new RecyclerView.LayoutParams(BNUtils.getWidth(), (int)(68 * BNUtils.getDensity())));
            holder = new BNProgressViewHolder(v);

        }else if(viewType == VIEW_ITEM) {
        // load more end

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bnshowcase_item, parent, false);
            v.setLayoutParams(new RecyclerView.LayoutParams((elements.size() == 1) ? BNUtils.getWidth() : (BNUtils.getWidth() / 2), (BNUtils.getWidth() / 2) + (int) (48 * BNUtils.getDensity())));
            holder = new BNShowcaseViewHolder(v);

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
        } else if (viewHolder instanceof BNShowcaseViewHolder) {
            BNShowcaseViewHolder holder = (BNShowcaseViewHolder) viewHolder;
        // load more end

            final BNElement item = elements.get(position);
            TableRow.LayoutParams params = new TableRow.LayoutParams((elements.size() == 1) ? BNUtils.getWidth() : (BNUtils.getWidth() / 2), (BNUtils.getWidth() / 2) + (int) (48 * BNUtils.getDensity()));

//        loadShowcaseElementImage(item.getMedia().get(0).getUrl(), holder);
            imageLoader.get(item.getMedia().get(0).getUrl(), ImageLoader.getImageListener(holder.ivShowcaseElement, R.drawable.bg_feedback, R.drawable.biin));
            holder.ivShowcaseElement.setImageUrl(item.getMedia().get(0).getUrl(), imageLoader);
            holder.ivShowcaseElement.setBackgroundColor(item.getShowcase().getSite().getOrganization().getPrimaryColor());

            holder.rlShowcaseLabel.setBackgroundColor(item.getShowcase().getSite().getOrganization().getPrimaryColor());
            holder.tvShowcaseTitle.setText(item.getTitle());
            holder.tvShowcaseTitle.setTextColor(item.getShowcase().getSite().getOrganization().getSecondaryColor());
            holder.tvShowcaseSubtitle.setText(item.getSubTitle());
            holder.tvShowcaseSubtitle.setTextColor(item.getShowcase().getSite().getOrganization().getSecondaryColor());

            if (item.isHasDiscount() && !item.getDiscount().isEmpty()) {
                holder.tvShowcaseOffer.setText("-" + item.getDiscount() + "%");
                holder.tvShowcaseOffer.setTextColor(item.getShowcase().getSite().getOrganization().getSecondaryColor());
                holder.ivShowcaseOffer.setColorFilter(item.getShowcase().getSite().getOrganization().getPrimaryColor());
                holder.flShowcaseOffer.setVisibility(View.VISIBLE);
            } else {
                holder.flShowcaseOffer.setVisibility(View.GONE);
            }

            holder.cvShowcase.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, ElementsActivity.class);
                    SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.preferences_key), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(BNUtils.BNStringExtras.BNElement, item.getIdentifier());
                    editor.commit();
                    i.putExtra(BNUtils.BNStringExtras.BNShowMore, showMore);
                    context.startActivity(i);
                }
            });

            holder.cvShowcase.setLayoutParams(params);

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

    public BNShowcaseAdapter(Context context, List<BNElement> elements, RecyclerView recyclerView) {
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

    public BNElement getShowcaseElement(int position){
        return elements.get(position);
    }

    private void loadShowcaseElementImage(String imageURL, final BNShowcaseViewHolder holder) {
        imageLoader.get(imageURL, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                holder.ivShowcaseElement.setImageBitmap(response.getBitmap());
            }
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
    }

    public static class BNShowcaseViewHolder extends RecyclerView.ViewHolder{

        protected CardView cvShowcase;
        protected RelativeLayout rlShowcaseLabel;
        protected FrameLayout flShowcaseOffer;
        protected ImageView ivShowcaseOffer;
        protected NetworkImageView ivShowcaseElement;
        protected TextView tvShowcaseTitle, tvShowcaseSubtitle, tvShowcaseOffer;

        public BNShowcaseViewHolder(View itemView) {
            super(itemView);

            cvShowcase = (CardView)itemView.findViewById(R.id.cvShowcase);
            rlShowcaseLabel = (RelativeLayout)itemView.findViewById(R.id.rlShowcaseLabel);
            flShowcaseOffer = (FrameLayout)itemView.findViewById(R.id.flShowcaseOffer);

            ivShowcaseElement = (NetworkImageView)itemView.findViewById(R.id.ivShowcaseElement);
            ivShowcaseOffer = (ImageView)itemView.findViewById(R.id.ivShowcaseOffer);

            tvShowcaseTitle = (TextView)itemView.findViewById(R.id.tvShowcaseTitle);
            tvShowcaseSubtitle = (TextView)itemView.findViewById(R.id.tvShowcaseSubtitle);
            tvShowcaseOffer = (TextView)itemView.findViewById(R.id.tvShowcaseOffer);

            Typeface lato_regular = BNUtils.getLato_regular();
            Typeface lato_black = BNUtils.getLato_black();

            tvShowcaseTitle.setTypeface(lato_black);
            tvShowcaseSubtitle.setTypeface(lato_regular);
            tvShowcaseOffer.setTypeface(lato_black);
        }
    }

}