package uk.co.mezpahlan.thedroidpicture.rss.item;

import android.support.annotation.NonNull;
import android.view.View;

import java.util.List;

import uk.co.mezpahlan.thedroidpicture.R;
import uk.co.mezpahlan.thedroidpicture.data.model.RssItem;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by mpahlan on 28/07/16.
 */
public class ItemPresenter implements ItemMvp.Presenter {
    private final ItemMvp.View itemView;
    private final ItemMvp.ModelInteractor modelInteractor;

    public ItemPresenter(@NonNull ItemMvp.View rssView) {
        this.itemView = checkNotNull(rssView, "rssView cannot be null");
        modelInteractor = new ItemModelInteractor(this);
    }

    @Override
    public void load(String itemUrl) {
        // TODO: Do we check for retained instances here?
        // TODO: Refactor showLoading remove boolean??
        itemView.showLoading(true);
        modelInteractor.fetch(itemUrl);
    }

    @Override
    public void onLoadSuccess(@NonNull List<RssItem.Photo> photoList) {
        // TODO: Update everything in the Recyclerview and notify data set changed or ....... something else?
        itemView.showContent(photoList);
    }

    @Override
    public void onLoadError() {
        itemView.showError();
    }

    @Override
    public void onSelectPhoto(int position) {
        checkNotNull(position, "position cannot be null");
        itemView.showExpandedPicture(position);
    }
}
