package uk.co.mezpahlan.thedroidpicture.rss.detail;

/**
 * ModelInteractor for Rss.Detail.
 */
public class DetailModelInteractor implements DetailMvp.ModelInteractor {

    private final DetailMvp.Presenter detailPresenter;

    public DetailModelInteractor(DetailMvp.Presenter detailPresenter) {
        this.detailPresenter = detailPresenter;
    }
}
