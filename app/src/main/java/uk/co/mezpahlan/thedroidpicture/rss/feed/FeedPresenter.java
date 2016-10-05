package uk.co.mezpahlan.thedroidpicture.rss.feed;

import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;
import java.util.List;

import uk.co.mezpahlan.thedroidpicture.data.model.RssFeed;

/**
 * Created by mpahlan on 23/07/16.
 */
public class FeedPresenter implements FeedMvp.Presenter {

    private WeakReference<FeedMvp.View> rssView;
    private final FeedMvp.ModelInteractor modelInteractor;
    private boolean isConfigChaning;

    public FeedPresenter(@NonNull FeedMvp.View rssView) {
        this.rssView = new WeakReference<>(rssView);
        modelInteractor = new FeedModelInteractor(this);
    }

    @Override
    public void load(boolean isUserDriven) {
        // TODO: Do we check for retained instances here?
        rssView.get().showLoading(isUserDriven);
        modelInteractor.fetch();
    }

    @Override
    public void onConfigurationChanged(FeedMvp.View view) {
        rssView = new WeakReference<>(view);
        rssView.get().showLoading(false);
        modelInteractor.fetchCached();
    }

    @Override
    public void onDestroy(boolean isConfigChanging) {
        // Remove the reference to the View Layer as it is being destroyed
        rssView = null;
        this.isConfigChaning = isConfigChanging;
        if (!isConfigChanging) {
            // Tell the Model Layer to clean up
            modelInteractor.onDestroy();
        }
    }

    @Override
    public void onLoadSuccess(@NonNull List<RssFeed.Item> rssList) {
        // TODO: Update everything in the Recyclerview and notify data set changed or ....... something else?
        rssView.get().updateContent(rssList);
        rssView.get().showContent();
    }

    @Override
    public void onLoadError() {
        rssView.get().showError();
    }

    @Override
    public void onSelectRssItem(@NonNull RssFeed.Item rssItem) {
        rssView.get().showRssItem(rssItem.getTitle(), rssItem.getLink());
    }
}
