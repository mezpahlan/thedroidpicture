package uk.co.mezpahlan.thedroidpicture.base;

/**
 * Base MVP definitions for operations that all implementations will use.
 */
public interface BaseMvp {

    interface LCEView <VT> {
        void showLoading(boolean active);
        void showContent(VT viewType);
        void showError();
    }

    interface Presenter {
        void onDestroy(boolean isConfigChanging);
    }

    interface ModelInteractor <MT> {
        void fetch();
        void onFetched(MT modelType);
        void onError();
        void onDestroy();

    }
}
