package uk.co.mezpahlan.thedroidpicture.rss.detail;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;

import uk.co.mezpahlan.thedroidpicture.R;
import uk.co.mezpahlan.thedroidpicture.base.PermissionsActivity;
import uk.co.mezpahlan.thedroidpicture.base.PermissionsChecker;

/**
 * Activity for Rss.Detail. Sets up the fragment that is received as a result of an
 * intent from RssItem.
 */

public class DetailActivity extends AppCompatActivity {
    public static final String EXTRA_DETAIL_POSITION = "DETAIL_POSITION";
    public static final String EXTRA_DETAIL_PHOTO_LIST = "DETAIL_PHOTO_LIST";
    private static final int REQUEST_CODE = 0;
    private static final String[] PERMISSIONS = new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.SET_WALLPAPER };
    private boolean hasExternalStoragePermission = true;
    private Parcelable wrappedPhotoList;
    private int position;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rss_detail);

        final Intent intent = getIntent();
        // Get the list of photos
        wrappedPhotoList = intent.getParcelableExtra(EXTRA_DETAIL_PHOTO_LIST);
        // Get the requested position for the ViewPager
        // TODO: Should the default value be -1 so that we can check for some error?
        position = intent.getIntExtra(EXTRA_DETAIL_POSITION, 0);

        PermissionsChecker checker = new PermissionsChecker(this);

        if (checker.lacksPermissions(PERMISSIONS)) {
            startPermissionsActivity();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        initFragment(DetailFragment.newInstance(wrappedPhotoList, position, hasExternalStoragePermission));
    }

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            // disable save image button
            hasExternalStoragePermission = false;
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
