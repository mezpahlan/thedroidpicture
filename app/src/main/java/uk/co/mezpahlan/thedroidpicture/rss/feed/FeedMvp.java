package uk.co.mezpahlan.thedroidpicture.rss.feed;

import android.support.annotation.NonNull;

import java.util.List;

import uk.co.mezpahlan.thedroidpicture.base.BaseMvp;
import uk.co.mezpahlan.thedroidpicture.data.model.RssFeed;

/**
 * MVP definitions for RSS Feed.
 */
public interface FeedMvp {
    interface View extends BaseMvp.LCEView <List<RssFeed.Item>> {
        void showRssItem(String title, String rssItemLink);
    }

    interface Presenter extends BaseMvp.Presenter <List<RssFeed.Item>> {
        void onConfigurationChanged(View view);
        void onSelectRssItem(@NonNull RssFeed.Item rssItem);
    }

    interface ModelInteractor extends BaseMvp.ModelInteractor <RssFeed> {
        void fetchCached();
    }
}
