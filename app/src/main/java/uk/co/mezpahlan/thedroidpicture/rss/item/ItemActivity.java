package uk.co.mezpahlan.thedroidpicture.rss.item;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import uk.co.mezpahlan.thedroidpicture.R;

/**
 * Created by mpahlan on 27/07/16.
 */
public class ItemActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (null == savedInstanceState) {
            initFragment(ItemFragment.newInstance());
        }
    }

    private void initFragment(Fragment itemFragment) {
        // Add the FeedFragment to the layout
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.feedContentFrame, itemFragment);
        transaction.commit();
    }
}
