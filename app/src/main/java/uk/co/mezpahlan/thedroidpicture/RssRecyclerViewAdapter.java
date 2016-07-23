package uk.co.mezpahlan.thedroidpicture;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by mpahlan on 20/07/16.
 */
public class RssRecyclerViewAdapter extends RecyclerView.Adapter<RssViewHolder> {

    private List<RssFeed.Item> itemList;

    public RssRecyclerViewAdapter(List<RssFeed.Item> itemList) {
        this.itemList = itemList;
    }

    @Override
    public RssViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rss_list, null);
        return new RssViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(RssViewHolder holder, int position) {
        // TODO: Should picasso be called from here?
        // TODO: Add placeholder and error images as BG icon
        final ImageView imageView = holder.getImage();
        final TextView textView = holder.getDescription();
        final RssFeed.Description rssItemDescription = itemList.get(position).getDescription();

        String text = rssItemDescription.getDescriptionText();
        String url = rssItemDescription.getDescriptionLink();

        Picasso.with(imageView.getContext())
                .load(url)
//                .placeholder(R.drawable.user_placeholder)
//                .error(R.drawable.user_placeholder_error)
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
}
