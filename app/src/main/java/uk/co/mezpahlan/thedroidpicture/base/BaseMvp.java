package uk.co.mezpahlan.thedroidpicture.base;

/**
 * Base MVP definitions for operations that all implementations will use.
 */
public interface BaseMvp {

    interface LCEView <T> {
        void showLoading(boolean active);
        void showContent(T viewType);
        void showError();
    }

    interface Presenter {
        void onDestroy(boolean isConfigChanging);
    }

    interface ModelInteractor {
        void onDestroy();

    }
}
