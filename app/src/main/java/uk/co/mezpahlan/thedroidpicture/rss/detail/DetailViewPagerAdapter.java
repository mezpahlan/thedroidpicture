package uk.co.mezpahlan.thedroidpicture.rss.detail;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import uk.co.mezpahlan.thedroidpicture.R;
import uk.co.mezpahlan.thedroidpicture.data.model.RssItem;

/**
 * ViewPagerAdapter for Rss.Detail.
 */

public class DetailViewPagerAdapter extends PagerAdapter {
    private List<RssItem.Photo> photosList;

    public DetailViewPagerAdapter(List<RssItem.Photo> photosList) {
        this.photosList = photosList;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, final int position) {
        LayoutInflater inflater = LayoutInflater.from(collection.getContext());
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.pager_fullscreen_photo, collection, false);
        collection.addView(layout);

        final RssItem.Photo photo = photosList.get(position);

        ImageView fullscreenImageview = (ImageView) layout.findViewById(R.id.fullscreen_image_view);

        Picasso.with(fullscreenImageview.getContext())
                .load(photo.getImageLink())
                .fit()
                .centerInside()
                .placeholder(R.drawable.logo_boston_globe)
                .error(R.drawable.logo_boston_globe)
                .into(fullscreenImageview);

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
