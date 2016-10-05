package uk.co.mezpahlan.thedroidpicture.rss.detail;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;

import uk.co.mezpahlan.thedroidpicture.R;

/**
 * Activity for RssItemDetail. Sets up the fragment that is received as a result of an
 * intent from RssItem.
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

        if (null == savedInstanceState) {
            initFragment(DetailFragment.newInstance(wrappedPhotoList, position));
        }
    }

    private void initFragment(Fragment itemFragment) {
        // Add the ItemFragment to the layout
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.detailContentFrame, itemFragment);
        transaction.commit();
    }
}
