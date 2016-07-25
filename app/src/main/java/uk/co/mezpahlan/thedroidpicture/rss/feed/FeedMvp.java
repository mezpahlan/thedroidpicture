package uk.co.mezpahlan.thedroidpicture.rss.feed;

import android.support.annotation.NonNull;

import java.util.List;

import uk.co.mezpahlan.thedroidpicture.base.BaseMvp;
import uk.co.mezpahlan.thedroidpicture.data.model.RssFeed;

/**
 * Created by mpahlan on 23/07/16.
 */
public interface FeedMvp {

    interface View extends BaseMvp.View <List<RssFeed.Item>> {

        // Only what the view is concerned with........ typically showing things?
        void showRssItem(String rssItemLink);

    }

    interface Presenter {

        void load(boolean isUserDrive);

        void onLoadSuccess(@NonNull List<RssFeed.Item> rssList);

        void onLoadError();

        void onSelectRssItem(@NonNull RssFeed.Item rssItem);

    }

    interface ModelInteractor extends BaseMvp.ModelInteractor <RssFeed> {

    }
}
