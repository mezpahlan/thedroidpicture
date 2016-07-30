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
    public static final String EXTRA_ITEM_URL = "ITEM_URL";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rss_item);

        // Get the requested RSS Item
        String itemUrl = getIntent().getStringExtra(EXTRA_ITEM_URL);

        if (null == savedInstanceState) {
            initFragment(ItemFragment.newInstance(itemUrl));
        }
    }

    private void initFragment(Fragment itemFragment) {
        // Add the ItemFragment to the layout
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.itemContentFrame, itemFragment);
        transaction.commit();
    }
}
