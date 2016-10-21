package uk.co.mezpahlan.thedroidpicture.rss.detail;

import android.support.annotation.NonNull;

import java.util.List;

import uk.co.mezpahlan.thedroidpicture.base.BaseMvp;
import uk.co.mezpahlan.thedroidpicture.data.model.RssItem;

/**
 * Interface operations for Rss.Detail.
 */
public interface DetailMvp {
    interface View extends BaseMvp.LCEView<List<RssItem.Photo>> {
        void onSelectSavePicture(int currentPosition);
        void savePicture(int currentPosition);
        void onSelectSharePictureAndText(int currentPosition);
        void sharePictureAndText(int currentPosition);
        void onSelectSetAsWallpaper(int currentPosition);
        void setAsWallpaper(int currentPosition);
    }

    interface Presenter {
        // TODO: Move this into Base
        // TODO: Create Base type
        void load();
        void onLoadSuccess();
        void onLoadError();
        void selectSharePictureAndText(@NonNull int position);
        void selectSavePicture(@NonNull int position);
        void selectSetAsWallpaper(@NonNull int position);
        void onConfigurationChanged(DetailMvp.View view);
    }

    interface ModelInteractor {
        // Doesn't do anything at the moment.
    }
}
