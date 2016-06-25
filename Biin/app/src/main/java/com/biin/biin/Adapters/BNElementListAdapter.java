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

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.biin.biin.BiinApp;
import com.biin.biin.Components.BNProgressViewHolder;
import com.biin.biin.Components.Listeners.BNLoadMoreElementsListener;
import com.biin.biin.ElementsActivity;
import com.biin.biin.Entities.BNElement;
import com.biin.biin.R;
import com.biin.biin.Utils.BNUtils;

import java.util.List;

/**
 * Created by ramirezallan on 5/5/16.
 */
public class BNElementListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "BNElementListAdapter";
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

    public BNElementListAdapter(Context context, List<BNElement> elements) {
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
            v.setLayoutParams(new RecyclerView.LayoutParams(BNUtils.getWidth(), (int)(68 * BNUtils.getDensity())));
            holder = new BNProgressViewHolder(v);

        }else if(viewType == VIEW_ITEM){
        // load more end

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bnelement_item, parent, false);
            v.setLayoutParams(new RecyclerView.LayoutParams(BNUtils.getWidth(), (BNUtils.getWidth() / 2) + (int)(68 * BNUtils.getDensity())));
            holder = new BNElementViewHolder(v);

        // load more start
        }
        // load more end

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        // load more start
        if(viewHolder instanceof BNProgressViewHolder){
            BNProgressViewHolder holder = (BNProgressViewHolder)viewHolder;
            holder.progressBar.setIndeterminate(true);
        }else if(viewHolder instanceof BNElementViewHolder) {
            BNElementViewHolder holder = (BNElementViewHolder) viewHolder;
            // load more end

            final BNElement item = elements.get(position);
            TableRow.LayoutParams params = new TableRow.LayoutParams(BNUtils.getWidth(), (BNUtils.getWidth() / 2) + (int) (68 * BNUtils.getDensity()));

//        loadElementImage(item.getMedia().get(0).getUrl(), holder); //element.media.url
            imageLoader.get(item.getMedia().get(0).getUrl(), ImageLoader.getImageListener(holder.ivElement, R.drawable.bg_feedback, R.drawable.biin));
            holder.ivElement.setImageUrl(item.getMedia().get(0).getUrl(), imageLoader);
            holder.ivElement.setBackgroundColor(item.getShowcase().getSite().getOrganization().getPrimaryColor());

//        loadOrganizationImage(item.getShowcase().getSite().getMedia().get(0).getUrl(), holder);
            imageLoader.get(item.getShowcase().getSite().getMedia().get(0).getUrl(), ImageLoader.getImageListener(holder.ivOrganization, R.drawable.bg_feedback, R.drawable.biin));
            holder.ivOrganization.setImageUrl(item.getShowcase().getSite().getMedia().get(0).getUrl(), imageLoader);
            holder.ivOrganization.setBackgroundColor(item.getShowcase().getSite().getOrganization().getPrimaryColor());

            holder.rlElementLabel.setBackgroundColor(item.getShowcase().getSite().getOrganization().getPrimaryColor());
            holder.tvTitle.setText(item.getTitle());
            holder.tvTitle.setTextColor(item.getShowcase().getSite().getOrganization().getSecondaryColor());
            holder.tvSubtitle.setText(item.getShowcase().getSite().getTitle());
            holder.tvSubtitle.setTextColor(item.getShowcase().getSite().getOrganization().getSecondaryColor());
            holder.tvSubtitleLocation.setText(item.getShowcase().getSite().getSubTitle());
            holder.tvSubtitleLocation.setTextColor(item.getShowcase().getSite().getOrganization().getSecondaryColor());
//        holder.tvPrice.setText(item.getListPrice());
            holder.tvPrice.setTextColor(item.getShowcase().getSite().getOrganization().getSecondaryColor());
            holder.tvDiscount.setText("Ȼ" + item.getPrice());
            holder.tvDiscount.setTextColor(item.getShowcase().getSite().getOrganization().getSecondaryColor());

            if (item.isHasDiscount() && !item.getDiscount().isEmpty()) {
                holder.tvOffer.setText("-" + item.getDiscount() + "%");
                holder.tvOffer.setTextColor(item.getShowcase().getSite().getOrganization().getSecondaryColor());
                holder.ivOffer.setColorFilter(item.getShowcase().getSite().getOrganization().getPrimaryColor());
                holder.flOffer.setVisibility(View.VISIBLE);
            } else {
                holder.flOffer.setVisibility(View.GONE);
            }
//        holder.pbElement.getProgressDrawable().setColorFilter(item.getShowcase().getSite().getOrganization().getSecondaryColor(), PorterDuff.Mode.SRC_ATOP);

            holder.cvElement.setLayoutParams(params);

            android.view.ViewGroup.LayoutParams layoutParams = holder.ivElement.getLayoutParams();
            layoutParams.width = BNUtils.getWidth();
            layoutParams.height = (BNUtils.getWidth() / 2);
            holder.ivElement.setLayoutParams(layoutParams);

            holder.cvElement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, ElementsActivity.class);
                    SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.preferences_key), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(BNUtils.BNStringExtras.BNElement, item.getIdentifier());
                    editor.commit();
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

    public BNElementListAdapter(Context context, List<BNElement> elements, RecyclerView recyclerView) {
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

    public BNElement getElement(int position){
        return elements.get(position);
    }

    public static class BNElementViewHolder extends RecyclerView.ViewHolder{

        protected CardView cvElement;
        protected RelativeLayout rlElementLabel;
        protected FrameLayout flOffer;
        protected ImageView ivOffer;
        protected NetworkImageView ivElement, ivOrganization;
        protected TextView tvTitle, tvSubtitle, tvSubtitleLocation, tvPrice, tvDiscount, tvOffer;

        public BNElementViewHolder(View itemView) {
            super(itemView);

            cvElement = (CardView)itemView.findViewById(R.id.cvElement);
            rlElementLabel = (RelativeLayout)itemView.findViewById(R.id.rlElementLabel);
            flOffer = (FrameLayout)itemView.findViewById(R.id.flElementOffer);

            ivElement = (NetworkImageView)itemView.findViewById(R.id.ivElement);
            ivOrganization = (NetworkImageView)itemView.findViewById(R.id.ivOrganization);
            ivOffer = (ImageView)itemView.findViewById(R.id.ivElementOffer);

            tvTitle = (TextView)itemView.findViewById(R.id.tvTitle);
            tvSubtitle = (TextView)itemView.findViewById(R.id.tvSubtitle);
            tvSubtitleLocation = (TextView)itemView.findViewById(R.id.tvSubtitleLocation);
            tvPrice = (TextView)itemView.findViewById(R.id.tvPrice);
            tvDiscount = (TextView)itemView.findViewById(R.id.tvDiscount);
            tvOffer = (TextView)itemView.findViewById(R.id.tvElementOffer);

            Typeface lato_regular = BNUtils.getLato_regular();
            Typeface lato_black = BNUtils.getLato_black();

            tvTitle.setTypeface(lato_regular);
            tvSubtitle.setTypeface(lato_black);
            tvSubtitleLocation.setTypeface(lato_regular);
            tvPrice.setTypeface(lato_regular);
            tvDiscount.setTypeface(lato_regular);
            tvOffer.setTypeface(lato_black);
        }
    }

}
