package uk.co.mezpahlan.thedroidpicture.rss.item;

import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;
import java.util.List;

import uk.co.mezpahlan.thedroidpicture.data.model.RssItem;

/**
 * Created by mpahlan on 28/07/16.
 */
public class ItemPresenter implements ItemMvp.Presenter {
    private WeakReference<ItemMvp.View> itemView;
    private final ItemMvp.ModelInteractor modelInteractor;

    public ItemPresenter(@NonNull ItemMvp.View rssView) {
        this.itemView = new WeakReference<>(rssView);
        modelInteractor = new ItemModelInteractor(this);
    }

    @Override
    public void load(String itemUrl) {
        // TODO: Do we check for retained instances here?
        // TODO: Refactor showLoading remove boolean??
        itemView.get().showLoading(true);
        modelInteractor.fetch(itemUrl);
    }

    @Override
    public void onLoadSuccess(@NonNull List<RssItem.Photo> photoList) {
        // TODO: Update everything in the Recyclerview and notify data set changed or ....... something else?
        itemView.get().showContent(photoList);
    }

    @Override
    public void onLoadError() {
        itemView.get().showError();
    }

    @Override
    public void onSelectPhoto(int position) {
        itemView.get().showExpandedPicture(position);
    }

    @Override
    public void onConfigurationChanged(ItemMvp.View view, String itemUrl) {
        itemView = new WeakReference<>(view);
        itemView.get().showLoading(false);
        modelInteractor.fetchCached(itemUrl);
    }
}