package uk.co.mezpahlan.thedroidpicture.rss.item;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import uk.co.mezpahlan.thedroidpicture.R;

/**
 * Activity for RssItem. Sets up the fragment that is received as a result of an
 * intent from RssFeed. Also sets up custom Toolbar and handles back button presses.
 */
public class ItemActivity extends AppCompatActivity {
    public static final String EXTRA_ITEM_URL = "ITEM_URL";
    public static final String EXTRA_ITEM_TITLE = "ITEM_TITLE";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rss_item);

        // Get the requested RSS Item Title and Link
        final Intent intent = getIntent();
        String itemTitle = intent.getStringExtra(EXTRA_ITEM_TITLE);
        String itemUrl = intent.getStringExtra(EXTRA_ITEM_URL);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (null == savedInstanceState) {
            initFragment(ItemFragment.newInstance(itemTitle, itemUrl));
        }
    }

    @Override
    public void onBackPressed() {
        final FragmentManager fragmentManager = getFragmentManager();
        final Fragment fragment = fragmentManager.findFragmentById(R.id.itemContentFrame);
        final View fullscreenViewPager = fragment.getView().findViewById(R.id.viewpager);
        final View contentView = fragment.getView().findViewById(R.id.content_view);

        if (fullscreenViewPager != null && View.VISIBLE == fullscreenViewPager.getVisibility()) {
            fullscreenViewPager.setVisibility(View.GONE);
            contentView.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
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
