package uk.co.mezpahlan.thedroidpicture.rss.item;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import uk.co.mezpahlan.thedroidpicture.R;
import uk.co.mezpahlan.thedroidpicture.data.model.RssItem;

/**
 * RecyclerViewAdapter for RssItem.
 */
public class ItemRecyclerViewAdapter extends RecyclerView.Adapter<ItemRecyclerViewAdapter.ItemViewHolder> {

    private List<RssItem.Photo> itemList;
    private final ItemFragment.PhotoClickListener photoClickListener;

    public ItemRecyclerViewAdapter(ItemFragment.PhotoClickListener photoClickListener) {
        this.photoClickListener = photoClickListener;
    }

    public void setItemList(List<RssItem.Photo> itemList) {
        this.itemList = itemList;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_rss_item, null);
        return new ItemViewHolder(layoutView, photoClickListener);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        final ImageView imageView = holder.getImage();
        final TextView textView = holder.getDescription();

        final RssItem.Photo photo = itemList.get(position);
        final String imageLink = photo.getImageLink();
        final String text = photo.getDescription();

        Picasso.with(imageView.getContext())
                .load(imageLink)
                .fit()
                .centerCrop()
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

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ItemFragment.PhotoClickListener photoClickListener;
        private ImageView image;
        private TextView description;

        public ItemViewHolder(View itemView, ItemFragment.PhotoClickListener photoClickListener) {
            super(itemView);
            this.photoClickListener = photoClickListener;

            itemView.setOnClickListener(this);
            image = (ImageView) itemView.findViewById(R.id.item_image);
            description = (TextView) itemView.findViewById(R.id.item_description);
        }

        @Override
        public void onClick(View v) {
            photoClickListener.onItemClick(getLayoutPosition());
        }

        public ImageView getImage() {
            return image;
        }

        public TextView getDescription() {
            return description;
        }
    }
}
