package uk.co.mezpahlan.thedroidpicture.base;

/**
 * Created by mpahlan on 24/07/16.
 */
public interface BaseMvp {

    interface LCEView <T> {

        void showLoading(boolean active);

        void showContent(T viewType);

        void showError();
    }

}
