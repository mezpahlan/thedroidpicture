package uk.co.mezpahlan.thedroidpicture.rss.item;

import android.support.annotation.NonNull;

import java.util.List;

import uk.co.mezpahlan.thedroidpicture.base.BaseMvp;
import uk.co.mezpahlan.thedroidpicture.data.model.RssItem;

/**
 * Created by mpahlan on 27/07/16.
 */
public interface ItemMvp {
    interface View extends BaseMvp.LCEView<List<RssItem.Photo>> {
        void setTitle(String title);
        void showExpandedPicture(int position);
    }

    interface Presenter {
        // TODO: Move this into Base
        // TODO: Create Base type
        void load(String itemUrl);
        void onLoadSuccess(@NonNull List<RssItem.Photo> photos);
        void onLoadError();
        void onSelectPhoto(@NonNull int position);
        void onConfigurationChanged(View view, String itemUrl);
    }

    interface ModelInteractor {
        void fetch(String itemUrl);
        void fetchCached(String itemUrl);
        void onFetched(RssItem rssItem);
        void onError();
    }
}
