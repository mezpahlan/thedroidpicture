package uk.co.mezpahlan.thedroidpicture.rss.feed;

import android.support.annotation.NonNull;

import java.util.List;

import uk.co.mezpahlan.thedroidpicture.base.BaseMvp;
import uk.co.mezpahlan.thedroidpicture.data.model.RssFeed;

/**
 * Created by mpahlan on 23/07/16.
 */
public interface FeedMvp {
    interface View extends BaseMvp.LCEView <List<RssFeed.Item>> {
        // Only what the view is concerned with........ typically showing things?
        void showRssItem(String title, String rssItemLink);
    }

    interface Presenter {
        void onConfigurationChanged(View view);
        void load(boolean isUserDrive);
        void onLoadSuccess(@NonNull List<RssFeed.Item> rssList);
        void onLoadError();
        void onSelectRssItem(@NonNull RssFeed.Item rssItem);

        // TODO: Add this to base MVP
        void onDestroy(boolean isConfigChanging);
    }

    interface ModelInteractor extends BaseMvp.ModelInteractor {
        void fetch();
        void fetchCached();
        void onFetched(RssFeed rssFeed);
        void onError();
    }
}
