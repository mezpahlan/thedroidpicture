package uk.co.mezpahlan.thedroidpicture.rss.feed;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import uk.co.mezpahlan.thedroidpicture.R;
import uk.co.mezpahlan.thedroidpicture.data.model.RssFeed;

/**
 * RecyclerViewAdapter for Rss.Feed. Also responsible for click events in the bound item.
 */
public class FeedRecyclerViewAdapter extends RecyclerView.Adapter<FeedRecyclerViewAdapter.FeedViewHolder> {

    private static final String ACTION_COLLAPSE = "ACTION_COLLAPSE";
    private static final String ACTION_TOGGLE = "ACTION_TOGGLE";

    private List<RssFeed.Item> itemList = new ArrayList<>(0);
    private FeedFragment.RssFeedItemClickListener rssFeedItemClickListener;
    private int currentExpandedDescriptionPosition = -1;

    public void updateItems(List<RssFeed.Item> itemList) {
        // Remove any items from list1 that aren't in list 2
        this.itemList.removeAll(itemList);
        // Add list 2 to the remaining items from list 1
        this.itemList.addAll(itemList);
    }

    public void setRssFeedItemClickListener(FeedFragment.RssFeedItemClickListener rssFeedItemClickListener) {
        this.rssFeedItemClickListener = rssFeedItemClickListener;
    }

    @Override
    public FeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_rss_feed, null);
        return new FeedViewHolder(layoutView, rssFeedItemClickListener);
    }

    @Override
    public void onBindViewHolder(FeedViewHolder holder, int position, List<Object> payloads) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads);
        } else {
            TextView descriptionView = holder.getDescription();
            final String action = (String) payloads.get(0);
            if (action == ACTION_COLLAPSE) {
                descriptionView.setVisibility(View.GONE);
                return;
            }
            if (action == ACTION_TOGGLE) {
                if (descriptionView.getVisibility() == View.VISIBLE) {
                    descriptionView.setVisibility(View.GONE);
                    currentExpandedDescriptionPosition = -1;
                } else {
                    descriptionView.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public void onBindViewHolder(FeedViewHolder holder, int position) {
        final TextView titleView = holder.getTitle();
        final ImageView imageView = holder.getImage();
        final TextView descriptionView = holder.getDescription();
        final RssFeed.Item item = itemList.get(position);
        final RssFeed.Description rssItemDescription = item.getDescription();

        String title = item.getTitle();
        String description = rssItemDescription.getDescriptionText();
        String url = rssItemDescription.getDescriptionLink();

        titleView.setText(title);
        descriptionView.setText(description);

        // Full update
        Picasso.with(imageView.getContext())
                .load(url)
                .fit()
                .centerCrop()
                .placeholder(R.drawable.logo_boston_globe)
                .error(R.drawable.logo_boston_globe)
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        if (itemList != null) {
            return itemList.size();
        } else {
            return 0;
        }
    }

    public RssFeed.Item getRssFeedItemWithPosition(int position) {
        return itemList.get(position);
    }

    public class FeedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private FeedFragment.RssFeedItemClickListener rssFeedItemClickListener;
        private TextView title;
        private ImageView image;
        private TextView description;

        public FeedViewHolder(View itemView, FeedFragment.RssFeedItemClickListener rssFeedItemClickListener) {
            super(itemView);
            this.rssFeedItemClickListener = rssFeedItemClickListener;

            title = (TextView) itemView.findViewById(R.id.rss_title);
            image = (ImageView) itemView.findViewById(R.id.rss_image);
            description = (TextView) itemView.findViewById(R.id.rss_description);

            title.setOnClickListener(this);
            image.setOnClickListener(this);
            description.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rss_image:
                    // Tell the presenter that we want to do something with the supplied item
                    int position = getAdapterPosition();
                    RssFeed.Item rssFeedItem = getRssFeedItemWithPosition(position);
                    rssFeedItemClickListener.onRssFeedItemClick(rssFeedItem);
                    break;
                case R.id.rss_title:
                    // Collapse the previously expanded item description
                    if (currentExpandedDescriptionPosition != -1 && currentExpandedDescriptionPosition != getAdapterPosition()) {
                        notifyItemChanged(currentExpandedDescriptionPosition, ACTION_COLLAPSE);
                    }
                    // Toggle the  current item description
                    notifyItemChanged(getAdapterPosition(), ACTION_TOGGLE);
                    currentExpandedDescriptionPosition = getAdapterPosition();
                    break;
                case R.id.rss_description:
                    // The description is GONE on creation therefore if we have reached here we must have
                    // previously made this visible by clicking the title. Make it disappear.
                    notifyItemChanged(getAdapterPosition(), ACTION_COLLAPSE);
                    break;
            }
        }

        public TextView getTitle() { return title; }
        public ImageView getImage() {
            return image;
        }
        public TextView getDescription() {
            return description;
        }
    }
}