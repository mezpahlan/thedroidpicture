package uk.co.mezpahlan.thedroidpicture.rss.feed;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import uk.co.mezpahlan.thedroidpicture.R;
import uk.co.mezpahlan.thedroidpicture.data.model.RssFeed;

/**
 * Created by mpahlan on 20/07/16.
 */
public class FeedRecyclerViewAdapter extends RecyclerView.Adapter<FeedRecyclerViewAdapter.FeedViewHolder> {

    private List<RssFeed.Item> itemList;
    private FeedFragment.RssFeedItemClickListener rssFeedItemClickListener;
    private int expandedDescriptionPosition = -1;

    public FeedRecyclerViewAdapter(List<RssFeed.Item> itemList, FeedFragment.RssFeedItemClickListener rssFeedItemClickListener) {
        this.itemList = itemList;
        this.rssFeedItemClickListener = rssFeedItemClickListener;
    }

    @Override
    public FeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_rss_feed, null);
        return new FeedViewHolder(layoutView, rssFeedItemClickListener);
    }

    @Override
    public void onBindViewHolder(FeedViewHolder holder, int position) {
        // TODO: Blink is not caused by Picasso reloading.
        // TODO: Look at clicking on title vs clicking on description to collapse.
        // TODO: One causes blink the other doesn't....

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
                .placeholder(R.drawable.logo_boston_globe)
                .error(R.drawable.logo_boston_globe)
                .into(imageView);

        if (position != expandedDescriptionPosition) {
            descriptionView.setVisibility(View.GONE);
        } else {
            // TODO: Rethink comment
            // Partial update. Only update the description to be visible
            descriptionView.setVisibility(View.VISIBLE);
        }
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
                case R.id.rss_description:
                    // Nothing is exapnded
                    // TODO
                    expandedDescriptionPosition = -1;
                    notifyItemChanged(getLayoutPosition(), true);
                    break;
                case R.id.rss_title:
                    if (expandedDescriptionPosition >= 0) {
                        // Store the previously expanded item
                        int prev = expandedDescriptionPosition;
                        notifyItemChanged(prev);
                    }

                    if (expandedDescriptionPosition == getLayoutPosition()) {
                        expandedDescriptionPosition = -1;
                        notifyItemChanged(getLayoutPosition());
                    } else {
                        // Always expand the item we have clicked on
                        expandedDescriptionPosition = getLayoutPosition();
                        notifyItemChanged(expandedDescriptionPosition);
                    }
                    break;
                case R.id.rss_image:
                    int position = getLayoutPosition();
                    RssFeed.Item rssFeedItem = getRssFeedItemWithPosition(position);
                    rssFeedItemClickListener.onRssFeedItemClick(rssFeedItem);
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
