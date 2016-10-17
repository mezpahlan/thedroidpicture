package uk.co.mezpahlan.thedroidpicture.rss.item;

import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;
import java.util.List;

import uk.co.mezpahlan.thedroidpicture.data.model.RssItem;

/**
 * Presenter for RSS.Item.
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
        itemView.get().showLoading();
        modelInteractor.fetch(itemUrl);
    }

    @Override
    public void onLoadSuccess(@NonNull List<RssItem.Photo> photoList) {
        itemView.get().updateContent(photoList);
        itemView.get().showContent();
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
    public void selectShareItem() {
        itemView.get().shareItem();
    }

    @Override
    public void onConfigurationChanged(ItemMvp.View view, String itemUrl) {
        itemView = new WeakReference<>(view);
        itemView.get().showLoading();
        modelInteractor.fetchCached(itemUrl);
    }
}