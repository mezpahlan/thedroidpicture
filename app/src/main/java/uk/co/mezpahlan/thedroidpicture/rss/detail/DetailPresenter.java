package uk.co.mezpahlan.thedroidpicture.rss.detail;

import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;

/**
 * Presenter for Rss.Detail.
 */
public class DetailPresenter implements DetailMvp.Presenter {
    private WeakReference<DetailMvp.View> detailView;
    private final DetailModelInteractor modelInteractor; // Acknowledged: This doesn't do anything at the moment.

    public DetailPresenter(@NonNull DetailMvp.View detailView) {
        this.detailView = new WeakReference<>(detailView);
        modelInteractor = new DetailModelInteractor(this);
    }

    @Override
    public void load() {
        detailView.get().showLoading();
        onLoadSuccess();
    }

    @Override
    public void onLoadSuccess() {
        detailView.get().showContent();
    }

    @Override
    public void onLoadError() {
        detailView.get().showError();
    }

    @Override
    public void selectSharePictureAndText(@NonNull int position) {
        detailView.get().sharePictureAndText(position);
    }

    @Override
    public void onConfigurationChanged(DetailMvp.View view) {
        detailView = new WeakReference<>(view);
        detailView.get().showLoading();
        detailView.get().showContent();
    }
}