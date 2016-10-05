package uk.co.mezpahlan.thedroidpicture.rss.detail;

import android.support.annotation.NonNull;

import java.util.List;

import uk.co.mezpahlan.thedroidpicture.base.BaseMvp;
import uk.co.mezpahlan.thedroidpicture.data.model.RssItem;

/**
 * Interface operations for Rss Item Detail.
 */
public interface DetailMvp {
    interface View extends BaseMvp.LCEView<List<RssItem.Photo>> {
    }

    interface Presenter {
        // TODO: Move this into Base
        // TODO: Create Base type
        void load();
        void onLoadSuccess();
        void onLoadError();
        void onSelectPhoto(@NonNull int position);
        void onConfigurationChanged(DetailMvp.View view);
    }

    interface ModelInteractor {
        // Doesn't do anything at the moment.
    }
}
