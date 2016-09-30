package uk.co.mezpahlan.thedroidpicture.rss.item;

import android.support.v4.view.PagerAdapter;
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
 * Created by mpahlan on 06/08/16.
 */
public class ItemViewPagerAdapter extends PagerAdapter {
    private List<RssItem.Photo> photosList;
    private ItemFragment.DetailLongClickListener detailLongClickListener;

    public ItemViewPagerAdapter(List<RssItem.Photo> photosList, ItemFragment.DetailLongClickListener detailLongClickListener) {
        this.photosList = photosList;
        this.detailLongClickListener = detailLongClickListener;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, final int position) {
        // TODO: Figure out how to pass the position in from the recyclerview onclick
        LayoutInflater inflater = LayoutInflater.from(collection.getContext());
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.pager_fullscreen_photo, collection, false);
        collection.addView(layout);

        final RssItem.Photo photo = photosList.get(position);

        ImageView fullscreenImageview = (ImageView) layout.findViewById(R.id.fullscreenImageView);
        TextView textView = (TextView) layout.findViewById(R.id.fullscreenTextView);

        Picasso.with(fullscreenImageview.getContext())
                .load(photo.getImageLink())
                .fit()
                .centerInside()
                .placeholder(R.drawable.logo_boston_globe)
                .error(R.drawable.logo_boston_globe)
                .into(fullscreenImageview);

        textView.setText(photo.getDescription());

        fullscreenImageview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                detailLongClickListener.onDetailLongClick();
                return false;
            }
        });

        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return photosList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}