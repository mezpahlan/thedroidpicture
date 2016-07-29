package uk.co.mezpahlan.thedroidpicture.rss.feed;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import uk.co.mezpahlan.thedroidpicture.R;
import uk.co.mezpahlan.thedroidpicture.data.model.RssFeed;

/**
 * Created by mpahlan on 20/07/16.
 */
public class FeedRecyclerViewAdapter extends RecyclerView.Adapter<FeedRecyclerViewAdapter.FeedViewHolder> {

    private List<RssFeed.Item> itemList;

    public FeedRecyclerViewAdapter(List<RssFeed.Item> itemList) {
        this.itemList = itemList;
    }

    @Override
    public FeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rss_list, null);
        return new FeedViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(FeedViewHolder holder, int position) {
        // TODO: Should picasso be called from here?
        final ImageView imageView = holder.getImage();
        final TextView textView = holder.getDescription();
        final RssFeed.Description rssItemDescription = itemList.get(position).getDescription();

        String text = rssItemDescription.getDescriptionText();
        String url = rssItemDescription.getDescriptionLink();

        Picasso.with(imageView.getContext())
                .load(url)
                .placeholder(R.drawable.logo_boston_globe)
                .error(R.drawable.logo_boston_globe)
                .into(imageView);

        textView.setText(text);
    }

    @Override
    public int getItemCount() {
        if (itemList != null) {
            return itemList.size();
        } else {
            return 0;
        }
    }

    public class FeedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView image;
        private TextView description;

        public FeedViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            image = (ImageView) itemView.findViewById(R.id.rss_image);
            description = (TextView) itemView.findViewById(R.id.rss_description);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(v.getContext(), "Clicked Position = " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
        }

        public ImageView getImage() {
            return image;
        }

        public TextView getDescription() {
            return description;
        }
    }
}
