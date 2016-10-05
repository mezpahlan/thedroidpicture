package uk.co.mezpahlan.thedroidpicture.rss.detail;

/**
 * ModelInteractor for RssItemDetail.
 */
public class DetailModelInteractor implements DetailMvp.ModelInteractor {

    private final DetailMvp.Presenter detailPresenter;

    public DetailModelInteractor(DetailMvp.Presenter detailPresenter) {
        this.detailPresenter = detailPresenter;
    }
}
