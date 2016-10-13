package uk.co.mezpahlan.thedroidpicture.base;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * StateMaintainer class. Each MVP triad will get a reference in this StateMaintainer.
 * No matter how the user changes configuration the newly created View layer will have
 * access to the previous state via this mechanism.
 */
public class StateMaintainer {
    private static final String TAG = "StateMaintainer";

    private final String stateMaintenerTag;
    private final WeakReference<FragmentManager> fragmentManager;
    private HeadlessRetainedFragment headlessRetainedFragment;
    private boolean isRecreating;

    public StateMaintainer(@NonNull FragmentManager fragmentManager, @NonNull String stateMaintainerTAG) {
        this.fragmentManager = new WeakReference<>(fragmentManager);
        stateMaintenerTag = stateMaintainerTAG;
    }

    public boolean isFirstTimeIn() {
        try {
            // Try and locate a previously created StateMaintainer
            headlessRetainedFragment = (HeadlessRetainedFragment) fragmentManager.get().findFragmentByTag(stateMaintenerTag);

            if (headlessRetainedFragment == null) {
                // Otherwise create one afresh and add it to the the fragment manager
                headlessRetainedFragment = new HeadlessRetainedFragment();
                fragmentManager.get().beginTransaction().add(headlessRetainedFragment, stateMaintenerTag).commit();

                // This implies that the view is starting for the first time. i.e. not recreating
                // from a configuration change.
                isRecreating = false;
                return true;
            } else {
                // Otherwise we assume that it is recreating from a configuration change
                isRecreating = true;
                return false;
            }
        } catch (NullPointerException e) {
            return false;
        }
    }

    public boolean wasRecreated() {
        return isRecreating;
    }

    public void put(String key, Object obj) {
        headlessRetainedFragment.put(key, obj);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key)  {
        return headlessRetainedFragment.get(key);

    }

    public boolean hasKey(String key) {
        return headlessRetainedFragment.get(key) != null;
    }


    public static class HeadlessRetainedFragment extends Fragment {
        private HashMap<String, Object> data = new HashMap<>();

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
        }

        public void put(String key, Object obj) {
            data.put(key, obj);
        }

        @SuppressWarnings("unchecked")
        public <T> T get(String key) {
            return (T) data.get(key);
        }
    }

}