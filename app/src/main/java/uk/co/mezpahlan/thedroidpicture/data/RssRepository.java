package uk.co.mezpahlan.thedroidpicture.data;

import uk.co.mezpahlan.thedroidpicture.data.model.RssFeed;
import uk.co.mezpahlan.thedroidpicture.rss.feed.FeedModelInteractor;

/**
 * Created by mpahlan on 24/07/16.
 */
public interface RssRepository {

    void loadFeed(FeedModelInteractor feedModelInteractor);
    void onFeedLoaded(RssFeed rssFeed);
    void onFeedError();
    RssFeed.Item loadItem(String itemLink);

}
