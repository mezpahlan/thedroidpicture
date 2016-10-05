package uk.co.mezpahlan.thedroidpicture.rss.detail;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import uk.co.mezpahlan.thedroidpicture.R;

/**
 * Activity for RssItemDetail. Sets up the fragment that is received as a result of an
 * intent from RssItem. Also sets up custom Toolbar and handles back button presses.
 */

public class DetailActivity extends AppCompatActivity {
    public static final String EXTRA_DETAIL_POSITION = "DETAIL_POSITION";
    public static final String EXTRA_DETAIL_PHOTO_LIST = "DETAIL_PHOTO_LIST";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rss_detail);

        final Intent intent = getIntent();
        // Get the list of photos
        Parcelable wrappedPhotoList = intent.getParcelableExtra(EXTRA_DETAIL_PHOTO_LIST);
        // Get the requested position for the ViewPager
        // TODO: Should the default value be -1 so that we can check for some error?
        int position = intent.getIntExtra(EXTRA_DETAIL_POSITION, 0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (null == savedInstanceState) {
            // TODO: Change to DetailFragment
            initFragment(DetailFragment.newInstance(wrappedPhotoList, position));
        }
    }


    // TODO: Check if we need to override back button behaviour. If not remove Class comment.
//    @Override
//    public void onBackPressed() {
//        final FragmentManager fragmentManager = getFragmentManager();
//        final Fragment fragment = fragmentManager.findFragmentById(R.id.itemContentFrame);
//        final View fullscreenViewPager = fragment.getView().findViewById(R.id.viewpager);
//        final View contentView = fragment.getView().findViewById(R.id.content_view);
//
//        if (fullscreenViewPager != null && View.VISIBLE == fullscreenViewPager.getVisibility()) {
//            fullscreenViewPager.setVisibility(View.GONE);
//            contentView.setVisibility(View.VISIBLE);
//        } else {
//            super.onBackPressed();
//        }
//    }

    private void initFragment(Fragment itemFragment) {
        // Add the ItemFragment to the layout
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.detailContentFrame, itemFragment);
        transaction.commit();
    }

}
