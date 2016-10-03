package uk.co.mezpahlan.thedroidpicture.base;

/**
 * Base Mvp definitions for operations that all implementations will use.
 */
public interface BaseMvp {

    interface LCEView <T> {
        void showLoading(boolean active);
        void showContent(T viewType);
        void showError();
    }

    interface ModelInteractor {
        void onDestroy();
    }
}
