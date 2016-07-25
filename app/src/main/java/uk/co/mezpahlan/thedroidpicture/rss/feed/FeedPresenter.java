package uk.co.mezpahlan.thedroidpicture.rss.feed;

import android.support.annotation.NonNull;

import java.util.List;

import uk.co.mezpahlan.thedroidpicture.data.model.RssFeed;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by mpahlan on 23/07/16.
 */
public class FeedPresenter implements FeedMvp.Presenter {

    private final FeedMvp.View rssView;
    private final FeedMvp.ModelInteractor modelInteractor;

    public FeedPresenter(@NonNull FeedMvp.View rssView) {
        this.rssView = checkNotNull(rssView, "rssView cannot be null");
        modelInteractor = new FeedModelInteractor(this);
    }

    @Override
    public void load(boolean isUserDriven) {
        // TODO: Do we check for retained instances here?
        modelInteractor.fetch();
    }

    @Override
    public void onLoadSuccess(@NonNull List<RssFeed.Item> rssList) {
        // TODO: Update everything in the Recyclerview and notify data set changed or ....... something else?
        rssView.showContent(rssList);
    }

    @Override
    public void onLoadError() {
        rssView.showError();
    }

    @Override
    public void onSelectRssItem(@NonNull RssFeed.Item rssItem) {
        checkNotNull(rssItem, "rssItem cannot be null");
        rssView.showRssItem(rssItem.getLink());
    }

}
