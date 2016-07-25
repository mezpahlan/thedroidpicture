package uk.co.mezpahlan.thedroidpicture.rss.feed;

import java.util.List;

import uk.co.mezpahlan.thedroidpicture.data.BostonGlobe;
import uk.co.mezpahlan.thedroidpicture.data.RssRepository;
import uk.co.mezpahlan.thedroidpicture.data.model.RssFeed;

/**
 * Created by mpahlan on 24/07/16.
 */
public class FeedModelInteractor implements FeedMvp.ModelInteractor {

    private FeedMvp.Presenter feedPresenter;
    private RssRepository repository;

    public FeedModelInteractor(FeedMvp.Presenter feedPresenter) {
        this.feedPresenter = feedPresenter;

        // Using DI select the correct repository??
        repository = new BostonGlobe(this);
    }

    @Override
    public void fetch() {
        repository.loadFeed(this);
    }

    @Override
    public void onFetched(RssFeed rssFeed) {
        List<RssFeed.Item> rssItems = rssFeed.getChannel().getItem();
        feedPresenter.onLoadSuccess(rssItems);
    }
}
