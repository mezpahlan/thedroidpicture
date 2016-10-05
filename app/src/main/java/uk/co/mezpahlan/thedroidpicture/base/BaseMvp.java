package uk.co.mezpahlan.thedroidpicture.base;

/**
 * Base MVP definitions for operations that all implementations will use.
 */
public interface BaseMvp {

    interface LCEView <VT> {
        void showLoading(boolean active);
        void showContent();
        void showError();
        void updateContent(VT viewType);
    }

    interface Presenter <PT> {
        void load(boolean isUserDrive);
        void onLoadSuccess(PT presenterType);
        void onLoadError();
        void onDestroy(boolean isConfigChanging);
    }

    interface ModelInteractor <MT> {
        void fetch();
        void onFetched(MT modelType);
        void onError();
        void onDestroy();
    }
}
